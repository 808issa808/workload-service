package org.epam.security;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    public static final int MAX_ATTEMPTS = 3;
    public static final long BLOCK_DURATION_MINUTES = 5;
    private final ConcurrentHashMap<String, LoginAttempt> attemptsCache = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        LoginAttempt attempt = attemptsCache.getOrDefault(username, new LoginAttempt());
        attempt.incrementAttempts();
        attempt.setLastAttemptTime(System.currentTimeMillis());
        attemptsCache.put(username, attempt);
    }

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
    }

    public boolean isBlocked(String username) {
        LoginAttempt attempt = attemptsCache.get(username);
        if (attempt == null) {
            return false;
        }

        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            long blockTime = TimeUnit.MINUTES.toMillis(BLOCK_DURATION_MINUTES);
            if (System.currentTimeMillis() - attempt.getLastAttemptTime() < blockTime) {
                return true;
            } else {
                attemptsCache.remove(username);
                return false;
            }
        }
        return false;
    }
}

@Data
class LoginAttempt {
    private int attempts;
    private long lastAttemptTime;

    public void incrementAttempts() {
        attempts++;
    }
}