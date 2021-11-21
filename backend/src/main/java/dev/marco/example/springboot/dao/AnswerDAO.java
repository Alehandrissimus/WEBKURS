package dev.marco.example.springboot.dao;

import dev.marco.example.springboot.exception.AnswerDoesNotExistException;
import dev.marco.example.springboot.exception.DAOConfigException;
import dev.marco.example.springboot.exception.DAOLogicException;
import dev.marco.example.springboot.model.Answer;
import dev.marco.example.springboot.model.impl.AnswerImpl;

import java.math.BigInteger;
import java.util.List;

public interface AnswerDAO {

  String URL_PROPERTY = "${spring.datasource.url}";
  String USER_PROPERTY = "${spring.datasource.username}";
  String PASSWORD_PROPERTY = "${spring.datasource.password}";

  String SQL_ANSWER_ID = "id_answer";
  String SQL_MAX_ID_ANSWER = "MAX(ID_ANSWER)";
  String SQL_ANSWER_TEXT = "text";
  String SQL_ANSWER_IS_TRUE = "is_true";
  String SQL_ANSWER_QUESTION = "question";

  String GET_ANSWER_BY_ID = "GET_ANSWER_BY_ID";
  String GET_LAST_ANSWER_ID_BY_TITLE = "GET_LAST_ANSWER_ID_BY_TITLE";
  String CREATE_ANSWER = "CREATE_ANSWER";
  String DELETE_ANSWER = "DELETE_ANSWER";
  String UPDATE_ANSWER = "UPDATE_ANSWER";
  String GET_ANSWERS_BY_QUESTION_ID = "GET_ANSWERS_BY_QUESTION_ID";
  String TEST = "_TEST";

  void setTestConnection() throws DAOConfigException;

  AnswerImpl getAnswerById(BigInteger id) throws DAOLogicException, AnswerDoesNotExistException;

  BigInteger getLastAnswerIdByTitle(String title)
      throws DAOLogicException, AnswerDoesNotExistException;

  BigInteger createAnswer(Answer answer) throws DAOLogicException, AnswerDoesNotExistException;

  void deleteAnswer(BigInteger id) throws DAOLogicException;

  BigInteger updateAnswer(Answer answer) throws DAOLogicException;

  List<AnswerImpl> getAnswersByQuestionId(BigInteger questionId)
      throws DAOLogicException, AnswerDoesNotExistException;

}
