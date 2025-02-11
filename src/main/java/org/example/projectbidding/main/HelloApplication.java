package org.example.projectbidding.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;

/**
 * The main entry point for the Project Bidding System application.
 * <p>
 * This class initializes the JavaFX application, manages the main stage,
 * and tracks the currently logged-in user.
 */
public class HelloApplication extends Application implements Serializable {

    private static Stage mainStage;
    private static String loggedInUser;

    /**
     * Initializes and starts the JavaFX application.
     * - Loads the initial login screen from the FXML file. <br>
     * - Sets the primary stage dimensions and title. <br>
     * - Displays the main stage.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        stage.setTitle("Project Bidding System");
        stage.setScene(scene);
        stage.show();
    }

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setLoggedInUser(String loggedInUser) {
        HelloApplication.loggedInUser = loggedInUser;
    }

    public static String getLoggedInUser() {
        return loggedInUser;
    }
}