package dev.marco.example.springboot.dao.impl;

import dev.marco.example.springboot.model.impl.QuizImpl;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.impl.QuizAccomplishedImpl;

import java.math.BigInteger;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static dev.marco.example.springboot.exception.MessagesForException.ERROR_WHILE_SETTING_TEST_CONNECTION;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserAccomplishedQuizDAOImplTest {

  private UserAccomplishedQuizDAOImpl userAccomplishedQuizDAO;

  private static final Logger log = Logger.getLogger(UserAnnouncementDAOImpl.class);

  @Autowired
  void setAccomplishedQuizDAO(UserAccomplishedQuizDAOImpl userAccomplishedQuizDAO) {
    this.userAccomplishedQuizDAO = userAccomplishedQuizDAO;
    try {
      userAccomplishedQuizDAO.setTestConnection();
    } catch (DAOConfigException e) {
      log.error(ERROR_WHILE_SETTING_TEST_CONNECTION + e.getMessage());
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void addAccomplishedQuiz() {
    try {
      userAccomplishedQuizDAO.addAccomplishedQuiz(BigInteger.ONE, new QuizAccomplishedImpl(
          10, false, QuizImpl.QuizBuilder().setId(BigInteger.ONE).build()));
      QuizAccomplishedImpl accomplished = userAccomplishedQuizDAO.getAccomplishedQuizById(
          BigInteger.ONE, BigInteger.ONE);
      assertNotNull(accomplished);
    } catch (DAOLogicException | QuizDoesNotExistException | QuizException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void editAccomplishedQuiz() {
    try {
      userAccomplishedQuizDAO.editAccomplishedQuiz(BigInteger.ONE, new QuizAccomplishedImpl(
          10, QuizImpl.QuizBuilder().setId(BigInteger.ONE).build()));
      QuizAccomplishedImpl accomplished = userAccomplishedQuizDAO.getAccomplishedQuizById(
          BigInteger.ONE, BigInteger.ONE);
      assertNotNull(accomplished);
      assertEquals(10, accomplished.getCorrectAnswers());
    } catch (DAOLogicException | QuizDoesNotExistException | QuizException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getAccomplishedQuizzesByUser() {
    try {
      Set<QuizAccomplishedImpl> accomplishedSet = userAccomplishedQuizDAO.getAccomplishedQuizesByUser(
          BigInteger.valueOf(4));
      assertNotNull(accomplishedSet);
      for (QuizAccomplishedImpl quizAccomplished : accomplishedSet) {
        assertNotNull(quizAccomplished);
      }
    } catch (DAOLogicException | QuizDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void setIsFavoriteQuiz() {
    try {
      userAccomplishedQuizDAO.setIsFavoriteQuiz(BigInteger.ONE, BigInteger.ONE, 0);
      assertFalse(userAccomplishedQuizDAO.getAccomplishedQuizById(BigInteger.ONE, BigInteger.ONE)
          .getFavourite());

      userAccomplishedQuizDAO.setIsFavoriteQuiz(BigInteger.ONE, BigInteger.ONE, 1);
      assertTrue(userAccomplishedQuizDAO.getAccomplishedQuizById(BigInteger.ONE, BigInteger.ONE)
          .getFavourite());
    } catch (DAOLogicException | QuizDoesNotExistException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getAccomplishedQuizById() {
    try {
      QuizAccomplishedImpl quizAccomplished = userAccomplishedQuizDAO.getAccomplishedQuizById(
          BigInteger.valueOf(4), BigInteger.valueOf(4));
      assertNotNull(quizAccomplished);
    } catch (QuizDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void isAccomplishedQuiz() {
    try {
      assertTrue(userAccomplishedQuizDAO.isAccomplishedQuiz(BigInteger.ONE, BigInteger.ONE));
      assertFalse(userAccomplishedQuizDAO.isAccomplishedQuiz(BigInteger.ONE, BigInteger.TWO));
    } catch (DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }

  }
}