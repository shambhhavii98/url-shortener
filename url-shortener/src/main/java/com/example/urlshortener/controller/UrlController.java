package com.example.urlshortener.controller;

import com.example.urlshortener.dto.AnalyticsResponse;
import com.example.urlshortener.dto.ClickEventResponse;
import com.example.urlshortener.dto.CreateShortUrlRequest;
import com.example.urlshortener.dto.ShortUrlResponse;
import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.service.QrCodeService;
import com.example.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * Management API: create short links, inspect them, view analytics, get a QR code.
 * The actual redirect (visiting the short link) lives in {@link RedirectController}.
 */
@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerService urlShortenerService;
    private final QrCodeService qrCodeService;

    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(@Valid @RequestBody CreateShortUrlRequest request) {
        UrlMapping mapping = urlShortenerService.createShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(mapping));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<ShortUrlResponse> getUrlInfo(@PathVariable String shortCode) {
        UrlMapping mapping = urlShortenerService.getByShortCode(shortCode);
        return ResponseEntity.ok(toResponse(mapping));
    }

    @GetMapping("/{shortCode}/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(@PathVariable String shortCode) {
        UrlMapping mapping = urlShortenerService.getByShortCode(shortCode);

        List<ClickEventResponse> clicks = mapping.getClickEvents().stream()
                .map(e -> new ClickEventResponse(e.getClickedAt(), e.getIpAddress(), e.getUserAgent(), e.getBrowser()))
                .sorted(Comparator.comparing(ClickEventResponse::clickedAt).reversed())
                .toList();

        AnalyticsResponse response = new AnalyticsResponse(
                mapping.getShortCode(),
                mapping.getOriginalUrl(),
                mapping.getClickCount(),
                clicks
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{shortCode}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrCode(@PathVariable String shortCode,
                                             @RequestParam(defaultValue = "300") int size) throws Exception {
        UrlMapping mapping = urlShortenerService.getByShortCode(shortCode);
        String fullShortUrl = urlShortenerService.buildShortUrl(mapping.getShortCode());
        byte[] qrImage = qrCodeService.generateQrCodePng(fullShortUrl, size);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrImage);
    }

    private ShortUrlResponse toResponse(UrlMapping mapping) {
        return new ShortUrlResponse(
                mapping.getShortCode(),
                urlShortenerService.buildShortUrl(mapping.getShortCode()),
                mapping.getOriginalUrl(),
                mapping.getCreatedAt(),
                mapping.getExpirationDate(),
                mapping.getMaxClicks(),
                mapping.getClickCount(),
                StringUtils.hasText(mapping.getPassword()),
                mapping.isActive()
        );
    }
}
