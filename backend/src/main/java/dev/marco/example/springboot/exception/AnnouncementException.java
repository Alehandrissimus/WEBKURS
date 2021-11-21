package dev.marco.example.springboot.exception;

public class AnnouncementException extends Exception implements MessagesForException {

  public AnnouncementException(String errorMessage, Throwable error) {
    super(errorMessage, error);
  }

  public AnnouncementException(String errorMessage) {
    super(errorMessage);
  }
}
