package dev.marco.example.springboot.exception;

public class AnswerDoesNotExistException extends Exception implements MessagesForException {

  public AnswerDoesNotExistException(String errorMessage) {
    super(errorMessage);
  }

  public AnswerDoesNotExistException(String errorMessage, Throwable error) {
    super(errorMessage, error);
  }

}
