package com.example.urlshortener.dto;

import java.time.LocalDateTime;

/** One row of click analytics. */
public record ClickEventResponse(
        LocalDateTime clickedAt,
        String ipAddress,
        String userAgent,
        String browser
) {}
