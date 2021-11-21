package dev.marco.example.springboot.model;

import java.math.BigInteger;
import java.util.Set;

public interface User {

  BigInteger getId();

  String getFullName();

  String getFirstName();

  String getLastName();

  String getEmail();

  String getPassword();

  boolean isActive();

  String getEmailCode();

  void setEmailCode(String email);

  Set<Quiz> getAccomplishedQuizes();

  Set<Quiz> getFavoriteQuizes();

  UserRoles getUserRole();

  public void setId(BigInteger id);

  public void setFirstName(String firstName);

  public void setLastName(String lastName);

  public void setEmail(String email);

  public void setPassword(String password);

  public void setRole(UserRoles role);

  public void setActive(boolean active);

  public void setFavoriteQuizes(Set<Quiz> favoriteQuizes);

  public void setAccomplishedQuizes(Set<Quiz> accomplishedQuizes);

  String getDescription();

  void setDescription(String description);

  void addFavoriteQuiz(Quiz quiz);

  void addAccomplishedQuiz(Quiz quiz);

  void removeFavoriteQuiz(Quiz quiz);

  String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
      "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
}
