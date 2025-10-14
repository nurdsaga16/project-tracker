package com.sdu_ai_lab.project_tracker.security;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {
    // token -> expiration epoch millis
    private final Map<String, Long> revoked = new ConcurrentHashMap<>();

    public void revoke(String token, long expiresAtMillis) {
        if (token == null) return;
        revoked.put(token, expiresAtMillis);
    }

    public boolean isRevoked(String token) {
        if (token == null) return false;
        Long exp = revoked.get(token);
        if (exp == null) return false;
        // if already expired, we can consider it not valid (and cleanup task will remove)
        if (exp < Instant.now().toEpochMilli()) {
            revoked.remove(token);
            return false;
        }
        return true;
    }

    // periodic cleanup every 5 minutes
    @Scheduled(fixedDelay = 300_000)
    public void cleanupExpired() {
        long now = Instant.now().toEpochMilli();
        revoked.entrySet().removeIf(e -> e.getValue() < now);
    }
}
