package dev.marco.example.springboot.util;

import dev.marco.example.springboot.exception.ControllerConfigException;
import dev.marco.example.springboot.exception.DAOConfigException;
import dev.marco.example.springboot.exception.MessagesForException;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class ControllerUtil {
    private static final String PATH_PROPERTY = "exception.properties";

    private static final Logger log = Logger.getLogger(ControllerUtil.class);

    private ControllerUtil() {}

    public static void getProperty(Properties properties)
            throws ControllerConfigException {
        try (InputStream fis = DAOUtil.class.getClassLoader().getResourceAsStream(PATH_PROPERTY)) {
            properties.load(fis);
        } catch (Exception e){
            log.error(MessagesForException.CONTROLLER_CONFIG_EXCEPTION + e.getMessage());
            throw new ControllerConfigException(MessagesForException.CONTROLLER_CONFIG_EXCEPTION,e);
        }
    }
}

