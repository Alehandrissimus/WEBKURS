package dev.marco.example.springboot.exception;

public class QuizDoesNotExistException extends Exception {
    public QuizDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizDoesNotExistException(String message) {
        super(message);
    }
}
