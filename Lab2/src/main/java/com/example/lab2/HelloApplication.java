package com.example.lab2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        List<String> args = getParameters().getRaw();

        Integer n = readPositiveInteger(
                args, 0, "n", "Maximum failed attempts"
        );

        if (n == null) {
            Platform.exit();
            return;
        }

        Integer t = readPositiveInteger(
                args, 1, "t", "Block duration in seconds"
        );

        if (t == null) {
            Platform.exit();
            return;
        }

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 400, 350);

        HelloController controller = loader.getController();
        controller.configure(n, t);

        stage.setTitle("Users Login");
        stage.setScene(scene);
        stage.show();
    }

    private Integer readPositiveInteger(
            List<String> args,
            int index,
            String name,
            String description) {

        if (index < args.size()) {
            try {
                int value = Integer.parseInt(args.get(index));

                if (value >= 1) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Ask the user instead
            }
        }

        while (true) {

            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Login Settings");
            dialog.setHeaderText("Enter " + name);
            dialog.setContentText(description + " (>= 1):");

            Optional<String> answer = dialog.showAndWait();

            // Cancel or X: close the application
            if (answer.isEmpty()) {
                return null;
            }

            try {
                int value = Integer.parseInt(
                        answer.get().trim()
                );

                if (value >= 1) {
                    return value;
                }

            } catch (NumberFormatException ignored) {
                // Ask again
            }
        }
    }
}
