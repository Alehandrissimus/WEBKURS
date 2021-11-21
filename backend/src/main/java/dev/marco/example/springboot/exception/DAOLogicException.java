package dev.marco.example.springboot.exception;

public class DAOLogicException  extends Exception implements MessagesForException {

  public DAOLogicException(String message) {
    super(message);
  }

  public DAOLogicException(String message, Throwable cause) {
    super(message, cause);
  }
}
