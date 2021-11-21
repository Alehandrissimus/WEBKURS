package dev.marco.example.springboot.rest;

import com.fasterxml.jackson.databind.JsonNode;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.UserRoles;
import dev.marco.example.springboot.model.impl.QuizAccomplishedImpl;
import dev.marco.example.springboot.model.impl.UserImpl;
import dev.marco.example.springboot.security.JwtTokenProvider;
import dev.marco.example.springboot.service.MailSenderService;
import dev.marco.example.springboot.service.UserService;
import dev.marco.example.springboot.util.ApiAddresses;
import dev.marco.example.springboot.util.ControllerUtil;
import dev.marco.example.springboot.util.RegexPatterns;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static dev.marco.example.springboot.exception.MessagesForException.*;

@RestController
public class UserController implements RegexPatterns, ApiAddresses {

    private final Properties properties = new Properties();
    private final UserService userService;
    private final MailSenderService mailSenderService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private static final Logger log = Logger.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, MailSenderService mailSenderService,
                          AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider)  {
        this.userService = userService;
        this.mailSenderService = mailSenderService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

        try {
            ControllerUtil.getProperty(properties);
        } catch (ControllerConfigException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(CONFIG_EXCEPTION));
        }
    }

    public void setTestConnection() throws DAOConfigException {
        userService.setTestConnection();
        mailSenderService.setTestConnection();
    }

    @PostMapping(API_REGISTER_USER)
    public ResponseEntity<Object> createUser(@RequestBody UserImpl user) {
        try {
            userService.validateNewUser(
                    user.getEmail(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName());
            BigInteger userId = userService.buildNewUser(
                    user.getEmail(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName());

            User user1 = new UserImpl.UserBuilder()
                    .setId(userId)
                    .setEmail(user.getEmail())
                    .build();

            if (!mailSenderService.sendEmail(user1)) {
                throw new MailException(EMAIL_ERROR);
            }
            return ResponseEntity.ok().build();
        } catch (DAOLogicException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (MailException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(MAIL_EXCEPTION));
        } catch (UserException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @PostMapping(API_AUTHORIZE_USER)
    public ResponseEntity<Map<String, Object>> tryToAuthorize(@RequestBody UserImpl user) {
        try {
            if (StringUtils.isEmpty(user.getEmail()) || !user.getEmail().matches(mailPattern)) {
                log.error("tryToAuthorize email not valid");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(MAIL_INVALID));
            }
            if (StringUtils.isEmpty(user.getPassword())) {
                log.error("tryToAuthorize password not valid");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(PASSWORD_INVALID));
            }

            User receivedUser = userService.authorize(user);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            String token = jwtTokenProvider.createToken(user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("user", receivedUser);
            response.put("jwt", token);

            return ResponseEntity.ok(response);
        } catch (DAOLogicException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (UserException | UserDoesNotExistException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        } catch (AuthenticationException e) {
            log.error("AuthenticationException " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(AUTHENTICATION_EXCEPTION));
        }
    }

    @PostMapping(API_RECOVER_PASSWORD)
    public ResponseEntity<Object> recoverPassword(@RequestBody UserImpl receivedUser) {
        try {
            if (StringUtils.isEmpty(receivedUser.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(MAIL_INVALID));
            }
            User user = new UserImpl.UserBuilder()
                    .setEmail(receivedUser.getEmail())
                    .build();
            if (!userService.recoverPassword(user)) {
                log.error(MessagesForException.EMAIL_ERROR);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(MAIL_EXCEPTION));
            }
            return ResponseEntity.ok().build();
        } catch (DAOLogicException e) {
            log.error("Error while recoverPassword()" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (MailException e) {
            log.error("Error while recoverPassword()" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(MAIL_EXCEPTION));
        } catch (UserException e) {
            log.error("Error while recoverPassword() " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @PutMapping(API_UPDATE_PASSWORD)
    public void updatePassword(@PathVariable BigInteger id, @RequestBody JsonNode requestBody)
            throws UserException {
        String oldPassword = requestBody.get("oldPass").asText();
        String newPassword = requestBody.get("newPass").asText();
        String confirmPassword = requestBody.get("confirmPass").asText();
        try {
            if (!newPassword.equals(confirmPassword)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(USER_EXCEPTION));
            }
            if (id == null) {
                throw new UserDoesNotExistException(USERS_DOESNT_EXIT);
            }
            if (StringUtils.isBlank(newPassword)) {
                throw new UserException(MessagesForException.INVALID_PASSWORD);
            }
            userService.updateUsersPassword(id, oldPassword, newPassword);
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (UserDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @PostMapping(API_CONFIRM_EMAIL)
    public ResponseEntity<Object> confirmEmail(@RequestParam String code) {
        try {
            if (StringUtils.isEmpty(code)) {
                throw new MailException(MessagesForException.EMAIL_ERROR);
            }

            User user = mailSenderService.confirmEmail(code);

            if (user == null) {
                throw new MailException(EMAIL_ERROR);
            }
            return ResponseEntity.ok().build();
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (MailException e) {
            log.error(EMAIL_ERROR + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(MAIL_EXCEPTION));
        } catch (UserException e) {
            log.error(USER_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @GetMapping(API_GET_USER)
    public User getUser(@PathVariable BigInteger idUser) {
        try {
            return userService.getUserById(idUser);
        } catch (UserDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        }
    }

    @GetMapping(API_GET_USER_BY_TOKEN)
    public User getUserByToken(@RequestHeader(name = "Authorization") String token) {
        try {
            String email = jwtTokenProvider.getEmailFromToken(token.substring(7));
            return userService.getUserByEmail(email);
        } catch (UserDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        }
    }

    @DeleteMapping(API_DELETE_USER)
    public void deleteUser(@PathVariable BigInteger idUser) {
        try {
            userService.deleteUser(idUser);
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (UserDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @PutMapping(API_EDIT_USER)
    public void editUser(@RequestBody UserImpl user, @PathVariable BigInteger idUser) {
        try {
            if (user.getFirstName() != null && user.getLastName() != null) {
                if (user.getFirstName().isBlank() || user.getLastName().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(NAME_INVALID));
                } else {
                    userService.updateUsersFullName(idUser, user.getFirstName(), user.getLastName());
                }
            }
            if (user.getDescription() != null) {
                if (user.getDescription().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(DESCRIPTION_INVALID));
                } else {
                    userService.updateUsersDescription(idUser, user.getDescription());
                }
            }
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (UserDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }


    @GetMapping(API_GET_ACCOMPLISHED_QUIZZES_BY_USER)
    public Set<QuizAccomplishedImpl> getAccomplishedQuizzesByUser(@PathVariable BigInteger userId) {
        try {
            return userService.getAccomplishedQuizesByUser(userId);
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (QuizDoesNotExistException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(QUIZ_EXCEPTION));
        } catch (UserDoesNotExistException e) {
            log.error(USER_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @GetMapping(API_GET_FAVORITE_QUIZZES_BY_USER)
    public Set<QuizAccomplishedImpl> getFavoriteQuizzesByUser(@PathVariable BigInteger id) {
        try {
            return userService.getFavoriteQuizesByUser(id);
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (QuizDoesNotExistException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(QUIZ_EXCEPTION));
        } catch (UserDoesNotExistException e) {
            log.error(USER_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @GetMapping("/changeRole/{id}")
    public void updateUserRole(@PathVariable(name = "id") BigInteger id,
                               @RequestParam(name = "role") UserRoles role) {
        try {
            userService.updateUserRole(id, role);
        } catch (UserDoesNotExistException | UserException e) {
            log.error(USER_NOT_FOUND_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
