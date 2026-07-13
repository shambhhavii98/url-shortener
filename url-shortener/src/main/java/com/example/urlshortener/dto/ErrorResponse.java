package com.example.urlshortener.dto;

import java.time.LocalDateTime;

/** Standard JSON shape returned for every error, so API clients can handle them consistently. */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}
