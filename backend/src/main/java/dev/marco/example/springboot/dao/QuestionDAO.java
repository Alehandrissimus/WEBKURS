package dev.marco.example.springboot.dao;

import dev.marco.example.springboot.exception.DAOConfigException;
import dev.marco.example.springboot.exception.DAOLogicException;
import dev.marco.example.springboot.exception.QuestionDoesNotExistException;
import dev.marco.example.springboot.model.Question;
import dev.marco.example.springboot.model.impl.AnswerImpl;
import dev.marco.example.springboot.model.impl.QuestionImpl;

import java.math.BigInteger;
import java.util.List;

public interface QuestionDAO {

  String URL_PROPERTY = "${spring.datasource.url}";
  String USERNAME_PROPERTY = "${spring.datasource.username}";
  String PASSWORD_PROPERTY = "${spring.datasource.password}";

  String QUESTION_ID_COLUMN = "id_question";
  String QUESTION_NAME_COLUMN = "question_name";
  String QUESTION_TYPE_COLUMN = "question_type";

  String PROPERTY_GET_QUESTION_BY_ID = "GET_QUESTION_BY_ID";
  String PROPERTY_GET_QUESTION_BY_DATA = "GET_QUESTION_BY_DATA";
  String PROPERTY_CREATE_QUESTION = "CREATE_QUESTION";
  String PROPERTY_GET_QUESTION_ID_BY_DATA = "GET_QUESTION_ID_BY_DATA";
  String PROPERTY_DELETE_QUESTION = "DELETE_QUESTION";
  String PROPERTY_GET_ALL_QUESTIONS = "GET_ALL_QUESTIONS";
  String PROPERTY_UPDATE_QUESTION = "UPDATE_QUESTION";
  String TEST = "_TEST";

  void setTestConnection() throws DAOConfigException;

  Question getQuestionById(BigInteger id, List<AnswerImpl> answers)
      throws QuestionDoesNotExistException, DAOLogicException;

  Question getQuestionByData(String questionText, BigInteger quizId)
      throws QuestionDoesNotExistException, DAOLogicException;

  Question createQuestion(Question question, BigInteger id)
      throws QuestionDoesNotExistException, DAOLogicException;

  void deleteQuestion(Question question) throws QuestionDoesNotExistException, DAOLogicException;

  List<QuestionImpl> getAllQuestions(BigInteger id)
      throws QuestionDoesNotExistException, DAOLogicException;

  void updateQuestion(Question question) throws DAOLogicException;

}
