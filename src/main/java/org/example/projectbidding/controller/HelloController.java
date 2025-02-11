package org.example.projectbidding.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.projectbidding.exception.InvalidCredentialsException;
import org.example.projectbidding.main.HelloApplication;
import org.example.projectbidding.model.LoginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HelloController {
   @FXML
   private TextField username;
   @FXML
   private PasswordField password;

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    /**
     * Handles the login process for users.
     * - Validates that both username and password fields are not empty.
     * - Authenticates the user using the LoginManager.
     * - Redirects admin users to the admin dashboard.
     * - Redirects regular users to the user dashboard.
     * - Displays an error alert if authentication fails due to invalid credentials.
     */
    public void login() {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter your username and password!");
            alert.showAndWait();
            return;
        }

        try {
            if (username.getText().equals("admin") && LoginManager.authenticate(username.getText(), password.getText())) {
                showNewScreen("/org/example/projectbidding/admin-hello.fxml");
                HelloApplication.setLoggedInUser(username.getText());
            } else if (LoginManager.authenticate(username.getText(), password.getText())) {
                showNewScreen("/org/example/projectbidding/user-hello.fxml");
                HelloApplication.setLoggedInUser(username.getText());
            }
        } catch (InvalidCredentialsException e) {
            logger.error("Invalid username or password", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong Credentials");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Loads and displays a new screen based on the given FXML file path.
     * - Loads the specified FXML file.
     * - Sets the scene with a default size of 800x600.
     * - Updates the main application stage with the new scene.
     * - Logs an error if the FXML file fails to load.
     * @param path The resource path to the FXML file.
     */
    public void showNewScreen(String path){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(path));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("Failed to load FXML", e);
        }
    }


}