package com.example.urlshortener.exception;

/** Thrown when a short link exists but is no longer valid (past its expiration date, or deactivated). */
public class UrlExpiredException extends RuntimeException {
    public UrlExpiredException(String message) {
        super(message);
    }
}
