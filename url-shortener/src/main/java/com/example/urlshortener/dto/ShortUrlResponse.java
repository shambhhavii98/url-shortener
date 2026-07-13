package com.example.urlshortener.dto;

import java.time.LocalDateTime;

/** What we send back to the client after creating/looking up a short link. */
public record ShortUrlResponse(
        String shortCode,
        String shortUrl,
        String originalUrl,
        LocalDateTime createdAt,
        LocalDateTime expirationDate,
        Integer maxClicks,
        int clickCount,
        boolean passwordProtected,
        boolean active
) {}
