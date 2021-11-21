package dev.marco.example.springboot.service;

import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.AnnouncementComment;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface AnnouncementService {

    int MIN_PAGE = 1;
    int PAGE_SIZE = 6;

    List<Announcement> getAllAnnouncements(BigInteger idUser)
            throws AnnouncementDoesNotExistException, DAOLogicException, AnnouncementException;

    BigInteger buildNewAnnouncement(Announcement announcement)
            throws AnnouncementException, DAOLogicException, UserException;

    void editAnnouncement(Announcement announcement)
            throws AnnouncementException, DAOLogicException, UserException, AnnouncementDoesNotExistException, UserDoesNotExistException;

    void deleteAnnouncement(BigInteger idAnnouncement, BigInteger idUser)
            throws DAOLogicException, UserException, AnnouncementDoesNotExistException, UserDoesNotExistException;

    void setLikeAnnouncement(BigInteger idAnnouncement)
            throws AnnouncementException, DAOLogicException, AnnouncementDoesNotExistException, UserDoesNotExistException;

    List<Announcement> getPopularAnnouncements(int numberAnnouncements, BigInteger idUser)
            throws AnnouncementDoesNotExistException, DAOLogicException;

    Set<Announcement> getAnnouncementsLikedByUser(BigInteger idUser)
            throws AnnouncementDoesNotExistException, DAOLogicException, AnnouncementException;

    void setTestConnection() throws DAOConfigException;

    List<AnnouncementComment> getComments(BigInteger AnnouncementId, BigInteger lastCommentId, int count) throws AnnouncementDoesNotExistException, DAOLogicException;

    void createComment(String commentContent, BigInteger  announcementId, BigInteger userId) throws DAOLogicException, AnnouncementException;

    Set<Announcement> getSetByTitle(String title, BigInteger idUser) throws DAOLogicException;

    Page<Announcement> getAnnouncementsByPage(int pageNumber)
            throws DAOLogicException, PageException;

    Page<Announcement> getAnnouncementsLikeTitle(String title, int pageNumber)
            throws DAOLogicException, PageException;
}
