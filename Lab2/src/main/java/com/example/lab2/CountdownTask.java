package com.example.lab2;

import javafx.application.Platform;
import java.util.function.LongConsumer;

public class CountdownTask implements Runnable {

    private final AttemptTracker tracker;
    private final String email;
    private final LongConsumer onTick;
    private final Runnable onFinish;

    public CountdownTask(AttemptTracker tracker,
                         String email,
                         LongConsumer onTick,
                         Runnable onFinish) {
        this.tracker = tracker;
        this.email = email;
        this.onTick = onTick;
        this.onFinish = onFinish;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                long remaining =
                        tracker.getRemainingSeconds(email);

                if (remaining <= 0) {
                    Platform.runLater(onFinish);
                    break;
                }

                Platform.runLater(() -> onTick.accept(remaining));

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
