package com.example.urlshortener.exception;

/** Thrown when a short code does not exist in the database at all. */
public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String message) {
        super(message);
    }
}
