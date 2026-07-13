package com.example.urlshortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one shortened link: the mapping between a short code
 * (e.g. "aZ3xQ1p") and the original long URL, plus all the rules
 * that control whether it still works (expiration date, click limit,
 * password).
 */
@Entity
@Table(name = "url_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String shortCode;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Optional: link stops working after this point in time. */
    private LocalDateTime expirationDate;

    /** Optional: link stops working after this many successful clicks. */
    private Integer maxClicks;

    @Column(nullable = false)
    @Builder.Default
    private Integer clickCount = 0;

    /** BCrypt-hashed password. Null/blank means "no password required". */
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "urlMapping", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ClickEvent> clickEvents = new ArrayList<>();
}
