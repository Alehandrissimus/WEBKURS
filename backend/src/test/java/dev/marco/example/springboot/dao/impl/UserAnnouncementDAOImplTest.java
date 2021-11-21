package dev.marco.example.springboot.dao.impl;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.impl.AnnouncementImpl;
import dev.marco.example.springboot.model.impl.UserImpl;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserAnnouncementDAOImplTest {

  private static final String TEST_TITLE = "test";
  private static final String TEST_DESCRIPTION = "testDescription";
  private static final String TEST_ADDRESS = "testAddress";
  private static final String LOG_ERROR = "Error while setting test connection" + " ";

  private UserAnnouncementDAOImpl userAnnouncementDAO;
  private AnnouncementDAOImpl announcementDAO;
  private UserDAOImpl userDAO;

  private static final Logger log = Logger.getLogger(UserAnnouncementDAOImpl.class);

  @Autowired
  private void setUserAnnouncementDAO(UserAnnouncementDAOImpl userAnnouncementDAO) {
    this.userAnnouncementDAO = userAnnouncementDAO;
    try {
      userAnnouncementDAO.setTestConnection();
    } catch (DAOConfigException e) {
      log.error(LOG_ERROR + e.getMessage());
    }
  }

  @Autowired
  private void setAnnouncementDAO(AnnouncementDAOImpl announcementDAO) {
    this.announcementDAO = announcementDAO;
    try {
      announcementDAO.setTestConnection();
    } catch (DAOConfigException e) {
      log.error(LOG_ERROR + e.getMessage());
    }
  }

  @Autowired
  private void setUserDAO(UserDAOImpl userDAO) {
    this.userDAO = userDAO;
    try {
      userDAO.setTestConnection();
    } catch (DAOConfigException e) {
      log.error(LOG_ERROR + e.getMessage());
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getAnnouncementsLikedByUser() {
    try {
      Set<Announcement> announcementSet = userAnnouncementDAO.getAnnouncementsLikedByUser(
          BigInteger.ONE);
      assertNotNull(announcementSet);
      for (Announcement announcement : announcementSet) {
        assertNotNull(announcement);
      }
    } catch (AnnouncementDoesNotExistException | DAOLogicException | AnnouncementException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void getUsersLikedAnnouncement() {
    try {
      Set<User> userSet = userAnnouncementDAO.getUsersLikedAnnouncement(BigInteger.ONE);
      assertNotNull(userSet);
      for (User user : userSet) {
        assertNotNull(user);
      }
    } catch (UserDoesNotExistException | DAOLogicException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }

  @Test
  @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
  void addAndGetAndDeleteParticipantById() {
    try {
      String firstName = "testFirstName";
      String lastName = "testLastName";
      String description = "testDescription";
      String email = "test@gmail.com";
      String password = "testPassword";
      String emailCode = "testEmailCode";

      BigInteger idAnnouncement = announcementDAO.createAnnouncement(
          new AnnouncementImpl.AnnouncementBuilder()
              .setTitle(TEST_TITLE)
              .setDescription(TEST_DESCRIPTION)
              .setIdUser(BigInteger.ONE)
              .setDate(new Date())
              .setAddress(TEST_ADDRESS)
              .setParticipantsCap(5)
              .build()
      );
      userDAO.createUser(
          new UserImpl.UserBuilder()
              .setFirstName(firstName)
              .setLastName(lastName)
              .setDescription(description)
              .setEmail(email)
              .setPassword(password)
              .setEmailCode(emailCode)
              .build()
      );
      assertNotNull(announcementDAO.getAnnouncementById(idAnnouncement));
      User user = userDAO.getUserByEmail(email);
      assertNotNull(user);
      userAnnouncementDAO.addParticipant(idAnnouncement, user.getId());
      assertTrue(userAnnouncementDAO.isParticipant(idAnnouncement, user.getId()));
      userAnnouncementDAO.deleteParticipant(idAnnouncement, user.getId());
      assertFalse(userAnnouncementDAO.isParticipant(idAnnouncement, user.getId()));
      announcementDAO.deleteAnnouncement(idAnnouncement);
      userDAO.deleteUser(user.getId());
    } catch (DAOLogicException | AnnouncementDoesNotExistException | UserDoesNotExistException | AnnouncementException | UserException e) {
      log.error(MessagesForException.TEST_ERROR + e.getMessage());
      fail();
    }
  }
}