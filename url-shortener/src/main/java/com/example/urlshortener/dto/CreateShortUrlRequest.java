package com.example.urlshortener.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * What the client sends when creating a new short link.
 * All fields except originalUrl are optional.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShortUrlRequest {

    @NotBlank(message = "originalUrl is required")
    @Pattern(regexp = "^https?://.+", message = "originalUrl must start with http:// or https://")
    private String originalUrl;

    /** Optional custom short code, e.g. "my-sale" instead of a random one. */
    @Size(min = 3, max = 20, message = "customAlias must be between 3 and 20 characters")
    private String customAlias;

    /** Optional: link stops working after this date/time (must be in the future). */
    @Future(message = "expirationDate must be in the future")
    private LocalDateTime expirationDate;

    /** Optional: link stops working after this many clicks. */
    @Min(value = 1, message = "maxClicks must be at least 1")
    private Integer maxClicks;

    /** Optional: if set, visitors must provide this password to be redirected. */
    @Size(min = 4, message = "password must be at least 4 characters")
    private String password;
}
