package org.example.projectbidding.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.projectbidding.exception.EmptyFieldException;
import org.example.projectbidding.exception.InvalidBidAmountException;
import org.example.projectbidding.main.HelloApplication;
import org.example.projectbidding.model.*;
import org.example.projectbidding.serialization.ChangeLogEntry;
import org.example.projectbidding.serialization.ChangeLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AddBidController {
    @FXML
    private TextField bidAmount;
    @FXML
    private ComboBox<String> pickProject;
    @FXML
    private Label dateLabel;

    private static final Logger logger = LoggerFactory.getLogger(AddBidController.class);


    ProjectRepository projectRepository = new ProjectRepository(Database.getConnection());
    List<Project> projects = projectRepository.getAll();
    ContractorRepository contractorRepository = new ContractorRepository(Database.getConnection());
    List<Contractor> contractors = contractorRepository.getAll();
    BidRepository bidRepository = new BidRepository(Database.getConnection());

    /**
     * Initializes the AddBidController by populating the project ComboBox with available projects
     * and setting the date label to the current date. This method is called automatically
     * when the associated FXML is loaded.
     */
    public void initialize() {

        pickProject.setItems(FXCollections.observableList(projects.stream().map(Project::getProjectName).toList()));
        dateLabel.setText("The date of your bid will be set as: " + LocalDate.now());

    }

    /**
     * Handles the creation of a new bid. Validates input fields, retrieves the selected project
     * and contractor, creates a new bid object, and logs the change in the change log. Displays
     * success or error messages based on the outcome.
     *
     * @throws EmptyFieldException if any required field is empty.
     * @throws InvalidBidAmountException if the bid amount is invalid.
     */
    public void makeBid(){
        if(!bidAmount.getText().isEmpty()){
            try{
                new BigDecimal(bidAmount.getText());
            }catch (NumberFormatException e){
                logger.error("Invalid bid amount, unable to be parsed!!", e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You need to enter a number!");
                alert.showAndWait();
                return;
            }
        }

        try{
            Project project = projects.stream().filter(p -> p.getProjectName().equals(pickProject.getSelectionModel().getSelectedItem())).findFirst().get();
            Contractor contractor = contractors.stream().filter(c -> c.getUsername().equals(HelloApplication.getLoggedInUser())).findFirst().get();

            Bid bid = new Bid.BidBuilder()
                    .setProject(project)
                    .setContractor(contractor)
                    .setBidAmount(new BigDecimal(bidAmount.getText()))
                    .setBidDate(LocalDate.now())
                    .build();
            bid.validate();
            bidRepository.insert(bid);

            ChangeLogEntry entry = new ChangeLogEntry("New bid for project "
                    + project.getProjectName(),
                    "N/A",
                    "N/A",
                    HelloApplication.getLoggedInUser());
            ChangeLogManager.logChange(entry);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("You have successfully added the bid!");
            alert.showAndWait();


            bidAmount.clear();
            pickProject.getSelectionModel().clearSelection();
        }catch (EmptyFieldException | InvalidBidAmountException e){
            logger.error("user left a field empty or made a invalid bid", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("One of the fields is empty or the bid amount is invalid!");
            alert.showAndWait();
        }



    }
}
