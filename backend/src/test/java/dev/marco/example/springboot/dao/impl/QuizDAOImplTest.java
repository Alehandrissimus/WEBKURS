package dev.marco.example.springboot.dao.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Quiz;
import dev.marco.example.springboot.model.QuizType;
import dev.marco.example.springboot.model.impl.QuizImpl;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static dev.marco.example.springboot.exception.MessagesForException.ERROR_WHILE_SETTING_TEST_CONNECTION;

@SpringBootTest
class QuizDAOImplTest {

  private QuizDAOImpl quizDAO;
  private static final Logger log = Logger.getLogger(QuizDAOImplTest.class);

  @Autowired
  private void setQuizDAO(QuizDAOImpl quizDAO) {
    this.quizDAO = quizDAO;
    try {
      quizDAO.setTestConnection();
    } catch (DAOConfigException e) {
      log.error(ERROR_WHILE_SETTING_TEST_CONNECTION + e.getMessage());
      fail();
    }
  }


  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void createQuizTest() {
    try {
      String title = "History Quiz";
      String description = "Historical quiz";
      QuizType quizType = QuizType.HISTORIC;
      Quiz quiz = QuizImpl.QuizBuilder()
          .setTitle(title)
          .setDescription(description)
          .setQuizType(quizType)
          .setCreationDate(new Date(System.currentTimeMillis()))
          .setCreatorId(BigInteger.valueOf(3))
          .build();

      Quiz newQuiz = quizDAO.createQuiz(quiz);

      assertNotNull(newQuiz);
      assertEquals(title, newQuiz.getTitle());

      quizDAO.deleteQuiz(newQuiz);
    } catch (QuizDoesNotExistException | DAOLogicException | QuizException e) {
      log.error(MessagesForException.TEST_ERROR, e);
      fail();
    }

  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getQuizByIdTest() {
    try {
      Quiz quiz = quizDAO.getQuizById(BigInteger.valueOf(1));

      assertNotNull(quiz);
      assertEquals(1, quiz.getId().intValue());
    } catch (QuizDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR, e);
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void deleteQuizTest() {
    try {
      String title = "newOlo";
      String description = "Horror quiz";
      QuizType quizType = QuizType.SCIENCE;
      Quiz quiz = QuizImpl.QuizBuilder()
          .setTitle(title)
          .setDescription(description)
          .setQuizType(quizType)
          .setCreationDate(new Date(System.currentTimeMillis()))
          .setCreatorId(BigInteger.valueOf(5))
          .build();

      Quiz newQuiz = quizDAO.createQuiz(quiz);
      assertEquals(title, newQuiz.getTitle());
      quizDAO.deleteQuiz(newQuiz);
    } catch (QuizDoesNotExistException | DAOLogicException | QuizException e) {
      log.error(MessagesForException.TEST_ERROR, e);
      fail();
    }

  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void updateQuizTest() {
    try {
      Quiz quiz = quizDAO.getQuizById(BigInteger.valueOf(1));
      Quiz updatedQuiz = quizDAO.getQuizById(quiz.getId());

      updatedQuiz.setCreationDate(new Date(System.currentTimeMillis()));

      quizDAO.updateQuiz(quiz.getId(), updatedQuiz);

      assertNotEquals(quiz.getCreationDate(), updatedQuiz.getCreationDate());
    } catch (QuizDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR, e);
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getAllQuizzesTest() {
    try {
      List<Quiz> quizList = quizDAO.getAllQuizzes();
      if (!quizList.isEmpty()) {
        assertNotNull(quizList);
      }
    } catch (QuizDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR, e);
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getQuizByTitleTest() {
    try {
      String title = "ZNO";
      Quiz quiz = quizDAO.getQuizByTitle(title);
      if (quiz != null) {
        assertEquals(title, quiz.getTitle());
      }
    } catch (QuizDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR, e);
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getQuizzesByTypeTest() {
    try {
      QuizType quizType = QuizType.MATHEMATICS;
      List<Quiz> quizzes = quizDAO.getQuizzesByType(quizType);

      if (!quizzes.isEmpty()) {
        assertEquals(quizType, quizzes.get(0).getQuizType());
      }
    } catch (QuizDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR, e);
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getLastCreatedQuizzesTest() {
    try {
      List<Quiz> quizList = quizDAO.getLastCreatedQuizzes(3);

      if (!quizList.isEmpty()) {
        assertNotNull(quizList);
      }
    } catch (DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR, e);
      fail();
    }
  }
}
