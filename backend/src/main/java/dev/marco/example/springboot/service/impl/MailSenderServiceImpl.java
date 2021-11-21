package dev.marco.example.springboot.service.impl;

import java.io.InputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import dev.marco.example.springboot.dao.UserDAO;
import dev.marco.example.springboot.exception.*;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.service.MailSenderService;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

@Service
public class MailSenderServiceImpl implements MailSenderService {

  private static final Logger log = Logger.getLogger(MailSenderServiceImpl.class);

  @Autowired
  private UserDAO userDAO;

  @Override
  public void setTestConnection() throws DAOConfigException {
    //userDAO.setTestConnection();
  }

  @Override
  public boolean sendEmail(User user) throws UserException, MailException {
    try {
      User userInDatabase = userDAO.getUserByEmail(user.getEmail());

      if (userInDatabase.isActive()) {
        log.debug(MessagesForException.ERROR_WHILE_ACTIVATING);
        throw new UserException(MessagesForException.ERROR_WHILE_ACTIVATING);
      }

      String code = generateCode();
      userDAO.updateUsersEmailCode(user.getId(), code);

      Properties properties = new Properties();
      setProperties(properties);

      String to = userInDatabase.getEmail();
      String from = properties.getProperty(HOST_EMAIL_NAME);
      String password = properties.getProperty(HOST_EMAIL_PASSWORD);

      properties = new Properties();

      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.starttls.enable", "true");
      properties.put("mail.smtp.host", "smtp.gmail.com");
      properties.put("mail.smtp.port", "587");

      Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(from, password);
        }
      });

      Message message = prepareMessage(session, from, to, EMAIL_SUBJECT, TEXT_HTML+
          String.format(TEXT_INPUT, code));
      Transport.send(message);
      return true;
    } catch (DAOLogicException | UserDoesNotExistException | MessagingException e) {
      log.error(MessagesForException.EMAIL_ERROR + e.getMessage());
      throw new MailException(MessagesForException.EMAIL_ERROR);
    }
  }

  @Override
  public Message prepareMessage(Session session, String from, String to, String subject,
      String content)
      throws MailException {
    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.setRecipient(RecipientType.TO, new InternetAddress(to));

      message.setSubject(subject);
//      message.setText("Email tesxt");
      message.setContent(content, TEXT_TYPE);
      return message;
    } catch (MessagingException e) {
      log.error(MessagesForException.MESSAGE_ERROR + e.getMessage());
      throw new MailException(MessagesForException.MESSAGE_ERROR, e);
    }
  }

  @Override
  public void setProperties(Properties properties) throws MailException {
    try (InputStream fis = MailSenderService.class.getClassLoader()
        .getResourceAsStream(PATH_PROPERTY)) {
      properties.load(fis);
    } catch (IOException e) {
      log.error(MessagesForException.PROPERTY_ERROR + e.getMessage());
      throw new MailException(MessagesForException.PROPERTY_ERROR, e);
    }
  }

  @Override
  public String generateCode() throws DAOLogicException {
    StringBuilder randString = new StringBuilder();

    while (true) {
      for (int i = 0; i < 10; i++) {
        randString.append(SYMBOLS.charAt((int) (Math.random() * SYMBOLS.length())));
      }
      try {
        userDAO.getUserByEmailCode(String.valueOf(randString));

      } catch (UserDoesNotExistException e) {
        break;
      } catch (DAOLogicException e) {
        log.error(MessagesForException.DAO_LOGIC_EXCEPTION + e.getMessage());
        throw new DAOLogicException(MessagesForException.DAO_LOGIC_EXCEPTION, e);
      }
    }

    return String.valueOf(randString);
  }

  @Override
  public User confirmEmail(String code) throws UserException, DAOLogicException {
    try {
      User userInDatabase = userDAO.getUserByEmailCode(code);

      if (!userDAO.activateUser(userInDatabase.getId())) {
        throw new UserException(MessagesForException.ERROR_WHILE_ACTIVATING);
      }
      userDAO.updateUsersEmailCode(userInDatabase.getId(), null);

      return userDAO.getUserById(userInDatabase.getId());
    } catch (UserDoesNotExistException | UserException e) {
      log.error(MessagesForException.ERROR_WHILE_CONFIRMING_EMAIL + e.getMessage());
      throw new UserException(MessagesForException.ERROR_WHILE_CONFIRMING_EMAIL, e);
    } catch (DAOLogicException e) {
      log.error(MessagesForException.DAO_LOGIC_EXCEPTION + e.getMessage());
      throw new DAOLogicException(MessagesForException.DAO_LOGIC_EXCEPTION, e);
    }
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder(2 * hash.length);
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if(hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  @Override
  public boolean generateNewPassword(String email) throws MailException {
    try {
      log.debug("Starting to generateNewPassword() with email=" + email);

      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      String newPassword = generateCode();
      byte[] encodedhash = digest.digest(
              newPassword.getBytes(StandardCharsets.UTF_8));
      String hashPassword = bytesToHex(encodedhash);


      userDAO.updateUsersPassword(userDAO.getUserByEmail(email).getId(), encoder.encode(hashPassword));

      Properties properties = new Properties();
      setProperties(properties);

      String to = email;
      String from = properties.getProperty(HOST_EMAIL_NAME);
      String password = properties.getProperty(HOST_EMAIL_PASSWORD);

      properties = new Properties();

      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.starttls.enable", "true");
      properties.put("mail.smtp.host", "smtp.gmail.com");
      properties.put("mail.smtp.port", "587");

      Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(from, password);
        }
      });

      Message message = prepareMessage(session, from, to, PASSWORD_SUBJECT,
          String.format(TEXT_PASSWORD, newPassword));
      Transport.send(message);
      log.debug("Password was successfully recovered");
      return true;
    } catch (DAOLogicException | UserDoesNotExistException | MessagingException | NoSuchAlgorithmException e) {
      log.error(MessagesForException.ERROR_WHILE_RECOVERING_PASSWORD + e.getMessage());
      throw new MailException(MessagesForException.ERROR_WHILE_RECOVERING_PASSWORD, e);
    }
  }
}
