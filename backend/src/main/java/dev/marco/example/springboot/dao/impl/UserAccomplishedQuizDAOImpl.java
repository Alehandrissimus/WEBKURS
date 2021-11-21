package dev.marco.example.springboot.dao.impl;

import java.util.stream.Collectors;

import dev.marco.example.springboot.model.QuizType;
import dev.marco.example.springboot.model.impl.QuizImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import dev.marco.example.springboot.dao.UserAccomplishedQuizDAO;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.impl.QuizAccomplishedImpl;
import dev.marco.example.springboot.util.DAOUtil;

import java.math.BigInteger;
import java.sql.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static dev.marco.example.springboot.exception.MessagesForException.*;

@Repository
public class UserAccomplishedQuizDAOImpl implements UserAccomplishedQuizDAO {

  private Connection connection;
  private final Properties properties = new Properties();
  private static final Logger log = Logger.getLogger(UserAccomplishedQuizDAOImpl.class);

  private final String URL;
  private final String USERNAME;
  private final String PASSWORD;

  @Autowired
  UserAccomplishedQuizDAOImpl(
      @Value(URL_PROPERTY) String URL,
      @Value(USERNAME_PROPERTY) String USERNAME,
      @Value(PASSWORD_PROPERTY) String PASSWORD
  ) throws DAOConfigException {
    this.URL = URL;
    this.USERNAME = USERNAME;
    this.PASSWORD = PASSWORD;

    connection = DAOUtil.getDataSource(URL, USERNAME, PASSWORD, properties);
  }

  public void setTestConnection()
      throws DAOConfigException {
    try {
      connection = DAOUtil.getDataSource(URL, USERNAME + TEST, PASSWORD, properties);
    } catch (DAOConfigException e) {
      log.error(TEST_CONNECTION_ERR + e.getMessage());
      throw new DAOConfigException(TEST_CONNECTION_ERR, e);
    }
  }

  @Override
  public Set<QuizAccomplishedImpl> getAccomplishedQuizesByUser(BigInteger idUser)
      throws DAOLogicException, QuizDoesNotExistException {
    try (PreparedStatement statement = connection
        .prepareStatement(properties.getProperty(SEARCH_ACCOMPLISHED_QUIZES_BY_USER_ID))) {
      statement.setLong(1, idUser.longValue());
      ResultSet resultSet = statement.executeQuery();
      Set<QuizAccomplishedImpl> quizzes = new HashSet<>();
      if (!resultSet.isBeforeFirst()) {
        return quizzes;
      }
      while (resultSet.next()) {
        QuizAccomplishedImpl quiz = new QuizAccomplishedImpl(
            resultSet.getInt(CORRECT_ANSWERS),
            resultSet.getInt(IS_FAVOURITE) == TRUE_SQL,
            resultSet.getDate(DATE_CREATE),
            QuizImpl.QuizBuilder()
                .setId(BigInteger.valueOf(resultSet.getLong(ID_QUIZ)))
                .setTitle(resultSet.getString(TITLE))
                .setDescription(resultSet.getString(DESCRIPTION))
                .setCreationDate(resultSet.getDate(CREATION_DATE))
                .setQuizType(QuizType.values()[resultSet.getInt(QUIZ_TYPE)])
                .setCreatorId(BigInteger.valueOf(resultSet.getLong(CREATOR)))
                .build());
        quizzes.add(quiz);
      }
      return quizzes;
    } catch (SQLException | QuizException e) {
      log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
    }
  }

  @Override
  public Set<QuizAccomplishedImpl> getFavoriteQuizesByUser(BigInteger id) throws DAOLogicException {
    try {
      return getAccomplishedQuizesByUser(id)
          .stream()
          .filter(QuizAccomplishedImpl::getFavourite)
          .collect(Collectors.toSet());
    } catch (Exception e) {
      log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
    }

  }

