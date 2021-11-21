package dev.marco.example.springboot.model.impl;

import dev.marco.example.springboot.exception.MessagesForException;
import dev.marco.example.springboot.exception.UserException;
import dev.marco.example.springboot.model.Quiz;
import dev.marco.example.springboot.model.User;
import dev.marco.example.springboot.model.UserRoles;

import dev.marco.example.springboot.service.impl.UserServiceImpl;
import java.math.BigInteger;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

import static dev.marco.example.springboot.exception.MessagesForException.*;

public class UserImpl implements User {

  private BigInteger id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private UserRoles role;
  private String description;
  private boolean active;
  private Set<Quiz> favoriteQuizes;
  private Set<Quiz> accomplishedQuizes;
  private String emailCode;

  private static final Logger log = Logger.getLogger(UserImpl.class);

  private UserImpl() {

  }

  @Override
  public Set<Quiz> getAccomplishedQuizes() {
    return accomplishedQuizes;
  }

  @Override
  public Set<Quiz> getFavoriteQuizes() {
    return favoriteQuizes;
  }

  @Override
  public void addFavoriteQuiz(Quiz quiz) {
    favoriteQuizes.add(quiz);
  }

  @Override
  public void addAccomplishedQuiz(Quiz quiz) {
    accomplishedQuizes.add(quiz);
  }

  @Override
  public void removeFavoriteQuiz(Quiz quiz) {
    favoriteQuizes.remove(quiz);
  }

  @Override
  public void setFavoriteQuizes(Set<Quiz> favoriteQuizes) {
    this.favoriteQuizes = favoriteQuizes;
  }

  @Override
  public void setAccomplishedQuizes(Set<Quiz> accomplishedQuizes) {
    this.accomplishedQuizes = accomplishedQuizes;
  }

  @Override
  public void setId(BigInteger id) {
    this.id = id;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public void setRole(UserRoles role) {
    this.role = role;
  }

  @Override
  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public String getDescription() {
    return description;
  }


  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public BigInteger getId() {
    return id;
  }

  @Override
  public String getFullName() {
    return lastName + " " + firstName;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isActive() {
    return active;
  }

  @Override
  public UserRoles getUserRole() {
    return role;
  }

  @Override
  public String getEmailCode() {
    return emailCode;
  }

  @Override
  public void setEmailCode(String emailCode) {
    this.emailCode = emailCode;
  }

  @Override
  public String toString() {
    return "UserImpl{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", role=" + role +
            ", description='" + description + '\'' +
            ", active=" + active +
            ", favoriteQuizes=" + favoriteQuizes +
            ", accomplishedQuizes=" + accomplishedQuizes +
            ", emailCode='" + emailCode + '\'' +
            '}';
  }

  public static class UserBuilder {

    private final UserImpl newUser;

    public UserBuilder() {
      newUser = new UserImpl();
    }

    public UserBuilder setId(BigInteger id) throws UserException {
      if (id == null) {
        log.error(EMPTY_USER_ID);
        throw new UserException(EMPTY_USER_ID);
      }
      newUser.id = id;
      log.debug("Id=" + id + " was added in user builder");
      return this;
    }

    public UserBuilder setFirstName(String firstName) throws UserException {
      if (firstName == null) {
        log.error(INVALID_USERS_FIRST_NAME);
        throw new UserException(NULL_FIRST_NAME);
      }
      if (firstName.isBlank() || firstName.length() < 3) {
        log.error(INVALID_USERS_FIRST_NAME);
        throw new UserException(EMPTY_FIRST_NAME);
      }
      newUser.firstName = firstName;
      log.debug("First name=" + firstName + " was added in user builder");
      return this;
    }

    public UserBuilder setLastName(String lastName) throws UserException {
      if (lastName == null) {
        log.error(INVALID_USERS_LAST_NAME);
        throw new UserException(NULL_LAST_NAME);
      }
      if (lastName.isBlank() || lastName.length() < 3) {
        log.error(INVALID_USERS_LAST_NAME);
        throw new UserException(EMPTY_LAST_NAME);
      }
      newUser.lastName = lastName;
      log.debug("Last name=" + lastName + " was added in user builder");
      return this;
    }

    public UserBuilder setEmail(String email) throws UserException {
      if (email == null) {
        log.error(INVALID_USERS_EMAIL);
        throw new UserException(NULL_EMAIL);
      }
      if (email.isBlank()) {
        log.error(INVALID_USERS_EMAIL);
        throw new UserException(EMPTY_EMAIL);
      }
      Pattern pattern = Pattern.compile(EMAIL_PATTERN);
      Matcher matcher = pattern.matcher(email);

      if (!matcher.find()) {
        log.error(INVALID_USERS_EMAIL + " with email=" + email + " in user builder");
        throw new UserException(INVALID_USERS_EMAIL + " mail: " + email);
      }
      newUser.email = email;
      log.debug("Email=" + email + " was added in user builder");
      return this;
    }

    public UserBuilder setPassword(String password) throws UserException {
      if (password == null) {
        throw new UserException(NULL_PASSWORD);
      }
      if (password.isBlank() || password.length() < 8) {
        throw new UserException(EMPTY_PASSWORD);
      }
      newUser.password = password;
      return this;
    }

    public UserBuilder setRole(UserRoles role) {
      newUser.role = role;
      return this;
    }

    public UserBuilder setDescription(String description) {
      newUser.description = description;
      return this;
    }

    public UserBuilder setActive(boolean active) {
      newUser.active = active;
      return this;
    }

    public UserBuilder setFavoriteQuizes(Set<Quiz> favoriteQuizes) {
      newUser.favoriteQuizes = favoriteQuizes;
      return this;
    }

    public UserBuilder setAccomplishedQuizes(Set<Quiz> accomplishedQuizes) {
      newUser.accomplishedQuizes = accomplishedQuizes;
      return this;
    }

    public UserBuilder setEmailCode(String emailCode) {
      newUser.emailCode = emailCode;
      return this;
    }

    public User build() {
      return newUser;
    }

  }

}
