package dev.marco.example.springboot.util;

import dev.marco.example.springboot.exception.MessagesForException;
import org.apache.log4j.Logger;
import dev.marco.example.springboot.exception.DAOConfigException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DAOUtil {

  private static final String PATH_PROPERTY = "sqlScripts.properties";
  private static final String DRIVER_PATH_PROPERTY = "oracle.jdbc.OracleDriver";

  private static final Logger log = Logger.getLogger(DAOUtil.class);

  private DAOUtil() {
  }

  public static Connection getDataSource(String URL, String USERNAME, String PASSWORD, Properties properties)
      throws  DAOConfigException {

    try (InputStream fis = DAOUtil.class.getClassLoader().getResourceAsStream(PATH_PROPERTY)) {
      Class.forName(DRIVER_PATH_PROPERTY);
      properties.load(fis);

      return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    } catch (Exception e){
      log.error(MessagesForException.DAO_CONFIG_EXCEPTION + e.getMessage());
      throw new DAOConfigException(MessagesForException.DAO_CONFIG_EXCEPTION,e);
    }
  }

}
