package org.epam.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {

    private LoginAttemptService loginAttemptService;
    private static final String TEST_USERNAME = "test.user";
    private static final String ANOTHER_USERNAME = "another.user";

    @BeforeEach
    void setUp() {
        loginAttemptService = new LoginAttemptService();
    }

    @Test
    void loginFailed_shouldIncrementAttempts() {
        // When
        loginAttemptService.loginFailed(TEST_USERNAME);
        loginAttemptService.loginFailed(TEST_USERNAME);

        // Then
        assertThat(loginAttemptService.isBlocked(TEST_USERNAME)).isFalse();
    }

    @Test
    void loginFailed_shouldBlockAfterMaxAttempts() {
        // When
        for (int i = 0; i < LoginAttemptService.MAX_ATTEMPTS; i++) {
            loginAttemptService.loginFailed(TEST_USERNAME);
        }

        // Then
        assertThat(loginAttemptService.isBlocked(TEST_USERNAME)).isTrue();
    }

    @Test
    void loginSucceeded_shouldResetAttempts() {
        // Given
        loginAttemptService.loginFailed(TEST_USERNAME);
        loginAttemptService.loginFailed(TEST_USERNAME);

        // When
        loginAttemptService.loginSucceeded(TEST_USERNAME);

        // Then
        assertThat(loginAttemptService.isBlocked(TEST_USERNAME)).isFalse();
    }

    @Test
    void isBlocked_shouldReturnFalseForUnknownUser() {
        assertThat(loginAttemptService.isBlocked("unknown.user")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void isBlocked_shouldReturnFalseForAttemptsBelowMax(int attemptCount) {
        // Given
        for (int i = 0; i < attemptCount; i++) {
            loginAttemptService.loginFailed(TEST_USERNAME);
        }

        // Then
        assertThat(loginAttemptService.isBlocked(TEST_USERNAME)).isFalse();
    }

    @Test
    void concurrentAccess_shouldHandleMultipleThreadsSafely() throws InterruptedException {
        // Given
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // When
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    loginAttemptService.loginFailed(TEST_USERNAME);
                    loginAttemptService.isBlocked(TEST_USERNAME);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Then
        assertThat(loginAttemptService.isBlocked(TEST_USERNAME)).isTrue();
    }

    @Test
    void shouldNotAffectOtherUsers() {
        // Given
        for (int i = 0; i < LoginAttemptService.MAX_ATTEMPTS + 1; i++) {
            loginAttemptService.loginFailed(TEST_USERNAME);
        }

        // When/Then
        assertThat(loginAttemptService.isBlocked(TEST_USERNAME)).isTrue();
        assertThat(loginAttemptService.isBlocked(ANOTHER_USERNAME)).isFalse();
    }
}