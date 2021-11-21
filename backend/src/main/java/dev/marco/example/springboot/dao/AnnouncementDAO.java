package dev.marco.example.springboot.dao;

import dev.marco.example.springboot.exception.AnnouncementDoesNotExistException;
import dev.marco.example.springboot.exception.AnnouncementException;
import dev.marco.example.springboot.exception.DAOConfigException;
import dev.marco.example.springboot.exception.DAOLogicException;
import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.AnnouncementComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface AnnouncementDAO {

  String URL_PROPERTY = "${spring.datasource.url}";
  String USERNAME_PROPERTY = "${spring.datasource.username}";
  String PASSWORD_PROPERTY = "${spring.datasource.password}";

  String SELECT_ANNOUNCEMENT_BY_TITLE = "SELECT_ANNOUNCEMENT_BY_TITLE";
  String SELECT_SET_ANNOUNCEMENT_BY_TITLE = "SELECT_SET_ANNOUNCEMENT_BY_TITLE";
  String CREATE_ANNOUNCEMENT = "CREATE_ANNOUNCEMENT";
  String UPDATE_ANNOUNCEMENT = "UPDATE_ANNOUNCEMENT";
  String DELETE_ANNOUNCEMENT_BY_ID = "DELETE_ANNOUNCEMENT_BY_ID";
  String GET_POPULAR_ANNOUNCEMENT = "GET_POPULAR_ANNOUNCEMENT";
  String SELECT_ALL_ANNOUNCEMENT = "SELECT_ALL_ANNOUNCEMENT";
  String GET_ANNOUNCEMENT_BY_ID = "GET_ANNOUNCEMENT_BY_ID";
  String SET_LIKE = "SET_LIKE";
  String UNSET_LIKE = "UNSET_LIKE";
  String GET_ANNOUNCEMENT_COMMENTARIES_DESC = "GET_ANNOUNCEMENT_COMMENTARIES_DESC";
  String CREATE_COMMENTARY = "CREATE_COMMENTARY";
  String SELECT_ANNOUNCEMENTS_BY_PAGE = "SELECT_ANNOUNCEMENTS_BY_PAGE";
  String SELECT_NUMBER_OF_ANNOUNCEMENT = "SELECT_NUMBER_OF_ANNOUNCEMENT";
  String SELECT_ANNOUNCEMENTS_BY_TITLE = "SELECT_ANNOUNCEMENTS_BY_TITLE";
  String SELECT_NUMBER_OF_ANNOUNCEMENT_BY_TITLE = "SELECT_NUMBER_OF_ANNOUNCEMENT_BY_TITLE";

  String ID_ANNOUNCEMENT = "ID_ANNOUNCEMENT";
  String TITLE = "TITLE";
  String DESCRIPTION = "DESCRIPTION";
  String OWNER = "OWNR";
  String DATE_CREATE = "DATE_CREATE";
  String ADDRESS = "ADDRESS";
  String LIKES = "LIKES";
  String FIRST_NAME = "first_name";
  String LAST_NAME = "last_name";
  int COLUMN_IS_LIKED = 8;
  String ID_COMMENTARY = "ID_COMMENTARY";

  String ERROR_TEST_CONNECTION = "Error while setting test connection ";
  String MESSAGE_FOR_GET_BY_TITLE = " in getByTitle";
  String MESSAGE_FOR_CREATE_ANNOUNCEMENT = " in createAnnouncement";
  String MESSAGE_FOR_GET_ANNOUNCEMENT_BY_ID = " in createAnnouncement";
  String TEST = "_TEST";

  void setTestConnection() throws DAOConfigException;

  Announcement getAnnouncementById(BigInteger idAnnouncement)
      throws AnnouncementDoesNotExistException, DAOLogicException;

  void deleteAnnouncement(BigInteger idAnnouncement) throws DAOLogicException;

  BigInteger createAnnouncement(Announcement newAnnouncement) throws DAOLogicException;

  void editAnnouncement(Announcement newAnnouncement) throws DAOLogicException;

  Announcement getByTitle(String title) throws AnnouncementDoesNotExistException, DAOLogicException;

  Set<Announcement> getSetByTitle(String title, BigInteger idUser) throws DAOLogicException;

  List<Announcement> getAllAnnouncements(BigInteger idUser)
      throws AnnouncementDoesNotExistException, DAOLogicException, AnnouncementException;

  List<Announcement> getPopular(int number, BigInteger idUser)
      throws DAOLogicException, AnnouncementDoesNotExistException;

  boolean isAnnouncementByTitle(String title) throws DAOLogicException;

  boolean isAnnouncementById(BigInteger idAnnouncement) throws DAOLogicException;

  void toLike(BigInteger idAnnouncement) throws DAOLogicException;

  void toDisLike(BigInteger idAnnouncement) throws DAOLogicException;

  List<AnnouncementComment> getComments(BigInteger announcementId, BigInteger lastCommentId,
      int count) throws AnnouncementDoesNotExistException, DAOLogicException;

  void createComment(String commentContent, BigInteger announcementId, BigInteger userId)
      throws DAOLogicException;

  Page<Announcement> getAnnouncementsByPage(BigInteger idUser, Pageable pageable)
          throws DAOLogicException;

  long countOfAnnouncements() throws DAOLogicException;

  Page<Announcement> getAnnouncementsByTitle(String title, BigInteger idUser, Pageable pageable)
          throws DAOLogicException;

  long countOfAnnouncementsByTitle(String title) throws DAOLogicException;
}
