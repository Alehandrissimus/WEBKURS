package dev.marco.example.springboot.dao.impl;

import dev.marco.example.springboot.model.impl.AnswerImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import dev.marco.example.springboot.dao.QuestionDAO;
import dev.marco.example.springboot.exception.DAOConfigException;
import dev.marco.example.springboot.exception.DAOLogicException;
import dev.marco.example.springboot.exception.MessagesForException;
import dev.marco.example.springboot.exception.QuestionDoesNotExistException;
import dev.marco.example.springboot.model.Question;
import dev.marco.example.springboot.model.QuestionType;
import dev.marco.example.springboot.model.impl.QuestionImpl;
import dev.marco.example.springboot.util.DAOUtil;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Repository
public class QuestionDAOImpl implements QuestionDAO, MessagesForException {

  private Connection connection;
  private final Properties properties = new Properties();
  private static final Logger log = Logger.getLogger(QuestionDAOImpl.class);

  private final String URL;
  private final String USERNAME;
  private final String PASSWORD;

  @Autowired
  QuestionDAOImpl(
      @Value(URL_PROPERTY) String URL,
      @Value(USERNAME_PROPERTY) String USERNAME,
      @Value(PASSWORD_PROPERTY) String PASSWORD
  ) throws DAOConfigException {
    this.URL = URL;
    this.USERNAME = USERNAME;
    this.PASSWORD = PASSWORD;

    connection = DAOUtil.getDataSource(URL, USERNAME, PASSWORD, properties);
  }

  @Override
  public void setTestConnection() throws DAOConfigException {
    try {
      connection = DAOUtil.getDataSource(URL, USERNAME + TEST, PASSWORD, properties);
    } catch (DAOConfigException e) {
      log.error(String.format(TEST_CONNECTION_ERR, e.getMessage()));
      throw new DAOConfigException(TEST_CONNECTION_EXC, e);
    }
  }

  @Override
  public Question getQuestionById(BigInteger questionId, List<AnswerImpl> answers)
      throws QuestionDoesNotExistException, DAOLogicException {
    try (PreparedStatement preparedStatement =
        connection.prepareStatement(properties.getProperty(PROPERTY_GET_QUESTION_BY_ID))) {
      preparedStatement.setLong(1, questionId.intValue());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.next()) {
        log.error(QUESTION_NOT_FOUND + questionId);
        throw new QuestionDoesNotExistException(QUESTION_NOT_FOUND);
      }

      return new QuestionImpl(
          questionId,
          resultSet.getString(QUESTION_NAME_COLUMN),
          QuestionType.convertToRole(resultSet.getInt(QUESTION_TYPE_COLUMN)),
          answers
      );
    } catch (SQLException e) {
      log.error(DAO_LOGIC_EXCEPTION + questionId);
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
    }
  }

  @Override
  public Question getQuestionByData(String questionText, BigInteger quizId)
      throws DAOLogicException, QuestionDoesNotExistException {
    try (PreparedStatement preparedStatement =
        connection.prepareStatement(properties.getProperty(PROPERTY_GET_QUESTION_BY_DATA))) {

      preparedStatement.setString(1, questionText);
      preparedStatement.setLong(2, quizId.longValue());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.next()) {
        log.error(QUESTION_NOT_FOUND + questionText + quizId);
        throw new QuestionDoesNotExistException(QUESTION_NOT_FOUND);
      }

      return new QuestionImpl(
          BigInteger.valueOf(resultSet.getLong(QUESTION_ID_COLUMN)),
          resultSet.getString(QUESTION_NAME_COLUMN),
          QuestionType.convertToRole(resultSet.getInt(QUESTION_TYPE_COLUMN))
      );
    } catch (SQLException e) {
      log.error(DAO_LOGIC_EXCEPTION + questionText + quizId, e);
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
    }
  }

  @Override
  public Question createQuestion(Question question, BigInteger quizId)
      throws QuestionDoesNotExistException, DAOLogicException {
    try {
      PreparedStatement preparedStatement =
          connection.prepareStatement(properties.getProperty(PROPERTY_CREATE_QUESTION));
      preparedStatement.setString(1, question.getQuestion());
      preparedStatement.setInt(2, question.getQuestionType().ordinal());
      preparedStatement.setLong(3, quizId.longValue());
      preparedStatement.executeUpdate();

      preparedStatement.clearParameters();
      preparedStatement = connection.prepareStatement(
          properties.getProperty(PROPERTY_GET_QUESTION_ID_BY_DATA));
      preparedStatement.setString(1, question.getQuestion());
      preparedStatement.setLong(2, quizId.longValue());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.next()) {
        log.error(QUESTION_NOT_FOUND + question.getQuestion() + quizId);
        throw new QuestionDoesNotExistException(QUESTION_NOT_FOUND);
      }

      long questionId = resultSet.getLong(QUESTION_ID_COLUMN);
      question.setId(BigInteger.valueOf(questionId));

      return question;
    } catch (SQLException e) {
      log.error(DAO_LOGIC_EXCEPTION + question.getQuestion() + quizId, e);
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
    }
  }

  @Override
  public void deleteQuestion(Question question)
      throws QuestionDoesNotExistException, DAOLogicException {
    try {
      PreparedStatement preparedStatement =
          connection.prepareStatement(properties.getProperty(PROPERTY_GET_QUESTION_BY_ID));
      preparedStatement.setLong(1, question.getId().longValue());
      preparedStatement.executeQuery();
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.next()) {
        log.error(QUESTION_NOT_FOUND + question.getId());
        throw new QuestionDoesNotExistException(QUESTION_NOT_FOUND);
      }

      preparedStatement.clearParameters();
      preparedStatement = connection.prepareStatement(
          properties.getProperty(PROPERTY_DELETE_QUESTION));
      preparedStatement.setLong(1, question.getId().longValue());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      log.error(
          DAO_LOGIC_EXCEPTION + question.getId(),
          e);
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
    }
  }

  @Override
  public List<QuestionImpl> getAllQuestions(BigInteger quizId)
      throws QuestionDoesNotExistException, DAOLogicException {
    try (PreparedStatement preparedStatement =
        connection.prepareStatement(properties.getProperty(PROPERTY_GET_ALL_QUESTIONS))) {
      preparedStatement.setLong(1, quizId.longValue());
      preparedStatement.executeQuery();
      ResultSet resultSet = preparedStatement.executeQuery();

      List<QuestionImpl> questions = new ArrayList<>();
      while (resultSet.next()) {
        questions.add(new QuestionImpl(
            BigInteger.valueOf(resultSet.getLong(QUESTION_ID_COLUMN)),
            resultSet.getString(QUESTION_NAME_COLUMN),
            QuestionType.convertToRole(resultSet.getInt(QUESTION_TYPE_COLUMN))
        ));
      }

      if (questions.isEmpty()) {
        log.error(QUESTION_NOT_FOUND + quizId);
        throw new QuestionDoesNotExistException(QUESTION_NOT_FOUND);
      }

      return questions;
    } catch (SQLException e) {
      log.error(DAO_LOGIC_EXCEPTION + quizId, e);
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
    }
  }

  @Override
  public void updateQuestion(Question question) throws DAOLogicException {
    try (PreparedStatement preparedStatement =
        connection.prepareStatement(properties.getProperty(PROPERTY_UPDATE_QUESTION))) {
      preparedStatement.setString(1, question.getQuestion());
      preparedStatement.setInt(2, question.getQuestionType().ordinal());
      preparedStatement.setLong(3, question.getId().longValue());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      log.error(
          DAO_LOGIC_EXCEPTION + question.getId(),
          e);
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
    }
  }
}
