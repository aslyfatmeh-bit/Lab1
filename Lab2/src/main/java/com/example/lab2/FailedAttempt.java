package com.example.lab2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FailedAttempt {

    // Email of the user who failed to log in
    private final String email;

    // Queue used to return the result to LoginTask
    private final BlockingQueue<LoginResult> responseQueue = new ArrayBlockingQueue<>(1);

    // Create a failed login event
    public FailedAttempt(String email) {
        this.email = email;
    }

    // Return the user's email
    public String getEmail() {
        return email;
    }

    // Send the result back to LoginTask
    public void sendResult(LoginResult result)
            throws InterruptedException {
        responseQueue.put(result);
    }

    // Wait until FailureWorker returns the result
    public LoginResult waitForResult()
            throws InterruptedException {
        return responseQueue.take();
    }
}
