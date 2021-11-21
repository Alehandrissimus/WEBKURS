package dev.marco.example.springboot.dao.impl;

import dev.marco.example.springboot.dao.AnnouncementDAO;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.AnnouncementComment;
import dev.marco.example.springboot.model.impl.AnnouncementCommentImpl;
import dev.marco.example.springboot.model.impl.AnnouncementImpl;
import dev.marco.example.springboot.model.impl.UserImpl;
import dev.marco.example.springboot.util.DAOUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.*;
import java.util.*;

import static dev.marco.example.springboot.exception.MessagesForException.*;

@Repository
public class AnnouncementDAOImpl implements AnnouncementDAO {

    private Connection connection;
    private final Properties properties = new Properties();
    private static final Logger log = Logger.getLogger(AnnouncementDAOImpl.class);

    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;

    @Autowired
    public AnnouncementDAOImpl(
            @Value(URL_PROPERTY) String URL,
            @Value(USERNAME_PROPERTY) String USERNAME,
            @Value(PASSWORD_PROPERTY) String PASSWORD
    ) throws DAOConfigException {
        this.URL = URL;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;

        connection = DAOUtil.getDataSource(URL, USERNAME , PASSWORD, properties);
    }

    public void setTestConnection() throws DAOConfigException {
        try {
            connection = DAOUtil.getDataSource(URL, USERNAME + TEST, PASSWORD, properties);
        } catch (DAOConfigException e) {
            log.error(ERROR_TEST_CONNECTION + e.getMessage());
            throw new DAOConfigException(ERROR_TEST_CONNECTION, e);
        }
    }

