package dev.marco.example.springboot.exception;

public class QuestionException extends Exception {

    public QuestionException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionException(String message) {
        super(message);
    }

}
