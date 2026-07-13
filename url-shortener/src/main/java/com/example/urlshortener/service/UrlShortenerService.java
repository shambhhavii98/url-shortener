package com.example.urlshortener.service;

import com.example.urlshortener.dto.CreateShortUrlRequest;
import com.example.urlshortener.entity.ClickEvent;
import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.exception.ClickLimitExceededException;
import com.example.urlshortener.exception.InvalidPasswordException;
import com.example.urlshortener.exception.UrlExpiredException;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.repository.ClickEventRepository;
import com.example.urlshortener.repository.UrlMappingRepository;
import com.example.urlshortener.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/** Core business logic: creating short links and resolving/recording clicks. */
@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private static final int SHORT_CODE_LENGTH = 7;
    private static final int MAX_GENERATION_ATTEMPTS = 10;

    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public UrlMapping createShortUrl(CreateShortUrlRequest request) {
        String shortCode;
        if (StringUtils.hasText(request.getCustomAlias())) {
            shortCode = request.getCustomAlias().trim();
            if (urlMappingRepository.existsByShortCode(shortCode)) {
                throw new IllegalArgumentException("Custom alias already in use: " + shortCode);
            }
        } else {
            shortCode = generateUniqueShortCode();
        }

        if (request.getExpirationDate() != null && request.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("expirationDate must be in the future");
        }

        String hashedPassword = null;
        if (StringUtils.hasText(request.getPassword())) {
            hashedPassword = passwordEncoder.encode(request.getPassword());
        }

        UrlMapping mapping = UrlMapping.builder()
                .shortCode(shortCode)
                .originalUrl(request.getOriginalUrl())
                .createdAt(LocalDateTime.now())
                .expirationDate(request.getExpirationDate())
                .maxClicks(request.getMaxClicks())
                .clickCount(0)
                .password(hashedPassword)
                .active(true)
                .build();

        return urlMappingRepository.save(mapping);
    }

    private String generateUniqueShortCode() {
        String code;
        int attempts = 0;
        do {
            code = ShortCodeGenerator.generate(SHORT_CODE_LENGTH);
            attempts++;
            if (attempts > MAX_GENERATION_ATTEMPTS) {
                throw new IllegalStateException("Could not generate a unique short code, please try again");
            }
        } while (urlMappingRepository.existsByShortCode(code));
        return code;
    }

    /**
     * Validates a short code (expiration, click limit, password) and, if everything
     * checks out, records the click and returns the original long URL to redirect to.
     */
    @Transactional
    public String resolveAndRecordClick(String shortCode, String providedPassword, String ipAddress, String userAgent) {
        UrlMapping mapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("No URL found for code: " + shortCode));

        if (!mapping.isActive()) {
            throw new UrlExpiredException("This link is no longer active");
        }

        if (mapping.getExpirationDate() != null && LocalDateTime.now().isAfter(mapping.getExpirationDate())) {
            mapping.setActive(false);
            urlMappingRepository.save(mapping);
            throw new UrlExpiredException("This link expired on " + mapping.getExpirationDate());
        }

        if (mapping.getMaxClicks() != null && mapping.getClickCount() >= mapping.getMaxClicks()) {
            mapping.setActive(false);
            urlMappingRepository.save(mapping);
            throw new ClickLimitExceededException("This link has reached its maximum number of allowed clicks");
        }

        if (StringUtils.hasText(mapping.getPassword())) {
            if (!StringUtils.hasText(providedPassword) || !passwordEncoder.matches(providedPassword, mapping.getPassword())) {
                throw new InvalidPasswordException("A valid password is required to access this link");
            }
        }

        mapping.setClickCount(mapping.getClickCount() + 1);
        urlMappingRepository.save(mapping);

        ClickEvent event = ClickEvent.builder()
                .urlMapping(mapping)
                .clickedAt(LocalDateTime.now())
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .browser(com.example.urlshortener.util.BrowserDetector.detect(userAgent))
                .build();
        clickEventRepository.save(event);

        return mapping.getOriginalUrl();
    }

    public UrlMapping getByShortCode(String shortCode) {
        return urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("No URL found for code: " + shortCode));
    }

    public String buildShortUrl(String shortCode) {
        return baseUrl + "/" + shortCode;
    }
}
