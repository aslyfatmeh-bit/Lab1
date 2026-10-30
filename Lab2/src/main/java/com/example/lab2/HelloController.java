
package com.example.lab2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label welcomeText;

    private final ArrayList<User> users =
            UserLoader.loadUsers("users.txt");

    @FXML
    protected void onHelloButtonClick() {

        String user = username.getText();
        String pass = password.getText();

        boolean found = false;

        for (User u : users) {
            if (u.getUsername().equals(user) &&
                    u.getPassword().equals(pass)) {
                found = true;
                break;
            }
        }

        if (found) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("welcome-view.fxml")
                );

                Scene scene = new Scene(loader.load());

                Stage welcomeStage = new Stage();
                welcomeStage.setTitle("Welcome");
                welcomeStage.setScene(scene);

                Stage loginStage =
                        (Stage) username.getScene().getWindow();

                loginStage.close();
                welcomeStage.show();

            } catch (IOException e) {
                welcomeText.setText("Cannot open Welcome screen");
                e.printStackTrace();
            }

        } else {
            welcomeText.setText("user or password do not match");
        }
    }
}