  @Override
  public void addAccomplishedQuiz(BigInteger idUser, QuizAccomplishedImpl quiz)
      throws DAOLogicException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
        properties.getProperty(ADD_ACCOMPLISHED_QUIZ))) {
      if (isAccomplishedQuiz(idUser, quiz.getQuiz().getId())) {
        return;
      }
      preparedStatement.setLong(1, idUser.longValue());
      preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
      preparedStatement.setLong(3, quiz.getQuiz().getId().longValue());
      preparedStatement.setInt(4, quiz.getCorrectAnswers());
      preparedStatement.setInt(5, quiz.getIntFavourite());
      preparedStatement.executeUpdate();
    } catch (SQLException throwables) {
      log.error(DAO_LOGIC_EXCEPTION + throwables.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, throwables);
    }
  }

  @Override
  public void editAccomplishedQuiz(BigInteger idUser, QuizAccomplishedImpl newQuiz)
      throws DAOLogicException {
    try (PreparedStatement preparedStatement = connection
        .prepareStatement(properties.getProperty(UPDATE_ACCOMPLISHED_QUIZ))) {
      preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
      preparedStatement.setInt(2, newQuiz.getCorrectAnswers());
      preparedStatement.setLong(3, idUser.longValue());
      preparedStatement.setLong(4, newQuiz.getQuiz().getId().longValue());
      preparedStatement.executeUpdate();
    } catch (SQLException throwables) {
      log.error(DAO_LOGIC_EXCEPTION + throwables.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, throwables);
    }
  }

  @Override
  public void setIsFavoriteQuiz(BigInteger idUser, BigInteger idQuiz, int isFavourite)
      throws DAOLogicException {

    try (PreparedStatement preparedStatement = connection.prepareStatement(
        properties.getProperty(SET_IS_FAVOURITE))) {
      preparedStatement.setInt(1, isFavourite);
      preparedStatement.setLong(2, idUser.longValue());
      preparedStatement.setLong(3, idQuiz.longValue());
      preparedStatement.executeUpdate();
    } catch (SQLException throwables) {
      log.error(DAO_LOGIC_EXCEPTION + throwables.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, throwables);
    }
  }

  @Override
  public QuizAccomplishedImpl getAccomplishedQuizById(BigInteger idUser, BigInteger idQuiz)
      throws QuizDoesNotExistException, DAOLogicException {
    try (PreparedStatement preparedStatement = connection
        .prepareStatement(properties.getProperty(GET_ACCOMPLISHED_QUIZ))) {
      preparedStatement.setLong(1, idUser.longValue());
      preparedStatement.setLong(2, idQuiz.longValue());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.isBeforeFirst()) {
        log.error(ACCOMPLISHED_QUIZ_HAS_NOT_BEEN_FOUNDED + MESSAGE_FOR_GET_ACCOMPLISHED_QUIZ_BY_ID);
        throw new QuizDoesNotExistException(ACCOMPLISHED_QUIZ_HAS_NOT_BEEN_FOUNDED);
      }
      resultSet.next();
      return new QuizAccomplishedImpl(
          resultSet.getInt(CORRECT_ANSWERS),
          resultSet.getInt(IS_FAVOURITE) == TRUE_SQL,
          resultSet.getDate(DATE_CREATE),
          QuizImpl.QuizBuilder()
              .setId(BigInteger.valueOf(resultSet.getLong(ID_QUIZ)))
              .setTitle(resultSet.getString(TITLE))
              .setDescription(resultSet.getString(DESCRIPTION))
              .setCreationDate(resultSet.getDate(CREATION_DATE))
              .setQuizType(QuizType.values()[resultSet.getInt(QUIZ_TYPE)])
              .setCreatorId(BigInteger.valueOf(resultSet.getLong(CREATOR)))
              .build());
    } catch (SQLException | QuizException throwables) {
      log.error(DAO_LOGIC_EXCEPTION + throwables.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, throwables);
    }
  }

  @Override
  public boolean isAccomplishedQuiz(BigInteger idUser, BigInteger idQuiz)
      throws DAOLogicException {
    try (PreparedStatement preparedStatement = connection
        .prepareStatement(properties.getProperty(GET_ACCOMPLISHED_QUIZ))) {
      preparedStatement.setLong(1, idUser.longValue());
      preparedStatement.setLong(2, idQuiz.longValue());
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.isBeforeFirst()) {
        return true;
      }
    } catch (SQLException throwables) {
      log.error(DAO_LOGIC_EXCEPTION + throwables.getMessage());
      throw new DAOLogicException(DAO_LOGIC_EXCEPTION, throwables);
    }
    return false;
  }
}
