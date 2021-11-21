package dev.marco.example.springboot.service.impl;

import dev.marco.example.springboot.service.UserService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Dashboard;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.impl.UserImpl;
import dev.marco.example.springboot.service.DashboardService;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static dev.marco.example.springboot.exception.MessagesForException.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class DashboardServiceImplTest {

    @Autowired
    private DashboardService dashboardService;

    private static final Logger log = Logger.getLogger(DashboardServiceImplTest.class);

    @Autowired
    private void setTestConnection(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
        try {
            dashboardService.setTestConnection();
        } catch (DAOConfigException e) {
            log.error(ERROR_WHILE_SETTING_TEST_CONNECTION + e.getMessage());
        }
    }

    @Test
    @Timeout(value = 10000, unit = TimeUnit.MILLISECONDS)
    void generateDashboardTest() throws DAOLogicException {
        try {
            User user = new UserImpl.UserBuilder()
                    .setId(BigInteger.valueOf(1))
                    .build();

            Dashboard dashboard = dashboardService.generateDashboard(user.getId());
            assertNotNull(dashboard);
        } catch (DAOLogicException e) {
            log.error(DAO_LOGIC_EXCEPTION + " in generateDashboard");
            throw new DAOLogicException(DAO_LOGIC_EXCEPTION);
        } catch (AnnouncementException | AnnouncementDoesNotExistException e) {
            log.error(ANNOUNCEMENT_NOT_FOUND_EXCEPTION + " in generateDashboard");
            throw new DAOLogicException(ANNOUNCEMENT_NOT_FOUND_EXCEPTION);
        } catch (QuizDoesNotExistException e) {
            log.error(QUIZ_NOT_FOUND_EXCEPTION + " in generateDashboard");
            throw new DAOLogicException(QUIZ_NOT_FOUND_EXCEPTION);
        } catch (UserException | UserDoesNotExistException e) {
            log.error(USER_NOT_FOUND_EXCEPTION + " in generateDashboard");
            throw new DAOLogicException(USER_NOT_FOUND_EXCEPTION);
        }
    }

}
