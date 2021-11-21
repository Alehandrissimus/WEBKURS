package dev.marco.example.springboot.exception;

public class PageException  extends Exception implements MessagesForException {

    public PageException(String message) {
        super(message);
    }

    public PageException(String message, Throwable cause) {
        super(message, cause);
    }
}
