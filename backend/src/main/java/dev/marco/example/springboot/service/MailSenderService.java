package dev.marco.example.springboot.service;

import dev.marco.example.springboot.exception.DAOConfigException;
import dev.marco.example.springboot.exception.DAOLogicException;
import dev.marco.example.springboot.exception.MailException;
import dev.marco.example.springboot.exception.UserException;
import dev.marco.example.springboot.model.User;

import javax.mail.Message;
import javax.mail.Session;
import java.util.Properties;

public interface MailSenderService {

  String SYMBOLS = "1234567890qwertyuiopasdfghjklzxcvbnmQWERRTYUIOPASDFGHJKL";
  String EMAIL_SUBJECT = "Email confirmation for quiz";
  String PASSWORD_SUBJECT = "Recover password";
  String TEXT_PASSWORD = "New password is: %s";

  String TEXT_HTML = "<html>\n"
      + "<head>\n"
      + "<style>\n"
      + "   #content {\n"
      + "    width: 500px; /* Ширина блока */\n"
      + "    margin: 0 auto 50px; /* Выравнивание блока по центру */\n"
      + "  }\n"
      + "   #footer {\n"
      + "    position: fixed; /* Фиксированное положение футера (подвала)*/\n"
      + "    left: 0; bottom: 0; /* Левый нижний угол */\n"
      + "    padding: 10px; /* Поля вокруг текста */\n"
      + "    width: 100%; /* Ширина слоя */\n"
      + "    margin: 0 auto 50px; /* Выравнивание блока по центру */\n"
      + "  }\n"
      + "</style>\n"
      + "</head>\n"
      + "<body>\n"
      + "<div id=\"content\">\n"
      ;

  String TEXT_INPUT = "<form action=\"http://localhost:8080/confirm?code=%s\" method=\"post\">\n"
      + "<button type=\"submit\">Confirm email</button></form>\n"
      + "</div>\n"
      + "<div id=\"footer\">\n"
      + "      Do not reply to this email if you received it by accident\n"
      + "</div>\n"
      + "</body>\n"
      + "</html>";

  String TEXT_TYPE = "text/html";
  String PATH_PROPERTY = "email.properties";
  String HOST_EMAIL_NAME = "HOST_EMAIL_NAME";
  String HOST_EMAIL_PASSWORD = "HOST_EMAIL_PASSWORD";

  boolean sendEmail(User user) throws UserException, MailException;

  String generateCode() throws DAOLogicException;

  User confirmEmail(String code) throws UserException, DAOLogicException;

  boolean generateNewPassword(String email) throws MailException;

  void setProperties(Properties properties) throws MailException;

  Message prepareMessage(Session session, String from, String to, String subject,
      String content)
      throws MailException;

  void setTestConnection() throws DAOConfigException;
}
