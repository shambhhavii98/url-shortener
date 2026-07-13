package com.example.urlshortener.dto;

import java.util.List;

/** Full analytics payload for one short link: total clicks + individual click events. */
public record AnalyticsResponse(
        String shortCode,
        String originalUrl,
        int totalClicks,
        List<ClickEventResponse> clicks
) {}
