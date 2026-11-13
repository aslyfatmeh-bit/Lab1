package com.example.lab2;

import javafx.application.Platform;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class LoginTask implements Runnable {
    private final String email;
    private final String password;
    private final List<User> users;
    private final AttemptTracker tracker;
    private final BlockingQueue<FailedAttempt> queue;
    private final Consumer<LoginResult> callback;
    public LoginTask(String email, String password,
                     List<User> users,
                     AttemptTracker tracker,
                     BlockingQueue<FailedAttempt> queue,
                     Consumer<LoginResult> callback) {
        this.email = email;
        this.password = password;
        this.users = users;
        this.tracker = tracker;
        this.queue = queue;
        this.callback = callback;
    }

    @Override
    public void run() {
        LoginResult result;
        try {
            // Find the user in the loaded users list
            User foundUser = null;

            for (User user : users) {
                if (user.getUsername().equals(email)) {
                    foundUser = user;
                    break;
                }
            }

            if (foundUser == null) {
                // Unknown emails are not tracked
                result = LoginResult.error(-1);

            } else {
                // Check blocking in a separate thread
                BlockCheckThread check = new BlockCheckThread(tracker, email);
                check.start();
                check.join();
                if (check.isBlocked()) {
                    // Reject blocked accounts immediately
                    result = LoginResult.blocked(check.getRemainingSeconds());
                } else if (foundUser.getPassword().equals(password)) {
                    // Reset counter after successful login
                    tracker.reset(email);
                    result = LoginResult.success();
                } else {
                    // Send failed attempt to the workers
                    FailedAttempt attempt = new FailedAttempt(email);
                    queue.put(attempt);

                    // Wait for the worker's response
                    result = attempt.waitForResult();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result = LoginResult.error(-1);

        } catch (IllegalArgumentException e) {
            // Handle invalid input
            result = LoginResult.error(-1);
        }

        // Update the GUI only on the JavaFX thread
        LoginResult finalResult = result;
        Platform.runLater(() -> callback.accept(finalResult));
    }
}
