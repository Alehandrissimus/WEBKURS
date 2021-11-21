package dev.marco.example.springboot.exception;

public class QuestionDoesNotExistException extends Exception {

  public QuestionDoesNotExistException(String errorMessage) {
    super(errorMessage);
  }

  public QuestionDoesNotExistException(String errorMessage, Throwable error) {
    super(errorMessage, error);
  }
}
