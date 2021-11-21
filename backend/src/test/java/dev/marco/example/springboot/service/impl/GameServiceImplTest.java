package dev.marco.example.springboot.service.impl;

import dev.marco.example.springboot.dao.AnswerDAO;
import dev.marco.example.springboot.dao.QuizDAO;
import dev.marco.example.springboot.dao.UserAccomplishedQuizDAO;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Answer;
import dev.marco.example.springboot.model.Quiz;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.impl.AnswerImpl;
import dev.marco.example.springboot.model.impl.QuestionImpl;
import dev.marco.example.springboot.model.impl.QuizAccomplishedImpl;
import dev.marco.example.springboot.service.UserService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.service.GameService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameServiceImplTest {
    private static final Logger log = Logger.getLogger(GameServiceImplTest.class);

    private GameService gameService;
    private UserService userService;
    private UserAccomplishedQuizDAO userAccomplishedQuizDAO;
    private QuizDAO quizDAO;
    private AnswerDAO answerDAO;

    @Autowired
    private void setTestConnection(GameService gameService, UserService userService,
                                   UserAccomplishedQuizDAO userAccomplishedQuizDAO, QuizDAO quizDAO, AnswerDAO answerDAO) {
        this.gameService = gameService;
        this.userService = userService;
        this.userAccomplishedQuizDAO = userAccomplishedQuizDAO;
        this.quizDAO = quizDAO;
        this.answerDAO = answerDAO;
        try {
            gameService.setTestConnection();
            userService.setTestConnection();
            userAccomplishedQuizDAO.setTestConnection();
        } catch (DAOConfigException e) {
            log.error("Error while setting test connection " + e.getMessage());
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void sendGameQuizTest() {
        try {
            String quizTitle = "ZNO";
            Quiz quiz = gameService.sendGameQuiz(quizTitle);
            assertNotNull(quiz);
            assertEquals("Try this!", quiz.getDescription());
            assertEquals(BigInteger.TWO, quiz.getCreatorId());

            List<QuestionImpl> questions = quiz.getQuestions();

            assertNotNull(questions);
            assertEquals("Ukraine location?", questions.get(0).getQuestion());

        } catch (DAOLogicException | QuizDoesNotExistException | QuizException
                | QuestionDoesNotExistException | AnswerDoesNotExistException e) {
            log.error("Error while testing sendGameQuiz in GameService " + e.getMessage());
            fail();
        }

    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void validateAnswersTest() {
        try {
            BigInteger userId = BigInteger.valueOf(5);
            User user = userService.getUserById(userId);
            Quiz quiz = quizDAO.getQuizById(BigInteger.valueOf(3));

            QuizAccomplishedImpl userAccomplishedQuiz = userAccomplishedQuizDAO.getAccomplishedQuizById(userId, quiz.getId());

            assertNotNull(userAccomplishedQuiz);
            assertEquals(1, userAccomplishedQuiz.getCorrectAnswers());

            List<AnswerImpl> userAnswers = new ArrayList<>();
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(84)));
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(88)));
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(90)));
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(95)));
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(100)));
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(103)));
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(108)));
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(112)));
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(113)));
            userAnswers.add(answerDAO.getAnswerById(BigInteger.valueOf(120)));

            List<QuestionImpl> resultList = gameService.validateAnswers(quiz, user, userAnswers);
            System.out.println(resultList);
            assertNotNull(resultList);

            QuizAccomplishedImpl newUserAccomplishedQuiz = userAccomplishedQuizDAO.getAccomplishedQuizById(userId, quiz.getId());

            assertNotNull(newUserAccomplishedQuiz);
            assertEquals(10, newUserAccomplishedQuiz.getCorrectAnswers());

            newUserAccomplishedQuiz.setCorrectAnswers(1);
            userAccomplishedQuizDAO.editAccomplishedQuiz(userId, newUserAccomplishedQuiz);

            QuizAccomplishedImpl finalUserAccomplishedQuiz = userAccomplishedQuizDAO.getAccomplishedQuizById(userId, quiz.getId());

            assertNotNull(finalUserAccomplishedQuiz);
            assertEquals(1, finalUserAccomplishedQuiz.getCorrectAnswers());

        } catch (UserDoesNotExistException | DAOLogicException | QuizDoesNotExistException | AnswerDoesNotExistException | QuestionDoesNotExistException | QuizException e) {
            log.error("Error while testing validateAnswers in GameService " + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void setIsFavoriteTest() {
        try {
            User user = userService.getUserById(BigInteger.valueOf(5));
            assertNotNull(user);

            QuizAccomplishedImpl quizAccomplished =
                    userAccomplishedQuizDAO.getAccomplishedQuizById(user.getId(), BigInteger.valueOf(3));
            assertNotNull(quizAccomplished);

            quizAccomplished.setFavourite(false);
            gameService.setIsFavorite(user, quizAccomplished);
            assertEquals(false, quizAccomplished.getFavourite());

            quizAccomplished.setFavourite(true);
            gameService.setIsFavorite(user, quizAccomplished);
            assertEquals(true, quizAccomplished.getFavourite());

            quizAccomplished.setFavourite(false);
            gameService.setIsFavorite(user, quizAccomplished);
            assertEquals(false, quizAccomplished.getFavourite());
        } catch (UserDoesNotExistException | DAOLogicException | QuizDoesNotExistException e) {
            log.error("Error while testing setIsFavorite in GameService " + e.getMessage());
            fail();
        }
    }

}
