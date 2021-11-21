package dev.marco.example.springboot.rest;

import com.fasterxml.jackson.databind.JsonNode;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.AnnouncementComment;
import dev.marco.example.springboot.model.impl.AnnouncementImpl;
import dev.marco.example.springboot.service.AnnouncementService;
import dev.marco.example.springboot.util.ApiAddresses;
import dev.marco.example.springboot.util.ControllerUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@RestController
@RequestMapping(ApiAddresses.API_ANNOUNCEMENT)
public class AnnouncementController implements ApiAddresses {

    private final Properties properties = new Properties();
    private static final String ID_USER = "idUser";
    private static final String ID_ANNOUNCEMENT = "idAnnouncement";
    private static final String ID_LAST_COMMENT = "idLastComment";
    private static final String PAGINATION_SIZE = "paginationSize";
    private static final String COMMENT_CONTENT = "commentContent";
    private static final String SEARCH_PROJECT = "searchProject";
    private static final Logger log = Logger.getLogger(AnnouncementController.class);
    private final AnnouncementService announcementService;

    @Autowired
    private AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
        try {
            ControllerUtil.getProperty(properties);
        } catch (ControllerConfigException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(CONFIG_EXCEPTION));
        }
    }

    public void setTestConnection() throws DAOConfigException {
        announcementService.setTestConnection();
    }

    @PostMapping(API_CREATE_ANNOUNCEMENT)
    public BigInteger createAnnouncement(@RequestBody AnnouncementImpl announcement) {
        try {
            return announcementService.buildNewAnnouncement(new AnnouncementImpl.AnnouncementBuilder()
                    .setTitle(announcement.getTitle())
                    .setDescription(announcement.getDescription())
                    .setIdUser(announcement.getIdUser())
                    .setAddress(announcement.getAddress())
                    .build());
        } catch (AnnouncementException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (UserException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, properties.getProperty(USER_EXCEPTION));
        }
    }

    @GetMapping(API_GET_ALL_ANNOUNCEMENT)
    public List<Announcement> getAllAnnouncement(@PathVariable BigInteger idUser) {
        try {
            return announcementService.getAllAnnouncements(idUser);
        } catch (AnnouncementDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (AnnouncementException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        }
    }

    @DeleteMapping(API_DELETE_ANNOUNCEMENT)
    public void deleteAnnouncement(@RequestBody JsonNode requestBody) {
        BigInteger idUser = BigInteger.valueOf(requestBody.get(ID_USER).asLong());
        BigInteger idAnnouncement = BigInteger.valueOf(requestBody.get(ID_ANNOUNCEMENT).asLong());
        try {
            announcementService.deleteAnnouncement(idAnnouncement, idUser);
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (UserException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(USER_EXCEPTION));
        } catch (AnnouncementDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        } catch (UserDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @PutMapping(API_UPDATE_ANNOUNCEMENT)
    public void editAnnouncement(@RequestBody AnnouncementImpl announcement) {
        try {
            announcementService.editAnnouncement(new AnnouncementImpl.AnnouncementBuilder()
                    .setId(announcement.getId())
                    .setTitle(announcement.getTitle())
                    .setDescription(announcement.getDescription())
                    .setIdUser(announcement.getIdUser())
                    .setAddress(announcement.getAddress())
                    .build());
        } catch (AnnouncementException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (UserException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, properties.getProperty(USER_EXCEPTION));
        } catch (AnnouncementDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        } catch (UserDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @PostMapping(API_LIKE_ANNOUNCEMENT)
    public void setLikeAnnouncement(@RequestBody JsonNode requestBody) {
        BigInteger idAnnouncement = BigInteger.valueOf(requestBody.get(ID_ANNOUNCEMENT).asLong());
        try {
            announcementService.setLikeAnnouncement(idAnnouncement);
        } catch (AnnouncementException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (AnnouncementDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        } catch (UserDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(USER_EXCEPTION));
        }
    }

    @GetMapping(API_GET_COMMENTARIES)
    public List<AnnouncementComment> getCommentaries(@RequestBody JsonNode requestBody) {
        try {
            BigInteger idAnnouncement = BigInteger.valueOf(requestBody.get(ID_ANNOUNCEMENT).asLong());
            BigInteger idLastComment = BigInteger.valueOf(requestBody.get(ID_LAST_COMMENT).asLong());
            int paginationSize = requestBody.get(PAGINATION_SIZE).asInt();

            return announcementService.getComments(idAnnouncement, idLastComment, paginationSize);
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (AnnouncementDoesNotExistException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        }
    }

    @PostMapping(API_CREATE_COMMENTARY)
    public void createCommentary(@RequestBody JsonNode requestBody) {
        try {
            BigInteger idAnnouncement = BigInteger.valueOf(requestBody.get(ID_ANNOUNCEMENT).asLong());
            BigInteger idUser = BigInteger.valueOf(requestBody.get(ID_USER).asLong());
            String commentContent = requestBody.get(COMMENT_CONTENT).asText();
            announcementService.createComment(commentContent, idAnnouncement, idUser);
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (AnnouncementException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, properties.getProperty(ANNOUNCEMENT_EXCEPTION));
        }
    }

    @PostMapping(API_SEARCH_ANNOUNCEMENT)
    public Set<Announcement> searchAnnouncement(@RequestBody JsonNode requestBody) {
        String titleForSearch = requestBody.get(SEARCH_PROJECT).asText();
        BigInteger idUser = BigInteger.valueOf(requestBody.get(ID_USER).asLong());
        try {
            return announcementService.getSetByTitle(titleForSearch, idUser);
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        }
    }

    @GetMapping(API_GET_ANNOUNCEMENTS_BY_PAGE)
    public Page<Announcement> getAnnouncementsByPage(@RequestParam int page){
        try {
            return announcementService.getAnnouncementsByPage(page);
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (PageException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(PAGE_EXCEPTION));
        }
    }

    @GetMapping(API_GET_ANNOUNCEMENTS_BY_TITLE)
    public Page<Announcement> getAnnouncementsLikeTitle(@RequestParam int page, @RequestParam String title){
        try {
            return announcementService.getAnnouncementsLikeTitle(title, page);
        } catch (DAOLogicException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, properties.getProperty(DAO_LOGIC_EXCEPTION));
        } catch (PageException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, properties.getProperty(PAGE_EXCEPTION));
        }
    }

}
