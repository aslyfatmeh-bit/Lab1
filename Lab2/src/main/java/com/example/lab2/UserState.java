package com.example.lab2;

public class UserState {

    private int failedAttempts = 0;
    private long blockedUntil = 0;

    public synchronized boolean isBlocked() {
        if (blockedUntil != 0 &&
                System.currentTimeMillis() >= blockedUntil) {
            failedAttempts = 0;
            blockedUntil = 0;
        }

        return System.currentTimeMillis() < blockedUntil;
    }

    public synchronized long getRemainingSeconds() {
        long remaining = blockedUntil - System.currentTimeMillis();
        return Math.max(0, (remaining + 999) / 1000);
    }

    public synchronized int recordFailure(int n, int t) {
        if (isBlocked()) {
            return 0;
        }

        failedAttempts++;

        if (failedAttempts >= n) {
            blockedUntil = System.currentTimeMillis() + t * 1000L;
            return 0;
        }

        return n - failedAttempts;
    }

    public synchronized void reset() {
        failedAttempts = 0;
        blockedUntil = 0;
    }
}
