package dev.marco.example.springboot.dao.impl;

import dev.marco.example.springboot.exception.MessagesForException;
import dev.marco.example.springboot.model.AnnouncementComment;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import dev.marco.example.springboot.exception.AnnouncementDoesNotExistException;
import dev.marco.example.springboot.exception.AnnouncementException;
import dev.marco.example.springboot.exception.DAOConfigException;
import dev.marco.example.springboot.exception.DAOLogicException;
import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.impl.AnnouncementImpl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AnnouncementDAOImplTest {

    private static final String TEST_TITLE = "test";
    private static final String TEST_DESCRIPTION = "testDescription";
    private static final String TEST_ADDRESS = "testAddress";
    private static final String TEST_NEW_TITLE = "newTest";
    private static final String TEST_NEW_DESCRIPTION = "newDescription";
    private static final String TEST_NEW_ADDRESS = "newAddress";
    private static final String LOG_ERROR = "Error while setting test connection" + " ";

    private AnnouncementDAOImpl announcementDAO;
    private static final Logger log = Logger.getLogger(AnnouncementDAOImplTest.class);

    @Autowired
    private void setAnnouncementDAO(AnnouncementDAOImpl announcementDAO) {
        this.announcementDAO = announcementDAO;
        try {
            announcementDAO.setTestConnection();
        } catch (DAOConfigException e) {
            log.error(LOG_ERROR + e.getMessage());
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void getPopular() {
        try {
            List<Announcement> popularAnnouncement = announcementDAO.getPopular(4, BigInteger.valueOf(6));
            assertNotNull(popularAnnouncement);
            for(Announcement announcement : popularAnnouncement)
                assertNotNull(announcement);
            assertEquals(4, popularAnnouncement.size());
        } catch (DAOLogicException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void getByTitle() {
        try {
            announcementDAO.createAnnouncement(new AnnouncementImpl.AnnouncementBuilder()
                    .setTitle(TEST_TITLE)
                    .setDescription(TEST_DESCRIPTION)
                    .setIdUser(BigInteger.ONE)
                    .setAddress(TEST_ADDRESS)
                    .build());
            Announcement announcement = announcementDAO.getByTitle(TEST_TITLE);
            assertNotNull(announcement);
            assertEquals(TEST_TITLE, announcement.getTitle());
            announcementDAO.deleteAnnouncement(announcement.getId());
        } catch (AnnouncementDoesNotExistException | DAOLogicException | AnnouncementException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void createAnnouncement() {
        try {
            Announcement newAnnouncement = new AnnouncementImpl.AnnouncementBuilder()
                    .setTitle(TEST_TITLE)
                    .setDescription(TEST_DESCRIPTION)
                    .setIdUser(BigInteger.ONE)
                    .setDate(new Date())
                    .setAddress(TEST_ADDRESS)
                    .build();
            BigInteger idAnnouncement = announcementDAO.createAnnouncement(newAnnouncement);
            assertTrue(idAnnouncement.intValue() > 0);
            announcementDAO.deleteAnnouncement(idAnnouncement);
        } catch (DAOLogicException | AnnouncementException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void editAnnouncement() {
        try {
            Announcement newAnnouncement = new AnnouncementImpl.AnnouncementBuilder()
                    .setTitle(TEST_TITLE)
                    .setDescription(TEST_DESCRIPTION)
                    .setIdUser(BigInteger.ONE)
                    .setDate(new Date())
                    .setAddress(TEST_ADDRESS)
                    .build();
            BigInteger idAnnouncement = announcementDAO.createAnnouncement(newAnnouncement);
            Announcement announcement = announcementDAO.getAnnouncementById(idAnnouncement);
            assertNotNull(announcement);
            announcement.setTitle(TEST_NEW_TITLE);
            announcement.setDescription(TEST_NEW_DESCRIPTION);
            announcement.setAddress(TEST_NEW_ADDRESS);
            announcementDAO.editAnnouncement(announcement);
            Announcement testAnnouncement = announcementDAO.getAnnouncementById(idAnnouncement);
            assertEquals(announcement.getTitle(), testAnnouncement.getTitle());
            assertEquals(announcement.getDescription(), testAnnouncement.getDescription());
            assertEquals(announcement.getAddress(), testAnnouncement.getAddress());
            announcementDAO.deleteAnnouncement(idAnnouncement);
        } catch (DAOLogicException | AnnouncementDoesNotExistException | AnnouncementException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test()
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    @ExceptionHandler()
    void deleteAnnouncement() {
        try {
            Announcement newAnnouncement = new AnnouncementImpl.AnnouncementBuilder()
                    .setTitle(TEST_TITLE)
                    .setDescription(TEST_DESCRIPTION)
                    .setIdUser(BigInteger.ONE)
                    .setDate(new Date())
                    .setAddress(TEST_ADDRESS)
                    .build();
            BigInteger idAnnouncement = announcementDAO.createAnnouncement(newAnnouncement);
            assertNotNull(announcementDAO.getAnnouncementById(idAnnouncement));
            announcementDAO.deleteAnnouncement(idAnnouncement);
            AnnouncementDoesNotExistException thrown = assertThrows(AnnouncementDoesNotExistException.class, () ->
                announcementDAO.getAnnouncementById(idAnnouncement));
            assertNotNull(thrown);
        } catch (DAOLogicException | AnnouncementDoesNotExistException | AnnouncementException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void getAnnouncementById() {
        try {
            assertNotNull(announcementDAO.getAnnouncementById(BigInteger.ONE));
        } catch (AnnouncementDoesNotExistException | DAOLogicException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void getSetByTitle() {
        try {
            String title = "gath";
            Set<Announcement> announcements = announcementDAO.getSetByTitle(title, BigInteger.ONE);
            assertNotNull(announcements);
            for(Announcement announcement : announcements){
                assertNotNull(announcement);
            }
        } catch (DAOLogicException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void toLike() {
        try {
            int likes = announcementDAO.getAnnouncementById(BigInteger.ONE).getParticipantsCap();
            announcementDAO.toLike(BigInteger.ONE);
            assertEquals(likes + 1, announcementDAO.getAnnouncementById(BigInteger.ONE).getParticipantsCap());
        } catch (DAOLogicException | AnnouncementDoesNotExistException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void toDisLike() {
        try {
            int likes = announcementDAO.getAnnouncementById(BigInteger.ONE).getParticipantsCap();
            announcementDAO.toDisLike(BigInteger.ONE);
            assertEquals(likes - 1, announcementDAO.getAnnouncementById(BigInteger.ONE).getParticipantsCap());
        } catch (DAOLogicException | AnnouncementDoesNotExistException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void isAnnouncementById() {
        try {
            assertTrue(announcementDAO.isAnnouncementById(BigInteger.ONE));
        } catch (DAOLogicException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void getAllAnnouncementByIdUser() {
        try {
            List<Announcement> allAnnouncement = announcementDAO.getAllAnnouncements(BigInteger.ONE);
            assertNotNull(allAnnouncement);
            for(Announcement announcement: allAnnouncement){
                assertNotNull(announcement);
            }
        } catch (DAOLogicException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void getCommentaries() {
        try {
            List<AnnouncementComment> comments = announcementDAO.getComments(
                    BigInteger.ONE,
                    BigInteger.ZERO,
                    15);
            assertNotNull(comments.get(0));
            assertTrue(StringUtils.isNotBlank(comments.get(0).getContent()));
        } catch (DAOLogicException | AnnouncementDoesNotExistException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void createComment() {
        try {
            String commentContent = "" + new Random().nextInt(500000);
            announcementDAO.createComment(commentContent, BigInteger.ONE, BigInteger.ONE);

            List<AnnouncementComment> comments = announcementDAO.getComments(
                    BigInteger.ONE,
                    BigInteger.ONE,
                    999999);

            assertEquals(comments.get(0).getContent(),  commentContent);
        } catch (DAOLogicException | AnnouncementDoesNotExistException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void countOfAnnouncements() {
        try {
            assertTrue(announcementDAO.countOfAnnouncements() >= 0);
        } catch (DAOLogicException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void getAnnouncementsByPage() {
        try {
            Page<Announcement> page = announcementDAO.getAnnouncementsByPage(BigInteger.ONE,
                    PageRequest.of(0, 6));
            page.get().forEach(Assertions::assertNotNull);
        } catch (DAOLogicException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void countOfAnnouncementsByTitle() {
        try {
            assertTrue(announcementDAO.countOfAnnouncementsByTitle("gath") >= 0);
        } catch (DAOLogicException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }

    @Test
    @Timeout(value = 10000, unit= TimeUnit.MILLISECONDS)
    void getAnnouncementsByTitle() {
        try {
            Page<Announcement> page = announcementDAO.getAnnouncementsByTitle("gath", BigInteger.ONE,
                    PageRequest.of(0, 6));
            page.get().forEach(Assertions::assertNotNull);
        } catch (DAOLogicException e) {
            log.error(MessagesForException.TEST_ERROR + e.getMessage());
            fail();
        }
    }
}