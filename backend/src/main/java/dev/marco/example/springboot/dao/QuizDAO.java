package dev.marco.example.springboot.dao;

import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Quiz;
import dev.marco.example.springboot.model.QuizType;
import dev.marco.example.springboot.model.impl.QuizImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;

public interface QuizDAO {

    String URL_PROPERTY = "${spring.datasource.url}";
    String USERNAME_PROPERTY = "${spring.datasource.username}";
    String PASSWORD_PROPERTY = "${spring.datasource.password}";

    String ID_QUIZ = "ID_QUIZ";
    String TITLE = "TITLE";
    String DESCRIPTION = "DESCRIPTION";
    String CREATION_DATE = "CREATION_DATE";
    String QUIZ_TYPE = "QUIZ_TYPE";
    String CREATOR = "CREATOR";

    String INSERT_INTO_QUIZ = "INSERT_INTO_QUIZ";
    String GET_QUIZ_ID_BY_DATA = "GET_QUIZ_ID_BY_DATA";
    String UPDATE_QUIZ = "UPDATE_QUIZ";
    String DELETE_QUIZ = "DELETE_QUIZ";
    String SELECT_QUIZ_BY_ID = "SELECT_QUIZ_BY_ID";
    String SELECT_ALL_QUIZZES = "SELECT_ALL_QUIZZES";
    String SELECT_QUIZ_BY_TITLE = "SELECT_QUIZ_BY_TITLE";
    String SELECT_QUIZZES_BY_TYPE = "SELECT_QUIZZES_BY_TYPE";
    String SELECT_LAST_CREATED_QUIZZES = "SELECT_LAST_CREATED_QUIZZES";
    String SELECT_COUNT_OF_QUIZZES = "SELECT_COUNT_OF_QUIZZES";
    String SELECT_COUNT_OF_QUIZZES_BY_TITLE = "SELECT_COUNT_OF_QUIZZES_BY_TITLE";
    String SELECT_QUIZZES_BY_ROWS = "SELECT_QUIZZES_BY_ROWS";
    String SELECT_QUIZZES_LIKE_TITLE = "SELECT_QUIZZES_LIKE_TITLE";
    String TEST = "_TEST";

    Quiz createQuiz(Quiz quiz) throws DAOLogicException, UserDoesNotExistException;

    void updateQuiz(BigInteger id, Quiz quiz) throws QuizDoesNotExistException, DAOLogicException;

    void deleteQuiz(Quiz quiz) throws QuizDoesNotExistException, DAOLogicException;

    QuizImpl getQuizById(BigInteger id) throws QuizDoesNotExistException, DAOLogicException;

    boolean existQuizByTitle(String title) throws DAOLogicException;

    List<Quiz> getQuizzesByType(QuizType quizType) throws QuizDoesNotExistException, DAOLogicException;

    List<Quiz> getAllQuizzes() throws QuizDoesNotExistException, DAOLogicException;

    List<Quiz> getLastCreatedQuizzes(int count) throws DAOLogicException, QuizDoesNotExistException;

    Quiz getQuizByTitle(String title) throws QuizDoesNotExistException, DAOLogicException;

    void setTestConnection() throws DAOConfigException;

    int countOfQuizzesLikeTitle(String title) throws QuizException;

    Page<Quiz> getQuizzesLikeTitle(Pageable pageable, String title) throws QuizException;

    int countOfQuizzes() throws QuizException;

    Page<Quiz> getQuizzesByPage(Pageable pageable) throws QuizException;

}
