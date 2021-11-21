package dev.marco.example.springboot.exception;

public class UserDoesNotConfirmedEmailException extends Exception implements MessagesForException {

  public UserDoesNotConfirmedEmailException(String message) {
    super(message);
  }

  public UserDoesNotConfirmedEmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
