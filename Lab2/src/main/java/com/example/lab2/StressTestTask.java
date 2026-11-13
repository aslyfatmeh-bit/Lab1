package com.example.lab2;

import java.util.ArrayList;
import java.util.List;

public class StressTestTask {

    public static String runTest() {

        final int n = 10000;
        final int t = 60;
        final String email = "stress@test.com";

        AttemptTracker tracker = new AttemptTracker(n, t);

        List<Thread> threads = new ArrayList<>();

        // 20 threads, 499 failures each
        for (int i = 0; i < 20; i++) {

            Thread thread = new Thread(() -> {
                for (int j = 0; j < 499; j++) {
                    tracker.recordFailure(email);
                }
            });

            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Test interrupted";
            }
        }

        boolean blockedAfter9980 =
                tracker.isBlocked(email);

        // Add 19 more failed attempts
        for (int i = 0; i < 19; i++) {
            tracker.recordFailure(email);
        }

        boolean blockedAfter9999 =
                tracker.isBlocked(email);

        // The 10000th failure
        tracker.recordFailure(email);

        boolean blockedAfter10000 =
                tracker.isBlocked(email);

        boolean passed =
                !blockedAfter9980
                        && !blockedAfter9999
                        && blockedAfter10000;

        return "Stress Test Results:\n"
                + "20 threads x 499 = 9980 attempts\n"
                + "Blocked after 9980: "
                + blockedAfter9980 + "\n"
                + "Blocked after 9999: "
                + blockedAfter9999 + "\n"
                + "Blocked after 10000: "
                + blockedAfter10000 + "\n"
                + (passed ? "TEST PASSED" : "TEST FAILED");
    }
}
