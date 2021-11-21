package dev.marco.example.springboot.service.impl;

import dev.marco.example.springboot.model.AnswerResult;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Answer;
import dev.marco.example.springboot.model.Question;
import dev.marco.example.springboot.model.QuestionType;
import dev.marco.example.springboot.model.impl.AnswerImpl;
import dev.marco.example.springboot.model.impl.QuestionImpl;
import dev.marco.example.springboot.service.QuestionService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionServiceImplTest {

    private static final Logger log = Logger.getLogger(QuestionServiceImplTest.class);

    private QuestionService questionService;

    @Autowired
    private void setTestConnection(QuestionService questionService) {
        this.questionService = questionService;
        try {
            questionService.setTestConnection();
        } catch (DAOConfigException e) {
            log.error("Error while setting test connection " + e.getMessage());
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void getQuestionByIdTest() {
        try {
            Question question = questionService.getQuestionById(BigInteger.valueOf(4));
            assertNotNull(question);
            assertNotNull(question.getAnswers());
            assertEquals(question.getQuestion(), "Uganda location?");
            ArrayList<Answer> answers = new ArrayList<>(question.getAnswers());
            assertEquals(answers.get(0).getValue(), "America");
            assertEquals(answers.get(3).getValue(), "Europe");
        } catch (DAOLogicException | QuestionDoesNotExistException | AnswerDoesNotExistException e) {
            log.error("Error while testing getQuestionByIdTest " + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void getQuestionByIdNullTest() {
        assertThrows(QuestionDoesNotExistException.class,
                () -> questionService.getQuestionById(BigInteger.valueOf(-1))
        );
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void createQuestionTest() {
        try {
            BigInteger quizId = BigInteger.valueOf(2);
            String questionText = "" + new Random().nextInt(500000);
            Question questionModel = new QuestionImpl(
                    questionText,
                    QuestionType.TRUE_FALSE
            );

            List<AnswerImpl> answers = new ArrayList<>();
            answers.add(new AnswerImpl("America", AnswerResult.FALSE, BigInteger.valueOf(52)));
            answers.add(new AnswerImpl("Asia", AnswerResult.FALSE, BigInteger.valueOf(52)));
            answers.add(new AnswerImpl("Africa", AnswerResult.FALSE, BigInteger.valueOf(52)));
            answers.add(new AnswerImpl("Europe", AnswerResult.TRUE, BigInteger.valueOf(52)));
            questionModel.setAnswers(answers);

            Question questionGot = questionService.createQuestion(questionModel, quizId);
            Question questionFound = questionService.getQuestionByData(questionText, quizId);
            assertEquals(questionText, questionGot.getQuestion());
            assertEquals(questionText, questionFound.getQuestion());
            log.debug("QuestionServiceTest createQuestion id = " + questionFound.getId());
            questionService.deleteQuestion(questionFound);
        } catch (DAOLogicException | QuestionDoesNotExistException | QuestionException | AnswerDoesNotExistException e) {
            log.error("Error while testing createQuestionTest in QuestionServiceTest " + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void createQuestionThrowsTest() {
        BigInteger quizId = BigInteger.valueOf(2);
        String questionText = "";
        Question questionModel = new QuestionImpl(
                questionText,
                QuestionType.TRUE_FALSE
        );

        assertThrows(QuestionException.class,
                () -> questionService.createQuestion(questionModel, quizId)
        );
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void updateQuestionTest() {
        try {
            String questionText = "q" + new Random().nextInt(500000);
            Question questionModel = new QuestionImpl(
                    BigInteger.valueOf(5),
                    questionText,
                    QuestionType.TRUE_FALSE
            );

            questionService.updateQuestion(questionModel);
            Question question = questionService.getQuestionById(BigInteger.valueOf(5));
            assertEquals(questionModel.getQuestion(), question.getQuestion());
        } catch (DAOLogicException | QuestionDoesNotExistException | AnswerDoesNotExistException | QuestionException e) {
            log.error("Error while testing updateQuestionTest in QuestionServiceTest " + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void deleteQuestionTest() {
        try {
            Question questionModel = new QuestionImpl(
                    "Test location?",
                    QuestionType.FOUR_ANSWERS
            );
            List<AnswerImpl> answers = new ArrayList<>();
            answers.add(new AnswerImpl("America", AnswerResult.FALSE, BigInteger.valueOf(52)));
            answers.add(new AnswerImpl("Asia", AnswerResult.FALSE, BigInteger.valueOf(52)));
            answers.add(new AnswerImpl("Africa", AnswerResult.FALSE, BigInteger.valueOf(52)));
            answers.add(new AnswerImpl("Europe", AnswerResult.TRUE, BigInteger.valueOf(52)));
            questionModel.setAnswers(answers);

            Question createdQuestion = questionService.createQuestion(questionModel, BigInteger.valueOf(5));
            assertNotNull(questionService.getQuestionById(createdQuestion.getId()));

            log.debug("QuestionServiceTest deleteQuestionTest: Created question id = " + createdQuestion.getId());

            questionService.deleteQuestion(createdQuestion);
            assertThrows(QuestionDoesNotExistException.class,
                    () -> questionService.getQuestionById(createdQuestion.getId())
            );
        } catch (DAOLogicException | QuestionDoesNotExistException | AnswerDoesNotExistException | QuestionException e) {
            log.error("Error while testing deleteQuestionTest in QuestionServiceTest " + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void getQuestionByDataTest() {
        try {
            BigInteger quizId = BigInteger.valueOf(5);
            Question question = questionService.getQuestionByData("Rome location?", quizId);
            assertNotNull(question);
            assertEquals(question.getQuestion(), "Rome location?");
            assertEquals(question.getId(), BigInteger.valueOf(50));
        } catch (DAOLogicException | AnswerDoesNotExistException | QuestionDoesNotExistException e) {
            log.error("Error while testing getQuestionByDataTest in QuestionServiceTest " + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void getQuestionsByQuizTest() {
        try {
            BigInteger quizId = BigInteger.valueOf(4);
            List<QuestionImpl> questions = questionService.getQuestionsByQuiz(quizId);
            assertEquals(questions.size(), 10);
            assertNotNull(questions.get(3).getAnswers());
        } catch (DAOLogicException | AnswerDoesNotExistException | QuestionDoesNotExistException e) {
            log.error("Error while testing getQuestionsByQuizTest in QuestionServiceTest " + e.getMessage());
            fail();
        }
    }
}
