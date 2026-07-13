package com.example.urlshortener.util;

/**
 * Very simple User-Agent parser. It is NOT a full-blown library, just enough
 * to bucket clicks into "Chrome / Firefox / Safari / Edge / Opera / Other"
 * for basic analytics.
 */
public final class BrowserDetector {

    private BrowserDetector() {
    }

    public static String detect(String userAgent) {
        if (userAgent == null || userAgent.isBlank()) {
            return "Unknown";
        }
        String ua = userAgent.toLowerCase();

        if (ua.contains("edg/")) {
            return "Edge";
        }
        if (ua.contains("opr/") || ua.contains("opera")) {
            return "Opera";
        }
        if (ua.contains("chrome/")) {
            return "Chrome";
        }
        if (ua.contains("firefox/")) {
            return "Firefox";
        }
        if (ua.contains("safari/")) {
            return "Safari";
        }
        if (ua.contains("msie") || ua.contains("trident/")) {
            return "Internet Explorer";
        }
        return "Other";
    }
}
