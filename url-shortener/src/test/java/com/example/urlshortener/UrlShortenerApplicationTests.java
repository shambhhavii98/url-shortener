package com.example.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Runs against the H2 in-memory profile so this test works with no external database.
@SpringBootTest
@ActiveProfiles("h2")
class UrlShortenerApplicationTests {

    @Test
    void contextLoads() {
        // If the Spring context starts up without errors, this test passes.
    }
}
