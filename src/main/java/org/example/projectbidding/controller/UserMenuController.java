package org.example.projectbidding.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import org.example.projectbidding.main.HelloApplication;
import org.example.projectbidding.model.Contractor;
import org.example.projectbidding.model.ContractorRepository;
import org.example.projectbidding.model.Database;
import org.example.projectbidding.model.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class UserMenuController {
    private static final Logger logger = LoggerFactory.getLogger(UserMenuController.class);

    ContractorRepository contractorRepository = new ContractorRepository(Database.getConnection());
    List<Contractor> contractorList = contractorRepository.getAll();

    /**
     * Loads and displays the user's projects screen.
     * <p>
     * - Loads the FXML file for the user projects view. <br>
     * - Sets the scene with a default size of 800x600. <br>
     * - Updates the main application stage with the new scene. <br>
     * - Logs an error if the FXML file fails to load.
     */
    public void showProjects(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/user-projects.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }

    /**
     * Loads and displays the screen for adding bids.
     * <p>
     * - Loads the FXML file for adding bids. <br>
     * - Sets the scene with a default size of 800x600. <br>
     * - Updates the main application stage with the new scene. <br>
     * - Logs an error if the FXML file fails to load.
     */
    public void addBids(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/add-bid.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }

    /**
     * Loads and displays the screen showing the user's bids.
     * <p>
     * - Loads the FXML file for the "My Bids" view. <br>
     * - Sets the scene with a default size of 800x600. <br>
     * - Updates the main application stage with the new scene. <br>
     * - Logs an error if the FXML file fails to load.
     */
    public void myBids(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/projectbidding/my-bids.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(),800,600);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            logger.error("an error ocurred while reading fxml file", e);
        }
    }
    /**
     * Logs out the current user and redirects to the login screen.
     * <p>
     * - Loads the FXML file for the login screen. <br>
     * - Sets the scene with a default size of 500x400. <br>
     * - Updates the main application stage with the new scene. <br>
     * - Logs an error if the FXML file fails to load.
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
     * Displays the profile information of the currently logged-in contractor.
     * <p>
     * - Retrieves the currently logged-in contractor from the repository. <br>
     * - Displays an alert with the contractor's username, email, rating, and bid count.
     */
    public void myProfile(){
        Contractor currentContractor = contractorList.stream().filter(c -> c.getUsername().equals(HelloApplication.getLoggedInUser())).findFirst().get();
        Pair<String, String> userInfo = new Pair<>(currentContractor.getUsername(), currentContractor.getEmail());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("My Profile");
        alert.setHeaderText("Profile Information:");
        alert.setContentText("Username: " +
                userInfo.getKey() +
                "\nEmail: " + userInfo.getValue() +
                "\nRating: " + currentContractor.getRating()
                +"\nNumber of bids: " + currentContractor.getBidList().size());
        alert.showAndWait();


    }

}
