package dev.marco.example.springboot.exception;

public class JwtAuthenticationException extends Exception {

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}
