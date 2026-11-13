package com.example.lab2;

public class BlockCheckThread extends Thread {

    // Tracks the blocking state of each user
    private final AttemptTracker tracker;

    // Email to check
    private final String email;

    // Result of the blocking check
    private boolean blocked;
    private long remainingSeconds;
    public BlockCheckThread(AttemptTracker tracker, String email) {
        this.tracker = tracker;
        this.email = email;
    }
    @Override
    public void run() {
        // Check if the account is currently blocked
        blocked = tracker.isBlocked(email);
        if (blocked) {
            // Get the remaining blocking time
            remainingSeconds = tracker.getRemainingSeconds(email);
        }
    }

    // Return whether the account is blocked
    public boolean isBlocked() {
        return blocked;
    }

    // Return the remaining blocking time
    public long getRemainingSeconds() {
        return remainingSeconds;
    }
}
