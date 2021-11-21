package dev.marco.example.springboot.service.impl;

import dev.marco.example.springboot.model.UserRoles;
import dev.marco.example.springboot.security.JwtUser;
import dev.marco.example.springboot.util.RegexPatterns;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import dev.marco.example.springboot.dao.UserAccomplishedQuizDAO;
import dev.marco.example.springboot.dao.UserDAO;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.impl.QuizAccomplishedImpl;
import dev.marco.example.springboot.model.impl.UserImpl;
import dev.marco.example.springboot.service.MailSenderService;
import dev.marco.example.springboot.service.UserService;

import java.math.BigInteger;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.marco.example.springboot.exception.MessagesForException.*;

@Service
public class UserServiceImpl implements UserService, RegexPatterns {

  private static final Logger log = Logger.getLogger(UserServiceImpl.class);

  private final UserDAO userDAO;
  private final UserAccomplishedQuizDAO userAccomplishedQuizDAO;
  private final MailSenderService mailSenderService;

  @Autowired
  public UserServiceImpl(UserDAO userDAO, UserAccomplishedQuizDAO userAccomplishedQuizDAO,
      MailSenderServiceImpl mailSenderService) {
    this.userDAO = userDAO;
    this.userAccomplishedQuizDAO = userAccomplishedQuizDAO;
    this.mailSenderService = mailSenderService;
  }

  @Override
  public void setTestConnection() throws DAOConfigException {
    userDAO.setTestConnection();
    userAccomplishedQuizDAO.setTestConnection();
    mailSenderService.setTestConnection();
  }


