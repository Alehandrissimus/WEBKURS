package dev.marco.example.springboot.dao.impl;

import dev.marco.example.springboot.model.UserRoles;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.impl.UserImpl;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDAOImplTest {

  private UserDAOImpl userDAO;
  private static final Logger log = Logger.getLogger(UserDAOImplTest.class);

  private final String TEST_EMAIL = "test@gmail.co";
  private final String INVALID_TEST_EMAIL = "test";
  private final String TEST_FIRST_NAME = "testFirstName";
  private final String TEST_LAST_NAME = "testLastName";
  private final String TEST_PASSWORD = "testPassword";
  private final String TEST_EMAIL_CODE = "testEmailCode";
  private final String TEST_DESCRIPTION = "testDescription";

  @Autowired
  private void setUserDAO(UserDAOImpl userDAO) {
    this.userDAO = userDAO;
    try {
      userDAO.setTestConnection();
    } catch (DAOConfigException e) {
      log.error(MessagesForException.TEST_CONNECTION_ERR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getUserByNullId() {
    try {
      userDAO.getUserById(BigInteger.ZERO);
      fail();
    } catch (UserDoesNotExistException | DAOLogicException e) {
      assertTrue(true);
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getUserByAdminId() {
    try {
      assertNotNull(userDAO.getUserById(BigInteger.ONE));
    } catch (UserDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getUserByNullEmail() {
    try {
      userDAO.getUserByEmail(null);
      fail();
    } catch (UserDoesNotExistException | DAOLogicException e) {
      assertTrue(true);
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getUserByAdminEmail() {
    try {
      assertNotNull(userDAO.getUserByEmail(
          userDAO.getUserById(BigInteger.ONE).getEmail()
      ));
    } catch (UserDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void deleteUser() {
    try {
      try {
        userDAO.getUserByEmail(TEST_EMAIL);
      } catch (UserDoesNotExistException e) {
        assertTrue(true);
      }

      userDAO.createUser(
          new UserImpl.UserBuilder()
              .setFirstName(TEST_FIRST_NAME)
              .setLastName(TEST_LAST_NAME)
              .setEmail(TEST_EMAIL)
              .setPassword(TEST_PASSWORD)
              .setEmailCode(TEST_EMAIL_CODE)
              .build()
      );

      assertNotNull(userDAO.getUserByEmail(TEST_EMAIL));
      userDAO.deleteUser(userDAO.getUserByEmail(TEST_EMAIL).getId());
      try {
        userDAO.getUserByEmail(TEST_EMAIL);
      } catch (UserDoesNotExistException e) {
        assertTrue(true);
      }

    } catch (UserDoesNotExistException | DAOLogicException | UserException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void createUser() {
    try {
      try {
        userDAO.getUserByEmail(INVALID_TEST_EMAIL);
      } catch (UserDoesNotExistException e) {
        assertTrue(true);
      }
      userDAO.createUser(
          new UserImpl.UserBuilder()
              .setFirstName(TEST_FIRST_NAME)
              .setLastName(TEST_LAST_NAME)
              .setEmail(TEST_EMAIL)
              .setPassword(TEST_PASSWORD)
              .setEmailCode(TEST_EMAIL_CODE)
              .build()
      );

      assertNotNull(userDAO.getUserByEmail(TEST_EMAIL));

      userDAO.deleteUser(userDAO.getUserByEmail(TEST_EMAIL).getId());
      try {
        userDAO.getUserByEmail(TEST_EMAIL);
      } catch (UserDoesNotExistException e) {
        assertTrue(true);
      }

    } catch (UserDoesNotExistException | DAOLogicException | UserException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void updateExistUsersName() {
    try {
      String testFirstName = TEST_FIRST_NAME;
      String testLastName = TEST_LAST_NAME;

      String expected = testLastName + " " + testFirstName;
      String oldFirstName, oldLastName;
      oldLastName = userDAO.getUserById(BigInteger.ONE).getLastName();

      oldFirstName = userDAO.getUserById(BigInteger.ONE).getFirstName();

      userDAO.updateUsersFullName(BigInteger.ONE, testFirstName, testLastName);

      assertEquals(expected, userDAO.getUserById(BigInteger.ONE).getFullName());

      userDAO.updateUsersFullName(BigInteger.ONE, oldFirstName, oldLastName);

      assertEquals(oldLastName + " " + oldFirstName,
          userDAO.getUserById(BigInteger.ONE).getFullName().trim());

    } catch (UserDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }


  @Test()
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void updateNotExistUsersName() {
    try {
      userDAO.updateUsersFullName(BigInteger.ZERO, TEST_FIRST_NAME, TEST_LAST_NAME);

      userDAO.getUserById(BigInteger.ZERO).getFullName();
    } catch (DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    } catch (UserDoesNotExistException e) {
      assertTrue(true);
    }
  }


  @Test
  @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
  void updateUsersPassword() {
    try {
      userDAO.updateUsersPassword(BigInteger.ONE, TEST_PASSWORD);
      assertTrue(userDAO.comparisonOfPasswords(BigInteger.ONE, TEST_PASSWORD));

    } catch (DAOLogicException | UserDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test()
  @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
  void updateNotExistUsersPassword() {
    try {
      userDAO.updateUsersPassword(BigInteger.ZERO, TEST_PASSWORD);

    } catch (UserDoesNotExistException e) {
      assertTrue(true);
    } catch (DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getAuthorizeActiveUser() {
    try {
      userDAO.activateUser(BigInteger.ONE);
      userDAO.updateUsersPassword(BigInteger.ONE, "$2a$04$UegxIC3EpNJVeL9WUGuvK.K6GTeCfSgbfVUoC9ZUt9J.6OZF1r8Mq");
      userDAO.getAuthorizeUser(userDAO.getUserById(BigInteger.ONE).getEmail(), TEST_PASSWORD);
    } catch (DAOLogicException | UserDoesNotExistException | UserDoesNotConfirmedEmailException | UserException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void comparisonOfPasswordsTest() {
    try {
      userDAO.updateUsersPassword(BigInteger.ONE, "$2a$04$UegxIC3EpNJVeL9WUGuvK.K6GTeCfSgbfVUoC9ZUt9J.6OZF1r8Mq");
      assertTrue(userDAO.comparisonOfPasswords(BigInteger.ONE, "$2a$04$UegxIC3EpNJVeL9WUGuvK.K6GTeCfSgbfVUoC9ZUt9J.6OZF1r8Mq"));
    } catch (DAOLogicException | UserDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }

  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void updateExistUsersDescription() {
    try {
      userDAO.updateUsersDescription(BigInteger.ONE, TEST_DESCRIPTION);
      assertEquals(TEST_DESCRIPTION,
          userDAO.getUserById(BigInteger.ONE).getDescription());
    } catch (DAOLogicException | UserDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void updateNotExistUsersDescription() {
    try {
      userDAO.updateUsersDescription(BigInteger.ZERO, TEST_DESCRIPTION);

    } catch (UserDoesNotExistException e) {
      assertTrue(true);
    } catch (DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void updateExistUsersEmailCode() {
    try {
      String testEmailCode = TEST_EMAIL_CODE;
      userDAO.updateUsersEmailCode(BigInteger.ONE, testEmailCode);
      assertEquals(BigInteger.ONE, userDAO.getUserByEmailCode(testEmailCode).getId());

      String newEmailCode = INVALID_TEST_EMAIL;
      userDAO.updateUsersEmailCode(BigInteger.ONE, newEmailCode);
      assertEquals(BigInteger.ONE, userDAO.getUserByEmailCode(newEmailCode).getId());
    } catch (DAOLogicException | UserDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getUserByValidEmailCode() {

    try {
      String testEmailCode = TEST_EMAIL_CODE;
      userDAO.updateUsersEmailCode(BigInteger.ONE, testEmailCode);
      assertEquals(BigInteger.ONE, userDAO.getUserByEmailCode(testEmailCode).getId());

      String newEmailCode = INVALID_TEST_EMAIL;
      userDAO.updateUsersEmailCode(BigInteger.ONE, newEmailCode);
      assertEquals(BigInteger.ONE, userDAO.getUserByEmailCode(newEmailCode).getId());
    } catch (DAOLogicException | UserDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }

  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getUserByInvalidEmailCode() {
    try {
      String emailCode = "codeeee";
      userDAO.getUserByEmailCode(emailCode);
      fail();
    } catch (UserDoesNotExistException e) {
      assertTrue(true);
    } catch (DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void activateUnverifiedUser() {
    try {
      userDAO.createUser(
          new UserImpl.UserBuilder()
              .setEmail(TEST_EMAIL)
              .setLastName(TEST_LAST_NAME)
              .setFirstName(TEST_FIRST_NAME)
              .setPassword(TEST_PASSWORD)
              .setEmailCode(TEST_EMAIL_CODE)
              .build());

      userDAO.activateUser(userDAO.getUserByEmail(TEST_EMAIL).getId());

      assertTrue(userDAO.getUserByEmail(TEST_EMAIL).isActive());

      userDAO.deleteUser(userDAO.getUserByEmail(TEST_EMAIL).getId());

    } catch (DAOLogicException | UserDoesNotExistException | UserException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }

  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void activateVerifiedUser() {
    try {
      userDAO.activateUser(BigInteger.ONE);
      assertTrue(userDAO.getUserById(BigInteger.ONE).isActive());
    } catch (DAOLogicException | UserDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }

  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void disactivateVerifiedUser() {
    try {
      userDAO.disactivateUser(BigInteger.ONE);
      assertFalse(userDAO.getUserById(BigInteger.ONE).isActive());
    } catch (DAOLogicException | UserDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }

  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void disactivateUnverifiedUser() {
    try {
      userDAO.createUser(
          new UserImpl.UserBuilder()
              .setEmail(TEST_EMAIL)
              .setLastName(TEST_LAST_NAME)
              .setFirstName(TEST_FIRST_NAME)
              .setPassword(TEST_PASSWORD)
              .setEmailCode(TEST_EMAIL_CODE)
              .build());

      userDAO.disactivateUser(userDAO.getUserByEmail(TEST_EMAIL).getId());

      assertFalse(userDAO.getUserByEmail(TEST_EMAIL).isActive());

      userDAO.deleteUser(userDAO.getUserByEmail(TEST_EMAIL).getId());

    } catch (DAOLogicException | UserDoesNotExistException | UserException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  void testUpdateUserRole() {
    try {
      userDAO.updateUserRole(BigInteger.ONE, UserRoles.USER);
      assertEquals(UserRoles.USER, userDAO.getUserById(BigInteger.ONE).getUserRole());

    } catch (DAOLogicException | UserDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  void testGetUserPasswordByEmail() {
    try {
      assertNotNull(userDAO.getUserPasswordByEmail(userDAO.getUserById(BigInteger.ONE).getEmail()));
    } catch (UserDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }
}
