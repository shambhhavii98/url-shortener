package com.example.urlshortener.exception;

/** Thrown when a short link has already been clicked the maximum allowed number of times. */
public class ClickLimitExceededException extends RuntimeException {
    public ClickLimitExceededException(String message) {
        super(message);
    }
}
