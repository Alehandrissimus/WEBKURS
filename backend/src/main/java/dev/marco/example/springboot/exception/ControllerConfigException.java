package dev.marco.example.springboot.exception;

public class ControllerConfigException extends Exception implements MessagesForException {

    public ControllerConfigException(String message) {
        super(message);
    }

    public ControllerConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
