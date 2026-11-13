package com.example.lab2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class HelloController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label welcomeText;

    @FXML
    private Button loginButton;

    private final ArrayList<User> users =
            UserLoader.loadUsers("users.txt");

    private AttemptTracker tracker;

    private final BlockingQueue<FailedAttempt> queue =
            new LinkedBlockingQueue<>();

    private Thread countdownThread;
    private String countdownEmail;
    private long countdownVersion = 0;

    // Configure n and t
    public void configure(int n, int t) {

        tracker = new AttemptTracker(n, t);

        // Start two daemon worker threads
        for (int i = 0; i < 2; i++) {

            FailureWorker worker =
                    new FailureWorker(queue, tracker);

            worker.setDaemon(true);
            worker.start();
        }
    }

    // Login button
    @FXML
    protected void onHelloButtonClick() {

        String email = username.getText();
        String pass = password.getText();

        // Check if account is blocked
        if (tracker.isBlocked(email)) {
            startCountdown(email);
            return;
        }

        loginButton.setDisable(true);
        username.setEditable(false);
        password.setEditable(false);

        welcomeText.setText("Checking login...");

        LoginTask task = new LoginTask(
                email,
                pass,
                users,
                tracker,
                queue,
                result -> handleLoginResult(email, result)
        );

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    // Handle login result
    private void handleLoginResult(
            String email,
            LoginResult result) {

        enableLogin();

        switch (result.getStatus()) {

            case SUCCESS:
                stopCountdown();
                openWelcomeWindow();
                break;

            case ERROR:
                stopCountdown();

                if (result.getRemainingAttempts() < 0) {
                    welcomeText.setText(
                            "Invalid username or password"
                    );
                } else {
                    welcomeText.setText(
                            "Wrong password. Remaining attempts: "
                                    + result.getRemainingAttempts()
                    );
                }
                break;

            case BLOCKED:
                startCountdown(email);
                break;
        }
    }

    // Start countdown for blocked account
    private void startCountdown(String email) {

        if (email.equals(countdownEmail)
                && countdownThread != null
                && countdownThread.isAlive()) {

            welcomeText.setText(
                    "Account blocked. Try again in "
                            + tracker.getRemainingSeconds(email)
                            + " seconds"
            );
            return;
        }

        stopCountdown();

        countdownEmail = email;
        long version = ++countdownVersion;

        CountdownTask task = new CountdownTask(
                tracker,
                email,

                remaining -> {

                    if (version != countdownVersion) {
                        return;
                    }

                    if (username.getText().equals(email)) {
                        welcomeText.setText(
                                "Account blocked. Try again in "
                                        + remaining
                                        + " seconds"
                        );
                    }
                },

                () -> {

                    if (version != countdownVersion) {
                        return;
                    }

                    countdownEmail = null;
                    countdownThread = null;

                    if (username.getText().equals(email)) {
                        welcomeText.setText("");
                    }

                    enableLogin();
                }
        );

        countdownThread = new Thread(task);
        countdownThread.setDaemon(true);
        countdownThread.start();
    }

    // Stop countdown
    private void stopCountdown() {

        countdownVersion++;
        countdownEmail = null;

        if (countdownThread != null) {
            countdownThread.interrupt();
            countdownThread = null;
        }
    }

    // Enable login fields
    private void enableLogin() {

        loginButton.setDisable(false);

        username.setEditable(true);
        password.setEditable(true);

        username.deselect();
        password.requestFocus();
    }

    // Open welcome window
    private void openWelcomeWindow() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "welcome-view.fxml"
                    )
            );

            Scene scene = new Scene(loader.load());

            Stage welcomeStage = new Stage();
            welcomeStage.setTitle("Welcome");
            welcomeStage.setScene(scene);

            Stage loginStage =
                    (Stage) username.getScene().getWindow();

            welcomeStage.show();
            loginStage.close();

        } catch (IOException e) {

            welcomeText.setText(
                    "Cannot open Welcome screen"
            );

            enableLogin();
            e.printStackTrace();
        }
    }

    // Stress Test button
    @FXML
    protected void onStressTestClick() {

        welcomeText.setText("Running stress test...");
        loginButton.setDisable(true);

        Thread testThread = new Thread(() -> {

            String result = StressTestTask.runTest();

            Platform.runLater(() -> {

                loginButton.setDisable(false);

                if (result.contains("TEST PASSED")) {

                    welcomeText.setText(
                            "Stress Test PASSED"
                    );

                } else {

                    welcomeText.setText(
                            "Stress Test FAILED"
                    );
                }

                System.out.println(result);
            });
        });

        testThread.setDaemon(true);
        testThread.start();
    }
}

