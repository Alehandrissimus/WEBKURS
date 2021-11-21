package dev.marco.example.springboot.exception;

public class UserDoesNotExistException extends Exception implements MessagesForException {

  public UserDoesNotExistException(String message) {
    super(message);
  }

  public UserDoesNotExistException(String message, Throwable cause) {
    super(message, cause);
  }

}
