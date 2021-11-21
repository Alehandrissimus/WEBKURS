package dev.marco.example.springboot.service;

import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.UserRoles;
import dev.marco.example.springboot.model.impl.QuizAccomplishedImpl;

import java.math.BigInteger;
import java.util.Set;

public interface UserService {

  String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
          "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  void setTestConnection() throws DAOConfigException;

  BigInteger buildNewUser(String email, String password, String name, String surname)
      throws UserException, DAOLogicException;

  User authorize(User user) throws DAOLogicException, UserException, UserDoesNotExistException;

  boolean recoverPassword(User user) throws UserException, DAOLogicException, MailException;

  void validateNewUser(String email, String password, String firstName, String lastName)
      throws UserException;

  User getUserById(BigInteger id) throws UserDoesNotExistException, DAOLogicException;

  User getUserByEmail(String email) throws UserDoesNotExistException, DAOLogicException;

  void deleteUser(BigInteger id) throws DAOLogicException, UserDoesNotExistException;

  void updateUsersFullName(BigInteger id, String newFirstName, String newLastName)
          throws DAOLogicException, UserDoesNotExistException;

  void updateUsersPassword(BigInteger id, String oldPassword, String newPassword)
          throws DAOLogicException, UserDoesNotExistException, UserException;

  void updateUsersDescription(BigInteger id, String newDescription)
          throws DAOLogicException, UserDoesNotExistException;

  void updateUserRole(BigInteger id, UserRoles role) throws UserDoesNotExistException, DAOLogicException, UserException;

  User getUserByEmailCode(String code) throws UserDoesNotExistException, DAOLogicException;

  void updateUsersEmailCode(BigInteger id, String newCode) throws DAOLogicException, UserDoesNotExistException;

  boolean comparisonOfPasswords(BigInteger id, String checkPassword) throws DAOLogicException, UserDoesNotExistException;

  boolean activateUser(BigInteger id) throws DAOLogicException, UserDoesNotExistException;

  boolean disactivateUser(BigInteger id) throws DAOLogicException, UserDoesNotExistException;

  Set<QuizAccomplishedImpl> getAccomplishedQuizesByUser(BigInteger id) throws DAOLogicException, QuizDoesNotExistException, UserDoesNotExistException;

  Set<QuizAccomplishedImpl> getFavoriteQuizesByUser(BigInteger id) throws DAOLogicException, UserDoesNotExistException, QuizDoesNotExistException;

  void editAccomplishedQuiz(BigInteger idUser, QuizAccomplishedImpl newQuiz) throws DAOLogicException;

  void setIsFavoriteQuiz(BigInteger idUser,  BigInteger idQuiz, int isFavourite) throws DAOLogicException;

  void addAccomplishedQuiz(BigInteger id, QuizAccomplishedImpl quiz) throws DAOLogicException;

  QuizAccomplishedImpl getAccomplishedQuizById(BigInteger idUser, BigInteger idQuiz) throws QuizDoesNotExistException, DAOLogicException;

  boolean isAccomplishedQuiz(BigInteger idUser, BigInteger idQuiz) throws DAOLogicException;
}
