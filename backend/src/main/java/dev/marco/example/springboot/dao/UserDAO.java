package dev.marco.example.springboot.dao;

import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.User;

import dev.marco.example.springboot.model.UserRoles;
import java.math.BigInteger;

public interface UserDAO {

  String URL_PROPERTY = "${spring.datasource.url}";
  String USERNAME_PROPERTY = "${spring.datasource.username}";
  String PASSWORD_PROPERTY = "${spring.datasource.password}";
  String SEARCH_USER_BY_ID = "SEARCH_USER_BY_ID";
  String USER_FIRST_NAME = "USER_FIRST_NAME";
  String USER_LAST_NAME = "USER_LAST_NAME";
  String USER_EMAIL = "USER_EMAIL";
  String USER_ROLE = "USER_ROLE";
  String USER_ACTIVE = "USER_ACTIVE";

  String USER_DESCRIPTION = "USER_DESCRIPTION";
  String USER_ID = "USER_ID";
  String DELETE_USER_BY_ID = "DELETE_USER_BY_ID";
  String CREATE_USER = "CREATE_USER";
  String UPDATE_USER_NAME = "UPDATE_USER_NAME";
  String UPDATE_USER_PASSWORD = "UPDATE_USER_PASSWORD";
  String SEARCH_USER_AUTHORIZE = "SEARCH_USER_AUTHORIZE";
  String SEARCH_PASSWORD_BY_EMAIL = "SEARCH_PASSWORD_BY_EMAIL";
  String UPDATE_USER_DESCRIPTION = "UPDATE_USER_DESCRIPTION";
  String UPDATE_USER_EMAIL_CODE = "UPDATE_USER_EMAIL_CODE";
  String SEARCH_USER_BY_EMAIL_CODE = "SEARCH_USER_BY_EMAIL_CODE";
  String UPDATE_USER_ACTIVE = "UPDATE_USER_ACTIVE";
  String UPDATE_USER_DISACTIVE = "UPDATE_USER_DISACTIVE";
  String UPDATE_USER_ROLE = "UPDATE_USER_ROLE";
  String SEARCH_USER_BY_EMAIL = "SEARCH_USER_BY_EMAIL";
  String CHECK_USER_PASSWORD = "CHECK_USER_PASSWORD";
  String USERS_PASSWORD = "PASSWD";

  String TEST_CONNECTION = "_TEST";

  void setTestConnection() throws DAOConfigException;

  User getUserById(BigInteger id) throws UserDoesNotExistException, DAOLogicException;

  User getUserByEmail(String email) throws UserDoesNotExistException, DAOLogicException;

  void deleteUser(BigInteger id) throws DAOLogicException;

  BigInteger createUser(User user) throws DAOLogicException;

  void updateUsersFullName(BigInteger id, String newFirstName, String newLastName)
      throws DAOLogicException, UserDoesNotExistException;

  void updateUsersPassword(BigInteger id, String newPassword)
      throws DAOLogicException, UserDoesNotExistException;

  User getAuthorizeUser(String email, String password)
          throws UserDoesNotExistException, UserDoesNotConfirmedEmailException, DAOLogicException, UserException;

  void updateUsersDescription(BigInteger id, String newDescription)
      throws DAOLogicException, UserDoesNotExistException;

  User getUserByEmailCode(String code) throws UserDoesNotExistException, DAOLogicException;

  String getUserPasswordByEmail(String email) throws UserDoesNotExistException, DAOLogicException;

  void updateUsersEmailCode(BigInteger id, String newCode)
      throws DAOLogicException, UserDoesNotExistException;

  boolean comparisonOfPasswords(BigInteger id, String checkPassword) throws DAOLogicException;

  boolean activateUser(BigInteger id) throws DAOLogicException, UserDoesNotExistException;

  boolean disactivateUser(BigInteger id) throws DAOLogicException, UserDoesNotExistException;

  void updateUserRole(BigInteger id, UserRoles role)
      throws UserDoesNotExistException, DAOLogicException;

}