  @Override
  public BigInteger buildNewUser(String email, String password, String firstName, String lastName)
      throws UserException, DAOLogicException {
    try {
      validateNewUser(email, password, firstName, lastName);
      log.debug("User with email=" + email + " was validated!");

      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

      try {
        if (userDAO.getUserByEmail(email) != null) {
          throw new UserException(USER_ALREADY_EXIST);
        }
      } catch (UserDoesNotExistException e) {
        log.debug("User with email=" + email + " doesn't exist");
      }

      User user = new UserImpl.UserBuilder()
          .setFirstName(firstName)
          .setLastName(lastName)
          .setEmail(email)
          .setPassword(encoder.encode(password))
          .build();
      log.debug("User with email=" + email + " was build!");
      return userDAO.createUser(user);
    } catch (DAOLogicException e) {
      log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION + e.getMessage(), e);
    }

  }


  @Override
  public User authorize(User user)
      throws DAOLogicException, UserException, UserDoesNotExistException {
    try {
      if (user != null) {
        return userDAO.getAuthorizeUser(user.getEmail(), user.getPassword());
      }
      throw new UserException(USERS_DOESNT_EXIT);
    } catch (DAOLogicException | UserDoesNotConfirmedEmailException e) {
      log.info(DAO_LOGIC_EXCEPTION + e.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION + e.getMessage(), e);
    } catch (UserDoesNotExistException e) {
      log.info(USERS_DOESNT_EXIT);
      throw new UserDoesNotExistException(USERS_DOESNT_EXIT + e.getMessage(), e);
    }
  }

  @Override
  public boolean recoverPassword(User user) throws UserException, DAOLogicException, MailException {
    try {
      if (userDAO.getUserByEmail(user.getEmail()).isActive()) {
        log.debug("User was found and active");
        return mailSenderService.generateNewPassword(user.getEmail());
      }
      log.error(EMAIL_ERROR);
      throw new MailException(EMAIL_ERROR);
    } catch (UserDoesNotExistException e) {
      log.error(MessagesForException.USER_NOT_FOUND_EXCEPTION + e.getMessage());
      throw new UserException(USER_NOT_FOUND_EXCEPTION, e);
    } catch (DAOLogicException e) {
      log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
    } catch (MailException e) {
      log.error(MessagesForException.EMAIL_ERROR + e.getMessage());
      throw new MailException(EMAIL_ERROR, e);
    }
  }

  @Override
  public void validateNewUser(String email, String password, String firstName, String lastName)
      throws UserException {
    if (StringUtils.isEmpty(email)) {
      log.error(MessagesForException.INVALID_USERS_EMAIL);
      throw new UserException(INVALID_USERS_EMAIL);
    }
    if (!email.matches(EMAIL_PATTERN)) {
      log.error(MessagesForException.INVALID_USERS_EMAIL);
      throw new UserException(INVALID_USERS_EMAIL);
    }
    if (StringUtils.isEmpty(password)) {
      log.error(EMPTY_PASSWORD);
      throw new UserException(INVALID_USERS_EMAIL);
    }
    if (StringUtils.isEmpty(password)) { //!password.matches(passPattern)
      log.error(EMPTY_PASSWORD);
      throw new UserException(INVALID_USERS_EMAIL);
    }
    if (StringUtils.isEmpty(firstName)) {
      log.error(EMPTY_FIRST_NAME);
      throw new UserException(INVALID_USERS_FIRST_NAME);
    }
    if (firstName.length() < 3) {
      log.error(EMPTY_FIRST_NAME);
      throw new UserException(INVALID_USERS_FIRST_NAME);
    }
    if (StringUtils.isEmpty(lastName)) {
      log.error(EMPTY_LAST_NAME);
      throw new UserException(INVALID_USERS_LAST_NAME);
    }
    if (lastName.length() < 3) {
      log.error(EMPTY_LAST_NAME);
      throw new UserException(INVALID_USERS_LAST_NAME);
    }
  }


  @Override
  public User getUserById(BigInteger id) throws UserDoesNotExistException, DAOLogicException {
    if (id == null) {
      log.error(USER_NOT_FOUND_EXCEPTION);
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    if (id.longValue() < 1) {
      log.error(USER_NOT_FOUND_EXCEPTION);
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    return userDAO.getUserById(id);
  }

  @Override
  public User getUserByEmail(String email) throws UserDoesNotExistException, DAOLogicException {
    if (StringUtils.isEmpty(email)) {
      log.error(USER_NOT_FOUND_EXCEPTION);
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    return userDAO.getUserByEmail(email);
  }

  @Override
  public void deleteUser(BigInteger id) throws DAOLogicException, UserDoesNotExistException {
    if (id == null) {
      log.error(USER_NOT_FOUND_EXCEPTION);
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    userDAO.deleteUser(id);
  }

  @Override
  public void updateUsersFullName(BigInteger id, String newFirstName, String newLastName)
      throws DAOLogicException, UserDoesNotExistException {
    User userFromDAO = userDAO.getUserById(id);
    if (userFromDAO == null) {
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    userDAO.updateUsersFullName(id, newFirstName, newLastName);
  }

  @Override
  public void updateUsersPassword(BigInteger id, String oldPassword, String newPassword)
          throws DAOLogicException, UserDoesNotExistException, UserException {
    User userFromDAO = userDAO.getUserById(id);
    String dbPass = userDAO.getUserPasswordByEmail(userFromDAO.getEmail());
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    if(!encoder.matches(oldPassword, dbPass)){
      throw new UserException(MessagesForException.WRONG_PASSWORD);
    }
    userDAO.updateUsersPassword(id, encoder.encode(newPassword));
  }

  @Override
  public void updateUsersDescription(BigInteger id, String newDescription)
      throws DAOLogicException, UserDoesNotExistException {
    User userFromDAO = userDAO.getUserById(id);
    if (userFromDAO == null) {
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    userDAO.updateUsersDescription(id, newDescription);
  }

  @Override
  public User getUserByEmailCode(String code) throws UserDoesNotExistException, DAOLogicException {
    return userDAO.getUserByEmailCode(code);
  }

  @Override
  public void updateUsersEmailCode(BigInteger id, String newCode)
      throws DAOLogicException, UserDoesNotExistException {
    User userFromDAO = userDAO.getUserById(id);
    if (userFromDAO == null) {
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    userDAO.updateUsersEmailCode(id, newCode);
  }

  @Override
  public boolean comparisonOfPasswords(BigInteger id, String checkPassword)
      throws DAOLogicException, UserDoesNotExistException {
    User userFromDAO = userDAO.getUserById(id);
    if (userFromDAO == null) {
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    return userDAO.comparisonOfPasswords(id, checkPassword);
  }

  @Override
  public boolean activateUser(BigInteger id) throws DAOLogicException, UserDoesNotExistException {
    User userFromDAO = userDAO.getUserById(id);
    if (userFromDAO == null) {
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    return userDAO.activateUser(id);
  }

  @Override
  public boolean disactivateUser(BigInteger id)
      throws DAOLogicException, UserDoesNotExistException {
    User userFromDAO = userDAO.getUserById(id);
    if (userFromDAO == null) {
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    return userDAO.disactivateUser(id);
  }


  @Override
  public Set<QuizAccomplishedImpl> getAccomplishedQuizesByUser(BigInteger id)
      throws DAOLogicException, QuizDoesNotExistException, UserDoesNotExistException {
    User userFromDAO = userDAO.getUserById(id);
    if (userFromDAO == null) {
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    return userAccomplishedQuizDAO.getAccomplishedQuizesByUser(id);
  }

  @Override
  public Set<QuizAccomplishedImpl> getFavoriteQuizesByUser(BigInteger id)
      throws DAOLogicException, UserDoesNotExistException {
    userDAO.getUserById(id);

    return userAccomplishedQuizDAO.getFavoriteQuizesByUser(id);
  }

  @Override
  public void editAccomplishedQuiz(BigInteger idUser, QuizAccomplishedImpl newQuiz)
      throws DAOLogicException {
    userAccomplishedQuizDAO.editAccomplishedQuiz(idUser, newQuiz);
  }

  @Override
  public void setIsFavoriteQuiz(BigInteger idUser, BigInteger idQuiz, int isFavourite)
      throws DAOLogicException {
    userAccomplishedQuizDAO.setIsFavoriteQuiz(idUser, idQuiz, isFavourite);
  }

  @Override
  public void addAccomplishedQuiz(BigInteger id, QuizAccomplishedImpl quiz)
      throws DAOLogicException {
    userAccomplishedQuizDAO.addAccomplishedQuiz(id, quiz);
  }

  @Override
  public QuizAccomplishedImpl getAccomplishedQuizById(BigInteger idUser, BigInteger idQuiz)
      throws QuizDoesNotExistException, DAOLogicException {
    return userAccomplishedQuizDAO.getAccomplishedQuizById(idUser, idQuiz);
  }

  @Override
  public boolean isAccomplishedQuiz(BigInteger idUser, BigInteger idQuiz) throws DAOLogicException {
    return userAccomplishedQuizDAO.isAccomplishedQuiz(idUser, idQuiz);
  }

  @Override
  public void updateUserRole(BigInteger id, UserRoles role) throws UserDoesNotExistException, DAOLogicException, UserException {
    User userFromDAO = userDAO.getUserById(id);
    if (userFromDAO == null) {
      throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
    }
    JwtUser userJwt = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    BigInteger userId = BigInteger.valueOf(userJwt.getId());
    User user = userDAO.getUserById(userId);
    if (!user.getUserRole().equals(UserRoles.ADMIN)) {
      log.error(DONT_ENOUGH_RIGHTS);
      throw new UserException(DONT_ENOUGH_RIGHTS);
    }
    userDAO.updateUserRole(id, role);
  }
}

