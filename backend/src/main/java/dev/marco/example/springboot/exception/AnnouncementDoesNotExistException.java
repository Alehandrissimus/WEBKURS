package dev.marco.example.springboot.exception;

public class AnnouncementDoesNotExistException extends Exception implements MessagesForException {

  public AnnouncementDoesNotExistException(String errorMessage, Throwable error) {
    super(errorMessage, error);
  }

  public AnnouncementDoesNotExistException(String errorMessage) {
    super(errorMessage);
  }
}