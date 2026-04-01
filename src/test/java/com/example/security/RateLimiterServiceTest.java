package com.example.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.taskshift.security.RateLimiterService;
import com.example.taskshift.security.RateLimitException;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterServiceTest {

    private RateLimiterService rateLimiterService;

    @BeforeEach
    void setUp() {
        rateLimiterService = new RateLimiterService();
    }

    @Test
    void shouldAllowRequestsUnderLimit() {
        String ip = "192.168.1.1";
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> rateLimiterService.checkRateLimit(ip));
        }
    }

    @Test
    void shouldBlockAfterMaxAttempts() {
        String ip = "192.168.1.2";
        for (int i = 0; i < 5; i++) {
            rateLimiterService.checkRateLimit(ip);
        }
        assertThrows(RateLimitException.class,
                () -> rateLimiterService.checkRateLimit(ip));
    }

    @Test
    void shouldTrackDifferentIpsIndependently() {
        String ip1 = "10.0.0.1";
        String ip2 = "10.0.0.2";
        for (int i = 0; i < 5; i++) {
            rateLimiterService.checkRateLimit(ip1);
        }
        assertDoesNotThrow(() -> rateLimiterService.checkRateLimit(ip2));
    }
}