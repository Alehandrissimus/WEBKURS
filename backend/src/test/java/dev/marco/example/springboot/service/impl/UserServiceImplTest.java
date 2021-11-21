package dev.marco.example.springboot.service.impl;

import dev.marco.example.springboot.model.impl.QuizAccomplishedImpl;
import java.util.Set;

import dev.marco.example.springboot.model.impl.QuizImpl;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.dao.UserDAO;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.service.UserService;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static dev.marco.example.springboot.exception.MessagesForException.DAO_LOGIC_EXCEPTION;
import static dev.marco.example.springboot.exception.MessagesForException.ERROR_WHILE_SETTING_TEST_CONNECTION;

@SpringBootTest
class UserServiceImplTest {

  private static final Logger log = Logger.getLogger(UserServiceImplTest.class);
  private UserService userService;
  private UserDAO userDAO;

  @Autowired
  private void setTestConnection(UserService userService, UserDAO userDAO) {
    this.userDAO = userDAO;
    this.userService = userService;
    try {
      userService.setTestConnection();
      userDAO.setTestConnection();
    } catch (DAOConfigException e) {
      log.error(ERROR_WHILE_SETTING_TEST_CONNECTION + e.getMessage());
    }
  }

  @Test
  void buildNewInvalidEmailUserTest() {
    try {
      userService.buildNewUser("asd", "qwertyui", "asd", "asd");
      log.error(MessagesForException.INVALID_EMAIL);
      fail();
    } catch (UserException | DAOLogicException e) {
      assertTrue(true);
    }
  }

  @Test
  void buildNewInvalidPasswordUserTest() {
    try {
      userService.buildNewUser("max.bataiev@gmail.com", "qwe", "asd", "asd");
      log.error(MessagesForException.INVALID_PASSWORD);
      fail();
    } catch (UserException | DAOLogicException e) {
      assertTrue(true);
    }
  }

  @Test
  void buildNewInvalidFirstNameUserTest() {
    try {
      userService.buildNewUser("max.bataiev@gmail.com", "qwertyui", "a", "asd");
      log.error(MessagesForException.INVALID_USERS_FIRST_NAME);
      fail();
    } catch (UserException | DAOLogicException e) {
      assertTrue(true);
    }
  }

  @Test
  void buildNewInvalidLastNameUserTest() {
    try {
      userService.buildNewUser("max.bataiev@gmail.com", "qwertyui", "asd", "a");
      log.error(MessagesForException.INVALID_USERS_LAST_NAME);
      fail();
    } catch (UserException | DAOLogicException e) {
      assertTrue(true);
    }
  }

  @Test
  void testAuthorizeNull() {
    try {
      userService.authorize(null);
    } catch (UserException | UserDoesNotExistException e) {
      assertTrue(true);
    } catch (DAOLogicException e) {
      log.error(DAO_LOGIC_EXCEPTION);
      fail();
    }
  }

  @Test
  void testAuthorizeActiveValidUser() {
    try {
      String pass = "testPassword";
      String password = "$2a$04$UegxIC3EpNJVeL9WUGuvK.K6GTeCfSgbfVUoC9ZUt9J.6OZF1r8Mq";
      userDAO.activateUser(BigInteger.TWO);
      userDAO.updateUsersPassword(BigInteger.TWO, password);
      User user = userDAO.getUserById(BigInteger.TWO);
      user.setPassword(pass);
      assertNotNull(userService.authorize(user));
    } catch (DAOLogicException | UserException | UserDoesNotExistException e) {
      log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
      fail();
    }
  }

  @Test
  void testAuthorizeNotActiveValidUser() {
    try {
      userDAO.updateUsersPassword(BigInteger.TWO, "$2a$04$UegxIC3EpNJVeL9WUGuvK.K6GTeCfSgbfVUoC9ZUt9J.6OZF1r8Mq");
      userDAO.disactivateUser(BigInteger.TWO);
      User user = userDAO.getUserById(BigInteger.TWO);
      user.setPassword("testPassword");
      userService.authorize(user);
      log.error(MessagesForException.USERS_DOESNT_EXIT);
      fail();
    } catch (DAOLogicException | UserException | UserDoesNotExistException e) {
      assertTrue(true);
    }
  }

