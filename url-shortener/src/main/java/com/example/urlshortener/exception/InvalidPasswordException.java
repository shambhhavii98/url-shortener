package com.example.urlshortener.exception;

/** Thrown when a password-protected link is accessed with a missing or wrong password. */
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
