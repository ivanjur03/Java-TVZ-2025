package org.example.projectbidding.controller;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.projectbidding.main.HelloApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AdminMenuController {
    private static final Logger logger = LoggerFactory.getLogger(AdminMenuController.class);

    /**
     * Loads and displays the Project Search screen in the main application stage.
     * Logs an error if the FXML file cannot be loaded.
     */
    public void showProjectSearch(){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/project-search.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }
    /**
     * Loads and displays the Project Add screen in the main application stage.
     * Logs an error if the FXML file cannot be loaded.
     */
    public void showProjectAdd(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/project-add.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }
    /**
     * Loads and displays the Project Edit screen in the main application stage.
     * Logs an error if the FXML file cannot be loaded.
     */
    public void showProjectEdit(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/project-edit.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }
    /**
     * Loads and displays the Contractor Search screen in the main application stage.
     * Logs an error if the FXML file cannot be loaded.
     */

    public void showContractorSearch(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/contractor-search.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }
    /**
     * Loads and displays the Contractor Add screen in the main application stage.
     * Logs an error if the FXML file cannot be loaded.
     */

    public void showContractorAdd(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/contractor-add.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }
    /**
     * Loads and displays the Contractor Edit screen in the main application stage.
     * Logs an error if the FXML file cannot be loaded.
     */

    public void showContractorEdit(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/contractor-edit.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }
    /**
     * Logs the user out and displays the main login screen.
     * Logs an error if the FXML file cannot be loaded.
     */

    public void logOut(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/hello-view.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),500,400);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }

    /**
     * Loads and displays the Change Log screen in a new stage.
     * Centers the window on the screen and sets the stage title.
     * Logs an error if the FXML file cannot be loaded.
     */
    public void showChangeLog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/changes.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root, 1200, 700);
            stage.setScene(scene);
            stage.setTitle("Change Log");


            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((bounds.getHeight() - stage.getHeight()) / 2);

            stage.show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }

}
