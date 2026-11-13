package com.example.lab2;

import java.util.concurrent.BlockingQueue;

public class FailureWorker extends Thread {

    // Shared queue of failed login attempts
    private final BlockingQueue<FailedAttempt> queue;

    // Tracks failed attempts for each user
    private final AttemptTracker tracker;

    public FailureWorker(BlockingQueue<FailedAttempt> queue,
                         AttemptTracker tracker) {
        this.queue = queue;
        this.tracker = tracker;

        // Daemon threads stop when the application exits
        setDaemon(true);
    }
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                // Wait for a failed login attempt
                FailedAttempt attempt = queue.take();
                String email = attempt.getEmail();
                LoginResult result;

                // Reject attempts if the account is blocked
                if (tracker.isBlocked(email)) {
                    result = LoginResult.blocked(
                            tracker.getRemainingSeconds(email));
                } else {
                    // Update the failed attempts counter
                    int remaining = tracker.recordFailure(email);

                    if (tracker.isBlocked(email)) {
                        result = LoginResult.blocked(
                                tracker.getRemainingSeconds(email));
                    } else {
                        result = LoginResult.error(remaining);
                    }
                }

                // Send the result back to LoginTask
                attempt.sendResult(result);
            } catch (InterruptedException e) {
                // Restore interrupt status and stop the worker
                interrupt();
                break;
            }
        }
    }
}
