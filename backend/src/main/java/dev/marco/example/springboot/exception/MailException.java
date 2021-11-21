package dev.marco.example.springboot.exception;

public class MailException extends Exception implements MessagesForException {

  public MailException(String message) {
    super(message);
  }

  public MailException(String message, Throwable cause) {
    super(message, cause);
  }

}
