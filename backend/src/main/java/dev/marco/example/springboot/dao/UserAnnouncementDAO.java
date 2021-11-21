package dev.marco.example.springboot.dao;

import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.User;
import java.math.BigInteger;
import java.util.Set;

public interface UserAnnouncementDAO {

  String SELECT_USERS_LIKED_ANNOUNCEMENT = "SELECT_USERS_LIKED_ANNOUNCEMENT";
  String SELECT_ANNOUNCEMENT_LIKED_BY_USER = "SELECT_ANNOUNCEMENT_LIKED_BY_USER";
  String GET_PARTICIPANT_BY_ID = "GET_PARTICIPANT_BY_ID";
  String ADD_PARTICIPANT = "ADD_PARTICIPANT";
  String DELETE_PARTICIPANT = "DELETE_PARTICIPANT";

  String USER_ID = "USER_ID";
  String USER_FIRST_NAME = "USER_FIRST_NAME";
  String USER_LAST_NAME = "USER_LAST_NAME";
  String USER_EMAIL = "USER_EMAIL";
  String USER_PASSWORD = "USER_PASSWORD";
  String USER_ROLE = "USER_ROLE";
  String USER_ACTIVE = "USER_ACTIVE";
  String USER_EMAIL_CODE = "USER_EMAIL_CODE";
  String USER_DESCRIPTION = "USER_DESCRIPTION";
  String ERROR_TEST_CONNECTION = "Error while setting test connection ";
  String TEST = "_TEST";

  void setTestConnection() throws DAOConfigException;

  Set<Announcement> getAnnouncementsLikedByUser(BigInteger idUser)
      throws AnnouncementDoesNotExistException, DAOLogicException, AnnouncementException;

  Set<User> getUsersLikedAnnouncement(BigInteger idAnnouncement)
      throws UserDoesNotExistException, DAOLogicException;

  boolean isParticipant(BigInteger idAnnouncement, BigInteger idUser) throws DAOLogicException;

  void addParticipant(BigInteger idAnnouncement, BigInteger idUser) throws DAOLogicException;

  void deleteParticipant(BigInteger idAnnouncement, BigInteger idUser) throws DAOLogicException;

}
