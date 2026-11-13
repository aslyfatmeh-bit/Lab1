package com.example.lab2;

public class LoginResult {

    // Possible login results
    public enum Status {
        SUCCESS,
        ERROR,
        BLOCKED
    }
    private final Status status;
    private final int remainingAttempts;
    private final long remainingSeconds;

    // Create a login result
    public LoginResult(Status status, int remainingAttempts, long remainingSeconds) {
        this.status = status;
        this.remainingAttempts = remainingAttempts;
        this.remainingSeconds = remainingSeconds;
    }

    // Successful login
    public static LoginResult success() {
        return new LoginResult(Status.SUCCESS, 0, 0);
    }

    // Incorrect email or password
    public static LoginResult error(int remainingAttempts) {
        return new LoginResult(Status.ERROR, remainingAttempts, 0);
    }

    // Account is temporarily blocked
    public static LoginResult blocked(long remainingSeconds) {
        return new LoginResult(Status.BLOCKED, 0, remainingSeconds);
    }

    // Return the result status
    public Status getStatus() {
        return status;
    }

    // Return the number of remaining attempts
    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    // Return the remaining blocking time
    public long getRemainingSeconds() {
        return remainingSeconds;
    }
}
