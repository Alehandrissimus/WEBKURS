package dev.marco.example.springboot.service.impl;

import dev.marco.example.springboot.dao.AnnouncementDAO;
import dev.marco.example.springboot.dao.UserAnnouncementDAO;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.AnnouncementComment;
import dev.marco.example.springboot.model.UserRoles;
import dev.marco.example.springboot.security.JwtUser;
import dev.marco.example.springboot.service.AnnouncementService;
import dev.marco.example.springboot.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import static dev.marco.example.springboot.exception.MessagesForException.*;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private static final Logger log = Logger.getLogger(AnnouncementServiceImpl.class);
    private final AnnouncementDAO announcementDAO;
    private final UserAnnouncementDAO userAnnouncementDAO;
    private final UserService userService;

    @Autowired
    private AnnouncementServiceImpl(AnnouncementDAO announcementDAO, UserAnnouncementDAO userAnnouncementDAO, UserService userService){
        this.announcementDAO = announcementDAO;
        this.userAnnouncementDAO = userAnnouncementDAO;
        this.userService = userService;
    }

    @Override
    public void setTestConnection() throws DAOConfigException {
        try {
            announcementDAO.setTestConnection();
            userAnnouncementDAO.setTestConnection();
            userService.setTestConnection();
        } catch (DAOConfigException e) {
            log.error(DAO_CONFIG_EXCEPTION);
            throw new DAOConfigException(DAO_CONFIG_EXCEPTION, e);
        }
    }

    @Override
    public List<Announcement> getAllAnnouncements(BigInteger idUser)
            throws AnnouncementDoesNotExistException, DAOLogicException, AnnouncementException {
        return announcementDAO.getAllAnnouncements(idUser);
    }

    @Override
    public BigInteger buildNewAnnouncement(Announcement announcement)
            throws AnnouncementException, DAOLogicException {
        try {
            if(announcementDAO.isAnnouncementByTitle(announcement.getTitle())) {
                log.error(ANNOUNCEMENT_ALREADY_EXISTS);
                throw new AnnouncementException(ANNOUNCEMENT_ALREADY_EXISTS);
            }
            return announcementDAO.createAnnouncement(announcement);
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION);
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public void editAnnouncement(Announcement announcement)
            throws DAOLogicException, AnnouncementDoesNotExistException, UserDoesNotExistException, UserException {
        try {
            if(!announcementDAO.isAnnouncementById(announcement.getId()))
                throw new AnnouncementDoesNotExistException(ANNOUNCEMENT_NOT_FOUND_EXCEPTION);

            BigInteger owner = announcementDAO.getAnnouncementById(announcement.getId()).getIdUser();
            UserRoles userRole = userService.getUserById(announcement.getIdUser()).getUserRole();
            if(!owner.equals(announcement.getIdUser()) && !userRole.equals(UserRoles.ADMIN))
                throw new UserException(DONT_ENOUGH_RIGHTS);

            announcementDAO.editAnnouncement(announcement);
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION);
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public void deleteAnnouncement(BigInteger idAnnouncement, BigInteger idUser)
            throws DAOLogicException, UserException, AnnouncementDoesNotExistException, UserDoesNotExistException {
        try {
            if(!announcementDAO.isAnnouncementById(idAnnouncement))
                throw new AnnouncementDoesNotExistException(ANNOUNCEMENT_NOT_FOUND_EXCEPTION);

            BigInteger owner = announcementDAO.getAnnouncementById(idAnnouncement).getIdUser();
            UserRoles userRole = userService.getUserById(idUser).getUserRole();
            if(!owner.equals(idUser) && !userRole.equals(UserRoles.ADMIN))
                throw new UserException(DONT_ENOUGH_RIGHTS);

            announcementDAO.deleteAnnouncement(idAnnouncement);
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION);
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public void setLikeAnnouncement(BigInteger idAnnouncement)
            throws DAOLogicException, AnnouncementDoesNotExistException, UserDoesNotExistException {
        try {
            JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            BigInteger idUser = BigInteger.valueOf(user.getId());
            if(!announcementDAO.isAnnouncementById(idAnnouncement))
                throw new AnnouncementDoesNotExistException(ANNOUNCEMENT_NOT_FOUND_EXCEPTION);
            userService.getUserById(idUser); // throw UserDoesNotExistException

            if(userAnnouncementDAO.isParticipant(idAnnouncement, idUser)){
                userAnnouncementDAO.deleteParticipant(idAnnouncement, idUser);
                announcementDAO.toDisLike(idAnnouncement);
            } else{
                userAnnouncementDAO.addParticipant(idAnnouncement, idUser);
                announcementDAO.toLike(idAnnouncement);
            }
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION);
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public List<Announcement> getPopularAnnouncements(int numberAnnouncements, BigInteger idUser)
            throws AnnouncementDoesNotExistException, DAOLogicException {
        return announcementDAO.getPopular(numberAnnouncements, idUser);
    }

    @Override
    public Set<Announcement> getAnnouncementsLikedByUser(BigInteger idUser)
            throws AnnouncementDoesNotExistException, DAOLogicException, AnnouncementException {
        return userAnnouncementDAO.getAnnouncementsLikedByUser(idUser);
    }

    @Override
    public Set<Announcement> getSetByTitle(String title, BigInteger idUser)
            throws DAOLogicException {
        return announcementDAO.getSetByTitle(title, idUser);
    }

    @Override
    public List<AnnouncementComment> getComments(BigInteger AnnouncementId, BigInteger lastCommentId, int count)
            throws AnnouncementDoesNotExistException, DAOLogicException {
        return announcementDAO.getComments(AnnouncementId, lastCommentId, count);
    }

    @Override
    public void createComment(String commentContent, BigInteger announcementId, BigInteger userId)
            throws DAOLogicException, AnnouncementException {
        if(StringUtils.isEmpty(commentContent)) {
            log.error("Comment contents is empty or null");
            throw new AnnouncementException("");
        }
        if(announcementId == null || announcementId.equals(BigInteger.ZERO)) {
            log.error("announcementId is 0 or null");
            throw new AnnouncementException(ANNOUNCEMENT_NOT_FOUND_EXCEPTION);
        }
        if(userId == null || userId.equals(BigInteger.ZERO)) {
            log.error("announcementId is 0 or null");
            throw new AnnouncementException(USER_IS_NULL);
        }
        announcementDAO.createComment(commentContent, announcementId, userId);
    }

    @Override
    public Page<Announcement> getAnnouncementsByPage(int pageNumber)
            throws DAOLogicException, PageException {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BigInteger idUser = BigInteger.valueOf(user.getId());
        if (pageNumber < MIN_PAGE) {
            log.error(PAGE_DOES_NOT_EXIST);
            throw new PageException(PAGE_DOES_NOT_EXIST);
        }
        Pageable pageable = PageRequest.of(--pageNumber, PAGE_SIZE);
        Page<Announcement> page = announcementDAO.getAnnouncementsByPage(idUser, pageable);
        if (!page.hasContent()) {
            log.error(PAGE_DOES_NOT_EXIST);
            throw new PageException(PAGE_DOES_NOT_EXIST);
        }
        return page;
    }

    @Override
    public Page<Announcement> getAnnouncementsLikeTitle(String title, int pageNumber)
            throws DAOLogicException, PageException {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BigInteger idUser = BigInteger.valueOf(user.getId());
        if (pageNumber < MIN_PAGE) {
            log.error(PAGE_DOES_NOT_EXIST);
            throw new PageException(PAGE_DOES_NOT_EXIST);
        }
        Pageable pageable = PageRequest.of(--pageNumber, PAGE_SIZE);
        Page<Announcement> page = announcementDAO.getAnnouncementsByTitle(title, idUser, pageable);
        if (!page.hasContent()) {
            log.error(PAGE_DOES_NOT_EXIST);
            throw new PageException(PAGE_DOES_NOT_EXIST);
        }
        return page;
    }
}
