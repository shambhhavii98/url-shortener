package com.example.urlshortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * One recorded visit to a short link: when it happened, from which IP,
 * and what browser/user agent was used. Used to build click analytics.
 */
@Entity
@Table(name = "click_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_mapping_id", nullable = false)
    private UrlMapping urlMapping;

    @Column(nullable = false)
    private LocalDateTime clickedAt;

    private String ipAddress;

    @Column(length = 512)
    private String userAgent;

    /** Simple browser name derived from the User-Agent header (Chrome, Firefox, ...). */
    private String browser;
}
