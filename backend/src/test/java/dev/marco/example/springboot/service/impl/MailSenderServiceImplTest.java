package dev.marco.example.springboot.service.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.dao.impl.UserDAOImpl;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.impl.UserImpl;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailSenderServiceImplTest {

  private static final Logger log = Logger.getLogger(MailSenderServiceImplTest.class);

  private MailSenderServiceImpl mailSenderService;
  private UserDAOImpl userDAO;

  @Autowired
  private void setTestConnection(MailSenderServiceImpl mailSenderService, UserDAOImpl userDAO) {
    this.mailSenderService = mailSenderService;
    this.userDAO = userDAO;
    try {
      mailSenderService.setTestConnection();
      userDAO.setTestConnection();
    } catch (DAOConfigException e) {
      log.error(MessagesForException.DAO_CONFIG_EXCEPTION + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 40000, unit = TimeUnit.MILLISECONDS)
  void testSendEmail() {
    try {
      try {
        userDAO.getUserByEmail("test@gmail.com");
      } catch (DAOLogicException e) {
        log.error(MessagesForException.DAO_LOGIC_EXCEPTION + e.getMessage());
        fail();
      } catch (UserDoesNotExistException e) {
        userDAO.createUser(
            new UserImpl.UserBuilder()
                .setId(BigInteger.ONE)
                .setFirstName("test")
                .setLastName("test")
                .setPassword("Qwerty123")
                .setEmail("test@gmail.com")
                .build()
        );
      }

      assertTrue(
          mailSenderService.sendEmail(
              new UserImpl.UserBuilder()
                  .setId(BigInteger.ONE)
                  .setFirstName("test")
                  .setLastName("test")
                  .setPassword("Qwerty123")
                  .setEmail("test@gmail.com")
                  .build()
          ));
    } catch (UserException | MailException | DAOLogicException e) {
      log.error(MessagesForException.DAO_LOGIC_EXCEPTION + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void testInvalidSendEmail() {
    try {
      mailSenderService.sendEmail(
          new UserImpl.UserBuilder()
              .setId(BigInteger.ONE)
              .setFirstName("test")
              .setLastName("test")
              .setPassword("test")
              .setEmail("wrong.email@gmail.ua")
              .build()
      );
      log.error(MessagesForException.INVALID_EMAIL);
      fail();
    } catch (UserException | MailException e) {
      assertTrue(true);
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void testGenerateCode() {
    try {
      String code = mailSenderService.generateCode();
      userDAO.getUserByEmailCode(code);
    } catch (UserDoesNotExistException e) {
      assertTrue(true);
    } catch (DAOLogicException e) {
      log.error(MessagesForException.DAO_LOGIC_EXCEPTION + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void testConfirmEmail() {
    try {
      userDAO.updateUsersEmailCode(BigInteger.TWO, "testCode");
      userDAO.disactivateUser(BigInteger.TWO);

      User user = mailSenderService.confirmEmail("testCode");
      assertNull(user.getEmailCode());
      assertTrue(user.isActive());
    } catch (DAOLogicException | UserDoesNotExistException | UserException e) {
      log.error(MessagesForException.DAO_LOGIC_EXCEPTION + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void testConfirmEmailByWrongCode() {
    try {
      mailSenderService.confirmEmail(mailSenderService.generateCode());
      fail();
    } catch (UserException e) {
      assertTrue(true);
    } catch (DAOLogicException e) {
      log.error(MessagesForException.DAO_LOGIC_EXCEPTION + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 40000, unit = TimeUnit.MILLISECONDS)
  void testGenerateNewPassword() {
    try {
      User user = userDAO.getUserById(BigInteger.TWO);
      String email = user.getEmail();
      String password = "testPassword";
      userDAO.updateUsersPassword(BigInteger.TWO, password);
      mailSenderService.generateNewPassword(email);
      assertFalse(userDAO.comparisonOfPasswords(BigInteger.TWO, password));
    } catch (DAOLogicException | MailException | UserDoesNotExistException e) {
      log.error(MessagesForException.DAO_LOGIC_EXCEPTION + e.getMessage());
      fail();
    }
  }
}