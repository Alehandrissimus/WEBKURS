package dev.marco.example.springboot.service;

import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.Dashboard;

import java.math.BigInteger;

public interface DashboardService {

    Dashboard generateDashboard(BigInteger id) throws DAOLogicException, QuizDoesNotExistException, AnnouncementDoesNotExistException, AnnouncementException, UserDoesNotExistException;

    void setTestConnection() throws DAOConfigException;
}