    @Override
    public Announcement getByTitle(String title) throws AnnouncementDoesNotExistException, DAOLogicException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SELECT_ANNOUNCEMENT_BY_TITLE))){
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                log.error(ANNOUNCEMENT_HAS_NOT_BEEN_RECEIVED + MESSAGE_FOR_GET_BY_TITLE);
                throw new AnnouncementDoesNotExistException(ANNOUNCEMENT_NOT_FOUND_EXCEPTION);
            }
            resultSet.next();
            return new AnnouncementImpl.AnnouncementBuilder()
                    .setId(BigInteger.valueOf(resultSet.getLong(ID_ANNOUNCEMENT)))
                    .setTitle(resultSet.getString(TITLE))
                    .setDescription(resultSet.getString(DESCRIPTION))
                    .setIdUser(BigInteger.valueOf(resultSet.getLong(OWNER)))
                    .setDate(resultSet.getDate(DATE_CREATE))
                    .setAddress(resultSet.getString(ADDRESS))
                    .setParticipantsCap(resultSet.getInt(LIKES))
                    .build();

        } catch (SQLException | AnnouncementException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public boolean isAnnouncementByTitle(String title) throws DAOLogicException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SELECT_ANNOUNCEMENT_BY_TITLE))){
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public Set<Announcement> getSetByTitle(String title, BigInteger idUser) throws DAOLogicException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SELECT_SET_ANNOUNCEMENT_BY_TITLE))){
            preparedStatement.setLong(1, idUser.longValue());
            preparedStatement.setString(2, "%" + title + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            Set<Announcement> announcements = new HashSet<>();
            if(!resultSet.isBeforeFirst())
                return announcements;
            while (resultSet.next()) {
                Announcement announcement = new AnnouncementImpl.AnnouncementBuilder()
                        .setId(BigInteger.valueOf(resultSet.getLong(ID_ANNOUNCEMENT)))
                        .setTitle(resultSet.getString(TITLE))
                        .setDescription(resultSet.getString(DESCRIPTION))
                        .setUser(new UserImpl.UserBuilder()
                                .setId(BigInteger.valueOf(resultSet.getLong(OWNER)))
                                .setFirstName(resultSet.getString(FIRST_NAME))
                                .setLastName(resultSet.getString(LAST_NAME))
                                .build())
                        .setDate(resultSet.getDate(DATE_CREATE))
                        .setAddress(resultSet.getString(ADDRESS))
                        .setParticipantsCap(resultSet.getInt(LIKES))
                        .setBlank(BigInteger.valueOf(resultSet.getLong(COLUMN_IS_LIKED)))
                        .setIsLiked(!resultSet.wasNull())
                        .build();
                announcements.add(announcement);
            }
            return announcements;
        } catch (SQLException | AnnouncementException | UserException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public BigInteger createAnnouncement(Announcement newAnnouncement) throws DAOLogicException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(CREATE_ANNOUNCEMENT),
                new String[]{ID_ANNOUNCEMENT})){
            preparedStatement.setString(1, newAnnouncement.getTitle());
            preparedStatement.setString(2, newAnnouncement.getDescription());
            preparedStatement.setLong(3, newAnnouncement.getIdUser().longValue());
            preparedStatement.setDate(4,  new Date(System.currentTimeMillis()));
            preparedStatement.setString(5, newAnnouncement.getAddress());
            int idAnnouncement = preparedStatement.executeUpdate();
            if (idAnnouncement > 0)
            {
                ResultSet resultSets = preparedStatement.getGeneratedKeys();
                resultSets.next();
                return BigInteger.valueOf(resultSets.getLong(1));
            }
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION + MESSAGE_FOR_CREATE_ANNOUNCEMENT);
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public void editAnnouncement(Announcement newAnnouncement) throws DAOLogicException {

        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(UPDATE_ANNOUNCEMENT))){
            preparedStatement.setString(1, newAnnouncement.getTitle());
            preparedStatement.setString(2, newAnnouncement.getDescription());
            preparedStatement.setString(3, newAnnouncement.getAddress());
            preparedStatement.setLong(4, newAnnouncement.getId().longValue());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public void deleteAnnouncement(BigInteger idAnnouncement) throws DAOLogicException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(DELETE_ANNOUNCEMENT_BY_ID))){
            preparedStatement.setLong(1, idAnnouncement.longValue());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public List<Announcement> getAllAnnouncements(BigInteger idUser)
            throws DAOLogicException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                properties.getProperty(SELECT_ALL_ANNOUNCEMENT))){
            preparedStatement.setLong(1, idUser.longValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Announcement> announcements = new ArrayList<>();
            if(!resultSet.isBeforeFirst())
                return announcements;
            while (resultSet.next()) {
                Announcement announcement = new AnnouncementImpl.AnnouncementBuilder()
                        .setId(BigInteger.valueOf(resultSet.getLong(ID_ANNOUNCEMENT)))
                        .setTitle(resultSet.getString(TITLE))
                        .setDescription(resultSet.getString(DESCRIPTION))
                        .setUser(new UserImpl.UserBuilder()
                                .setId(BigInteger.valueOf(resultSet.getLong(OWNER)))
                                .setFirstName(resultSet.getString(FIRST_NAME))
                                .setLastName(resultSet.getString(LAST_NAME))
                                .build())
                        .setDate(resultSet.getDate(DATE_CREATE))
                        .setAddress(resultSet.getString(ADDRESS))
                        .setParticipantsCap(resultSet.getInt(LIKES))
                        .setBlank(BigInteger.valueOf(resultSet.getLong(COLUMN_IS_LIKED)))
                        .setIsLiked(resultSet.wasNull())
                        .build();
                announcements.add(announcement);
            }
            return announcements;
        } catch (SQLException | AnnouncementException | UserException e) {
            log.error(e.getMessage(), e);
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public List<Announcement> getPopular(int number, BigInteger idUser) throws DAOLogicException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(GET_POPULAR_ANNOUNCEMENT))){
            preparedStatement.setLong(1, idUser.longValue());
            preparedStatement.setInt(2, number);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Announcement> popularAnnouncement = new ArrayList<>();
            if(!resultSet.isBeforeFirst()){
                return popularAnnouncement;
            }
            while (resultSet.next()) {
                Announcement announcement = new AnnouncementImpl.AnnouncementBuilder()
                        .setId(BigInteger.valueOf(resultSet.getLong(ID_ANNOUNCEMENT)))
                        .setTitle(resultSet.getString(TITLE))
                        .setDescription(resultSet.getString(DESCRIPTION))
                        .setUser(new UserImpl.UserBuilder()
                                .setId(BigInteger.valueOf(resultSet.getLong(OWNER)))
                                .setFirstName(resultSet.getString(FIRST_NAME))
                                .setLastName(resultSet.getString(LAST_NAME))
                                .build())
                        .setDate(resultSet.getDate(DATE_CREATE))
                        .setAddress(resultSet.getString(ADDRESS))
                        .setParticipantsCap(resultSet.getInt(LIKES))
                        .setBlank(BigInteger.valueOf(resultSet.getLong(COLUMN_IS_LIKED)))
                        .setIsLiked(!resultSet.wasNull())
                        .build();
                popularAnnouncement.add(announcement);
            }
            return popularAnnouncement;
        } catch (SQLException | AnnouncementException | UserException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public Announcement getAnnouncementById(BigInteger idAnnouncement) throws AnnouncementDoesNotExistException, DAOLogicException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(GET_ANNOUNCEMENT_BY_ID))){
            preparedStatement.setLong(1, idAnnouncement.longValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst()){
                log.error(ANNOUNCEMENT_HAS_NOT_BEEN_RECEIVED + MESSAGE_FOR_GET_ANNOUNCEMENT_BY_ID);
                throw new AnnouncementDoesNotExistException(ANNOUNCEMENT_NOT_FOUND_EXCEPTION);
            }
            resultSet.next();
            return new AnnouncementImpl.AnnouncementBuilder()
                    .setId(BigInteger.valueOf(resultSet.getLong(ID_ANNOUNCEMENT)))
                    .setTitle(resultSet.getString(TITLE))
                    .setDescription(resultSet.getString(DESCRIPTION))
                    .setIdUser(BigInteger.valueOf(resultSet.getLong(OWNER)))
                    .setDate(resultSet.getDate(DATE_CREATE))
                    .setAddress(resultSet.getString(ADDRESS))
                    .setParticipantsCap(resultSet.getInt(LIKES))
                    .build();
        } catch (SQLException | AnnouncementException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public boolean isAnnouncementById(BigInteger idAnnouncement) throws DAOLogicException {
        try ( PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(GET_ANNOUNCEMENT_BY_ID))){
            preparedStatement.setLong(1, idAnnouncement.longValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.isBeforeFirst();
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public void toLike(BigInteger idAnnouncement) throws DAOLogicException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(SET_LIKE))){
            preparedStatement.setLong(1, idAnnouncement.longValue());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public void toDisLike(BigInteger idAnnouncement) throws DAOLogicException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(properties.getProperty(UNSET_LIKE))){
            preparedStatement.setLong(1, idAnnouncement.longValue());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public List<AnnouncementComment> getComments(BigInteger announcementId, BigInteger lastCommentId, int count) throws AnnouncementDoesNotExistException, DAOLogicException {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(properties.getProperty(GET_ANNOUNCEMENT_COMMENTARIES_DESC))) {
            preparedStatement.setLong(1, announcementId.longValue());
            preparedStatement.setLong(2, lastCommentId.longValue());
            preparedStatement.setInt(3, count);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                log.error(ANNOUNCEMENT_HAS_NOT_BEEN_RECEIVED + MESSAGE_FOR_GET_ANNOUNCEMENT_BY_ID);
                throw new AnnouncementDoesNotExistException(ANNOUNCEMENT_NOT_FOUND_EXCEPTION);
            }

            List<AnnouncementComment> comments = new ArrayList<>();
            while (resultSet.next()) {
                AnnouncementComment comment = new AnnouncementCommentImpl(
                        BigInteger.valueOf(resultSet.getLong(ID_COMMENTARY)),
                        resultSet.getString(DESCRIPTION),
                        resultSet.getString(FIRST_NAME) + " " + resultSet.getString(LAST_NAME),
                        resultSet.getTime(DATE_CREATE)
                        );
                comments.add(comment);
            }
            return comments;
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public void createComment(String commentContent, BigInteger announcementId, BigInteger userId) throws DAOLogicException {
        try (PreparedStatement preparedStatement
                     = connection.prepareStatement(properties.getProperty(CREATE_COMMENTARY))){
            preparedStatement.setLong(1, announcementId.longValue());
            preparedStatement.setLong(2, userId.longValue());
            preparedStatement.setString(3, commentContent);
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public Page<Announcement> getAnnouncementsByPage(BigInteger idUser, Pageable pageable)
            throws DAOLogicException {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(properties.getProperty(SELECT_ANNOUNCEMENTS_BY_PAGE))) {
            preparedStatement.setLong(1, idUser.longValue());
            preparedStatement.setLong(2, pageable.getOffset());
            preparedStatement.setInt(3, pageable.getPageSize());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Announcement> announcements = new ArrayList<>();
            while (resultSet.next()) {
                Announcement announcement = new AnnouncementImpl.AnnouncementBuilder()
                        .setId(BigInteger.valueOf(resultSet.getLong(ID_ANNOUNCEMENT)))
                        .setTitle(resultSet.getString(TITLE))
                        .setDescription(resultSet.getString(DESCRIPTION))
                        .setUser(new UserImpl.UserBuilder()
                                .setId(BigInteger.valueOf(resultSet.getLong(OWNER)))
                                .setFirstName(resultSet.getString(FIRST_NAME))
                                .setLastName(resultSet.getString(LAST_NAME))
                                .build())
                        .setDate(resultSet.getDate(DATE_CREATE))
                        .setAddress(resultSet.getString(ADDRESS))
                        .setParticipantsCap(resultSet.getInt(LIKES))
                        .setBlank(BigInteger.valueOf(resultSet.getLong(COLUMN_IS_LIKED)))
                        .setIsLiked(!resultSet.wasNull())
                        .build();
                announcements.add(announcement);
            }
            return new PageImpl<>(announcements, pageable, countOfAnnouncements());
        } catch (SQLException | AnnouncementException | UserException e) {
            log.error(DAO_LOGIC_EXCEPTION);
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION + e.getMessage());
        }
    }

    @Override
    public long countOfAnnouncements() throws DAOLogicException {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(properties.getProperty(SELECT_NUMBER_OF_ANNOUNCEMENT))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION);
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION + e.getMessage());
        }
    }

    @Override
    public Page<Announcement> getAnnouncementsByTitle(String title, BigInteger idUser, Pageable pageable)
            throws DAOLogicException {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(properties.getProperty(SELECT_ANNOUNCEMENTS_BY_TITLE))){
            preparedStatement.setLong(1, idUser.longValue());
            preparedStatement.setString(2, "%" + title + "%");
            preparedStatement.setLong(3, pageable.getOffset());
            preparedStatement.setInt(4, pageable.getPageSize());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Announcement> announcements = new ArrayList<>();

            while (resultSet.next()) {
                Announcement announcement = new AnnouncementImpl.AnnouncementBuilder()
                        .setId(BigInteger.valueOf(resultSet.getLong(ID_ANNOUNCEMENT)))
                        .setTitle(resultSet.getString(TITLE))
                        .setDescription(resultSet.getString(DESCRIPTION))
                        .setUser(new UserImpl.UserBuilder()
                                .setId(BigInteger.valueOf(resultSet.getLong(OWNER)))
                                .setFirstName(resultSet.getString(FIRST_NAME))
                                .setLastName(resultSet.getString(LAST_NAME))
                                .build())
                        .setDate(resultSet.getDate(DATE_CREATE))
                        .setAddress(resultSet.getString(ADDRESS))
                        .setParticipantsCap(resultSet.getInt(LIKES))
                        .setBlank(BigInteger.valueOf(resultSet.getLong(COLUMN_IS_LIKED)))
                        .setIsLiked(!resultSet.wasNull())
                        .build();
                announcements.add(announcement);
            }
            return new PageImpl<>(announcements, pageable, countOfAnnouncementsByTitle(title));
        } catch (SQLException | AnnouncementException | UserException e) {
            log.error(DAO_LOGIC_EXCEPTION + e.getMessage());
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION, e);
        }
    }

    @Override
    public long countOfAnnouncementsByTitle(String title) throws DAOLogicException {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(properties.getProperty(SELECT_NUMBER_OF_ANNOUNCEMENT_BY_TITLE))) {
            preparedStatement.setString(1, "%" + title + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            log.error(DAO_LOGIC_EXCEPTION);
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION + e.getMessage());
        }
    }
}
