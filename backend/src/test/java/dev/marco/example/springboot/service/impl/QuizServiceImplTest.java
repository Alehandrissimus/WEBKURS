package dev.marco.example.springboot.service.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Quiz;
import dev.marco.example.springboot.service.QuizService;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static dev.marco.example.springboot.exception.MessagesForException.ERROR_WHILE_SETTING_TEST_CONNECTION;

@SpringBootTest
class QuizServiceImplTest {

    private QuizService quizService;

    @Autowired
    private void setTestConnection(QuizService quizService) {
        this.quizService = quizService;
        try {
            quizService.setTestConnection();
        } catch (DAOConfigException e) {
            log.error(ERROR_WHILE_SETTING_TEST_CONNECTION + e.getMessage());
        }
    }

    private static final Logger log = Logger.getLogger(QuizServiceImplTest.class);

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void getQuizByIdTest() {
        try {
            Quiz quiz = quizService.getQuizById(BigInteger.valueOf(1));

            assertNotNull(quiz);
            assertEquals(1, quiz.getId().intValue());

            log.info("Quiz was found by id: " + quiz.getId());

        } catch (QuizDoesNotExistException | DAOLogicException | QuizException | QuestionDoesNotExistException e) {
            log.error("Error while testing getQuizById in QuizService", e);
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void getAllQuizzesTest() {
        try {
            List<Quiz> quizList = quizService.getAllQuizzes();

            if (!quizList.isEmpty()) {
                assertNotNull(quizList);
            }

            log.info("Get all quizzes in test");
        } catch (QuizDoesNotExistException | DAOLogicException e) {
            log.error("Error while testing getAllQuizzes ", e);
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void getQuizByTitleTest() {
        try {
            String title = "ZNO";
            Quiz quiz = quizService.getQuizByTitle(title);
            log.info("Get quiz by title in test");
            if (quiz != null) {
                assertEquals(title, quiz.getTitle());
            }
        } catch (QuizDoesNotExistException | DAOLogicException | QuizException | QuestionDoesNotExistException e) {
            log.error("Error while testing getQuizByTitle ", e);
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void getLastCreatedQuizzesTest() {
        try {
            List<Quiz> quizList = quizService.getLastCreatedQuizzes(3);

            if (!quizList.isEmpty()) {
                assertNotNull(quizList);
            }

            log.info("Get getLastCreatedQuizzes in test");
        } catch (DAOLogicException | QuizDoesNotExistException e) {
            log.error("Error while testing getAllQuizzes ", e);
            fail();
        }
    }
}
