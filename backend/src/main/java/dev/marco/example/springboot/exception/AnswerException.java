package dev.marco.example.springboot.exception;

public class AnswerException  extends Exception implements MessagesForException {

    public AnswerException(String message) {
        super(message);
    }

    public AnswerException(String message, Throwable cause) {
        super(message, cause);
    }
}