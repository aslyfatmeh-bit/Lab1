package com.example.lab2;

import java.util.concurrent.ConcurrentHashMap;

public class AttemptTracker {

    // Store the state of each user by email
    private final ConcurrentHashMap<String, UserState> users = new ConcurrentHashMap<>();

    // Maximum failed attempts and blocking duration
    private final int n;
    private final int t;
    public AttemptTracker(int n, int t) {
        if (n < 1 || t < 1) {
            throw new IllegalArgumentException(
                    "n and t must be at least 1");
        }
        this.n = n;
        this.t = t;
    }

    // Get or create the state for an existing user
    private UserState getState(String email) {
        return users.computeIfAbsent(
                email, key -> new UserState());
    }

    // Check whether the user is blocked
    public boolean isBlocked(String email) {
        return getState(email).isBlocked();
    }

    // Get the remaining blocking time
    public long getRemainingSeconds(String email) {
        return getState(email).getRemainingSeconds();
    }

    // Record a failed login attempt
    public int recordFailure(String email) {
        return getState(email).recordFailure(n, t);
    }

    // Reset attempts after successful login
    public void reset(String email) {
        getState(email).reset();
    }

    // Get the maximum number of attempts
    public int getMaxAttempts() {
        return n;
    }
}