  @Test
  void testRecoverPassword() {
    try {
      userDAO.updateUsersPassword(BigInteger.TWO, "testPassword");
      userDAO.activateUser(BigInteger.TWO);

      userService.recoverPassword(userDAO.getUserById(BigInteger.TWO));

      assertFalse(userDAO.comparisonOfPasswords(BigInteger.TWO, "testPassword"));
    } catch (DAOLogicException | UserException | MailException | UserDoesNotExistException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testValidateNewUser() {
    try {
      userService
          .validateNewUser("max.bataiev@gmail.com", "Qwerty123", "testFirstName", "testLastName");
      assertTrue(true);
    } catch (UserException e) {
      fail();
    }
  }

  @Test
  void testNotValidEmailValidateNewUser() {
    try {
      userService
          .validateNewUser("max.bataievgmail.com", "qwertyui", "testFirstName", "testLastName");
      fail();
    } catch (UserException e) {
      assertTrue(true);
    }
  }

  @Test
  void testNotValidPasswordValidateNewUser() {
    try {
      userService.validateNewUser("max.bataiev@gmail.com", "", "testFirstName", "testLastName");
      fail();
    } catch (UserException e) {
      assertTrue(true);
    }
  }

  @Test
  void testNotValidFirstNameValidateNewUser() {
    try {
      userService.validateNewUser("max.bataiev@gmail.com", "Qwerty123", "te", "testLastName");
      fail();
    } catch (UserException e) {
      assertTrue(true);
    }
  }

  @Test
  void testNotValidLastNameValidateNewUser() {
    try {
      userService.validateNewUser("max.bataiev@gmail.com", "qwertyui", "testFirstName", "te");
      fail();
    } catch (UserException e) {
      assertTrue(true);
    }
  }

  @Test
  void testUserGetById() {
    try {
      assertNotNull(userService.getUserById(BigInteger.ONE));
    } catch (DAOLogicException | UserDoesNotExistException e) {
      fail();
    }
  }

  @Test
  void testUserGetByInvalidId() {
    try {
      userService.getUserById(null);
      fail();
    } catch (DAOLogicException | UserDoesNotExistException e) {
      assertTrue(true);
    }
  }

  @Test
  void testComparisonOfPasswrods() {
    try {
      userDAO.updateUsersPassword(BigInteger.TWO, "testPassword");
      assertTrue(userService.comparisonOfPasswords(BigInteger.TWO, "testPassword"));
    } catch (DAOLogicException | UserDoesNotExistException e) {
      fail();
    }
  }

  @Test
  void testActiveUser() {
    try {
      userDAO.activateUser(BigInteger.TWO);
      assertTrue(userDAO.getUserById(BigInteger.TWO).isActive());
    } catch (DAOLogicException | UserDoesNotExistException e) {
      fail();
    }
  }

  @Test
  void testDisactiveUser() {
    try {
      userDAO.disactivateUser(BigInteger.TWO);
      assertFalse(userDAO.getUserById(BigInteger.TWO).isActive());
    } catch (DAOLogicException | UserDoesNotExistException e) {
      fail();
    }
  }

  @Test
  void testGetAccomplishedQuizesByUser() {
    try {
      userService.addAccomplishedQuiz(BigInteger.ONE, new QuizAccomplishedImpl(1, QuizImpl.QuizBuilder().setId(BigInteger.ONE).build()));
      assertNotNull(userService.getAccomplishedQuizesByUser(BigInteger.ONE));
    } catch (DAOLogicException | QuizDoesNotExistException | UserDoesNotExistException | QuizException e) {
      fail();
    }
  }

  @Test
  void testGetFavoriteQuizes() {
    try {
      userService
          .addAccomplishedQuiz(BigInteger.ONE, new QuizAccomplishedImpl(1, true, QuizImpl.QuizBuilder().setId(BigInteger.ONE).build()));
      assertNotNull(userService.getFavoriteQuizesByUser(BigInteger.ONE));
    } catch (DAOLogicException | UserDoesNotExistException | QuizDoesNotExistException | QuizException e) {
      e.printStackTrace();
      log.error(e.getMessage());
      fail();
    }
  }

  @Test
  void testGetAccomplishedQuizById() {
    try {
      assertNotNull(userService.getAccomplishedQuizById(BigInteger.ONE, BigInteger.ONE));
    } catch (QuizDoesNotExistException | DAOLogicException e) {
      log.error(e.getMessage());
      fail();
    }
  }

  @Test
  void testIsAccomplishedQuiz() {
    try {
      assertTrue(userService.isAccomplishedQuiz(BigInteger.ONE, BigInteger.ONE));
    } catch (DAOLogicException e) {
      log.error(e.getMessage());
      fail();
    }
  }
}