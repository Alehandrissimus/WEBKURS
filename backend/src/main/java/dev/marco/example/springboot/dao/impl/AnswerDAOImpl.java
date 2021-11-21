package dev.marco.example.springboot.dao.impl;

import dev.marco.example.springboot.model.AnswerResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import dev.marco.example.springboot.dao.AnswerDAO;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Answer;
import dev.marco.example.springboot.model.impl.AnswerImpl;
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
public class AnswerDAOImpl implements AnswerDAO, MessagesForException {

    private Connection connection;
    private final Properties properties = new Properties();
    private static final Logger log = Logger.getLogger(AnswerDAOImpl.class);
    private final int SQL_TRUE = 1;
    private final int SQL_FALSE = 0;

    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;

    @Autowired
    AnswerDAOImpl(
            @Value(URL_PROPERTY) String URL,
            @Value(USER_PROPERTY) String USERNAME,
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
    public AnswerImpl getAnswerById(BigInteger answerId) throws DAOLogicException, AnswerDoesNotExistException {
        try  {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(properties.getProperty(GET_ANSWER_BY_ID));
            preparedStatement.setLong(1, answerId.longValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                log.error(String.format(getAnswerByIdNotFoundErr, answerId));
                throw new AnswerDoesNotExistException(String.format(getAnswerByIdNotFoundExc, answerId));
            }
            return new AnswerImpl(
                    answerId,
                    resultSet.getString(SQL_ANSWER_TEXT),
                    AnswerResult.convertBooleanToAnswer(resultSet.getInt(SQL_ANSWER_IS_TRUE) == SQL_TRUE),
                    BigInteger.valueOf(resultSet.getLong(SQL_ANSWER_QUESTION)));
        } catch (SQLException throwable) {
            log.error(getAnswerByIdLogicErr, throwable);
            throw new DAOLogicException(String.format(getAnswerByIdLogicExc ,answerId), throwable);
        }

    }

    @Override
    public BigInteger getLastAnswerIdByTitle(String title) throws DAOLogicException, AnswerDoesNotExistException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(GET_LAST_ANSWER_ID_BY_TITLE));
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                log.error(String.format(getLastAnswerIdByTitleNotFoundErr, title));
                throw new AnswerDoesNotExistException(String.format(getLastAnswerIdByTitleNotFoundExc, title));
            }
            return BigInteger.valueOf(resultSet.getLong(SQL_MAX_ID_ANSWER));
        } catch (SQLException throwable) {
            log.error(getLastAnswerIdByTitleLogicErr, throwable);
            throw new DAOLogicException(String.format(getLastAnswerIdByTitleLogicExc, title), throwable);
        }
    }

    @Override
    public BigInteger createAnswer(Answer answer) throws DAOLogicException, AnswerDoesNotExistException {
        try {
            String title = answer.getValue();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(CREATE_ANSWER));
            preparedStatement.setString(1, title);
            if (answer.getAnswer().equals(AnswerResult.TRUE))
                preparedStatement.setInt(2, SQL_TRUE);
            else if (answer.getAnswer().equals(AnswerResult.FALSE))
                preparedStatement.setInt(2, SQL_FALSE);
            preparedStatement.setLong(3, answer.getQuestionId().longValue());
            preparedStatement.executeUpdate();
            return getLastAnswerIdByTitle(title);
        } catch (SQLException throwable) {
            log.error(createAnswerLogicExc, throwable);
            throw new DAOLogicException(createAnswerLogicExc, throwable);
        }
    }

    @Override
    public void deleteAnswer(BigInteger id) throws DAOLogicException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(DELETE_ANSWER));
            preparedStatement.setLong(1, id.longValue());
            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            log.error(deleteAnswerLogicExc, throwable);
            throw new DAOLogicException(deleteAnswerLogicExc, throwable);
        }
    }

    @Override
    public BigInteger updateAnswer(Answer answer) throws DAOLogicException {
        try {
            BigInteger id = answer.getId();
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(UPDATE_ANSWER));
            preparedStatement.setString(1, answer.getValue());
            if (answer.getAnswer().equals(AnswerResult.TRUE))
                preparedStatement.setInt(2, SQL_TRUE);
            else if (answer.getAnswer().equals(AnswerResult.FALSE))
                preparedStatement.setInt(2, SQL_FALSE);
            preparedStatement.setLong(3, answer.getQuestionId().longValue());
            preparedStatement.setLong(4, id.longValue());
            preparedStatement.executeUpdate();
            return id;
        } catch (SQLException throwable) {
            log.error(updateAnswerLogicExc, throwable);
            throw new DAOLogicException(updateAnswerLogicExc, throwable);
        }
    }

    @Override
    public List<AnswerImpl> getAnswersByQuestionId(BigInteger questionId) throws DAOLogicException, AnswerDoesNotExistException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(GET_ANSWERS_BY_QUESTION_ID));
            preparedStatement.setLong(1, questionId.longValue());
            ResultSet resultSet = preparedStatement.executeQuery();

            List<AnswerImpl> answers = new ArrayList<>();
            while (resultSet.next()) {
                answers.add(new AnswerImpl(
                        BigInteger.valueOf(resultSet.getLong(SQL_ANSWER_ID)),
                        resultSet.getString(SQL_ANSWER_TEXT),
                        AnswerResult.convertBooleanToAnswer(resultSet.getInt(SQL_ANSWER_IS_TRUE) == SQL_TRUE),
                        questionId));
            }
            return answers;
        } catch (SQLException throwable) {
            log.error(getAnswersByQuestionIdLogicErr, throwable);
            throw new DAOLogicException(String.format(getAnswersByQuestionIdLogicExc, questionId), throwable);
        }
    }

}
