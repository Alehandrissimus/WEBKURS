package dev.marco.example.springboot.service.impl;

import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Announcement;
import dev.marco.example.springboot.model.Dashboard;
import dev.marco.example.springboot.model.Quiz;
import dev.marco.example.springboot.model.impl.DashboardImpl;
import dev.marco.example.springboot.service.AnnouncementService;
import dev.marco.example.springboot.service.DashboardService;
import dev.marco.example.springboot.service.QuizService;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import static dev.marco.example.springboot.exception.MessagesForException.QUIZ_NOT_FOUND_EXCEPTION;
import static dev.marco.example.springboot.exception.MessagesForException.USER_NOT_FOUND_EXCEPTION;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final QuizService quizService;
    private final AnnouncementService announcementService;
    private final UserService userService;
    private static final Logger log = Logger.getLogger(DashboardServiceImpl.class);

    private static final int DASHBOARD_COUNT_POPULAR_ANNOUNCEMENT = 2;
    private static final int DASHBOARD_COUNT_LAST_CREATED_QUIZZES = 3;

    @Autowired
    public DashboardServiceImpl(QuizService quizService, AnnouncementService announcementService,
                                UserService userService) {
        this.quizService = quizService;
        this.announcementService = announcementService;
        this.userService = userService;
    }

    @Override
    public void setTestConnection() throws DAOConfigException {
        quizService.setTestConnection();
        announcementService.setTestConnection();
        userService.setTestConnection();
    }

    @Override
    public Dashboard generateDashboard(BigInteger id) throws DAOLogicException, QuizDoesNotExistException,
            AnnouncementDoesNotExistException, AnnouncementException, UserDoesNotExistException {

        User user = userService.getUserById(id);
        if (user == null) {
            log.error(USER_NOT_FOUND_EXCEPTION);
            throw new UserDoesNotExistException(USER_NOT_FOUND_EXCEPTION);
        }

        List<Quiz> lastQuizzes = quizService.getLastCreatedQuizzes(DASHBOARD_COUNT_LAST_CREATED_QUIZZES);

        if (lastQuizzes.isEmpty()) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + lastQuizzes);
            throw new QuizDoesNotExistException(QUIZ_NOT_FOUND_EXCEPTION);
        }

        List<Announcement> popularAnnouncement =
                announcementService.getPopularAnnouncements(DASHBOARD_COUNT_POPULAR_ANNOUNCEMENT, id);

        Set<Announcement> likedAnnouncement = announcementService.getAnnouncementsLikedByUser(id);

        return new DashboardImpl(lastQuizzes, popularAnnouncement, likedAnnouncement);
    }

}
