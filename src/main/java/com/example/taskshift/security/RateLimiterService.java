package com.example.taskshift.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_DURATION_MS = 15 * 60 * 1000L; // 15 min

    // IP → liste des timestamps des tentatives dans la fenêtre courante
    private final Map<String, Deque<Long>> attempts = new ConcurrentHashMap<>();

    public void checkRateLimit(String ip) {
        long now = Instant.now().toEpochMilli();
        long windowStart = now - WINDOW_DURATION_MS;

        attempts.merge(ip, new ArrayDeque<>(), (existing, empty) -> existing);
        Deque<Long> timestamps = attempts.get(ip);

        synchronized (timestamps) {
            // Supprimer les tentatives hors fenêtre
            while (!timestamps.isEmpty() && timestamps.peekFirst() < windowStart) {
                timestamps.pollFirst();
            }

            if (timestamps.size() >= MAX_ATTEMPTS) {
                long oldestAttempt = timestamps.peekFirst();
                long retryAfterSeconds = (oldestAttempt + WINDOW_DURATION_MS - now) / 1000;
                throw new RateLimitException(
                    "Trop de tentatives de connexion. Réessayez dans " 
                    + retryAfterSeconds + " secondes."
                );
            }

            timestamps.addLast(now);
        }
    }

    // Nettoyage optionnel — appelable via un @Scheduled si tu veux
    public void clearExpiredEntries() {
        long windowStart = Instant.now().toEpochMilli() - WINDOW_DURATION_MS;
        attempts.entrySet().removeIf(entry -> {
            synchronized (entry.getValue()) {
                return entry.getValue().isEmpty() || 
                       entry.getValue().peekFirst() < windowStart;
            }
        });
    }
}