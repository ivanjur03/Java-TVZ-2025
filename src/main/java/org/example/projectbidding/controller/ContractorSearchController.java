package org.example.projectbidding.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.example.projectbidding.main.HelloApplication;
import org.example.projectbidding.model.Contractor;
import org.example.projectbidding.model.ContractorRepository;
import org.example.projectbidding.model.Database;
import org.example.projectbidding.model.LoginManager;
import org.example.projectbidding.serialization.ChangeLogEntry;
import org.example.projectbidding.serialization.ChangeLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ContractorSearchController {
    @FXML
    private TextField username;
    @FXML
    private TextField rating;
    @FXML
    private TextField id;
    @FXML
    private TableView<Contractor> contractorTableView;
    @FXML
    private TableColumn<Contractor, String> idColumn;
    @FXML
    private TableColumn<Contractor, String> usernameColumn;
    @FXML
    private TableColumn<Contractor, String> emailColumn;
    @FXML
    private TableColumn<Contractor, String> ratingColumn;
    @FXML
    private TableColumn<Contractor, String> projectsColumn;

    ContractorRepository contractorRepository = new ContractorRepository(Database.getConnection());

    private static final Logger logger = LoggerFactory.getLogger(ContractorSearchController.class);
    /**
     * Initializes the contractor table by setting up column value factories.
     * Retrieves project names for each contractor and populates the project column.
     * This method is automatically called when the FXML is loaded.
     */
    public void initialize() {
        idColumn.setCellValueFactory(cellvalue-> new SimpleStringProperty(cellvalue.getValue().getId().toString()));
        usernameColumn.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getUsername()));
        emailColumn.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getEmail()));
        ratingColumn.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getRating().toString()));

        projectsColumn.setCellValueFactory(cellValue -> {
            Contractor contractor = cellValue.getValue();
            String projects = contractorRepository.getProjectNamesForContractor(contractor.getId());
            return new SimpleStringProperty(projects);
        });

    }
    /**
     * Searches for contractors based on the provided username and/or rating.
     * - If only a username is provided, filters contractors by username (case insensitive).
     * - If only a rating is provided, filters contractors with a rating greater than or equal to the given value.
     * - If both fields are provided, applies both filters. <br>
     * - Updates the table with the filtered results.
     */

    public void searchContractors(){
        List<Contractor> contractors = contractorRepository.getAll();


        if(!username.getText().isEmpty()&&rating.getText().isEmpty()){
            contractors=contractors.stream().filter(contractor -> contractor.getUsername().contains(username.getText())).toList();
        }
        if(username.getText().isEmpty()&&!rating.getText().isEmpty()){
            contractors=contractors.stream().filter(contractor-> contractor.getRating().compareTo(new BigDecimal(rating.getText())) >= 0).toList();
        }
        if(!username.getText().isEmpty()&&!rating.getText().isEmpty()){
            contractors=contractors.stream().filter(contractor -> contractor.getUsername().contains(username.getText())).toList();
            contractors=contractors.stream().filter(contractor-> contractor.getRating().compareTo(new BigDecimal(rating.getText())) >= 0).toList();
        }

        ObservableList<Contractor> contractorObservableList = FXCollections.observableList(contractors);
        contractorTableView.setItems(contractorObservableList);

    }
    /**
     * Removes a contractor based on the provided ID.
     * - Validates if the ID is provided and in the correct format.
     * - Checks if the contractor exists in the database.
     * - Prompts the user for confirmation before deletion.
     * - Deletes the contractor from the database if confirmed.
     * - Logs the removal in the ChangeLogManager.
     * - Removes the contractor from the LoginManager.
     */
    public void removeContractor(){
        List<Contractor> contractors = contractorRepository.getAll();
        if(!id.getText().isEmpty()){
            try{
                Long.parseLong(id.getText());
                if(contractors.stream().anyMatch(contractor -> contractor.getId().equals(Long.parseLong(id.getText())))){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Remove Contractor");
                    alert.setHeaderText("Are you sure you want to remove this contractor?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        contractorRepository.deleteById(Long.parseLong(id.getText()));
                        Contractor selectedContractor = contractors.stream().filter(c -> c.getId().equals(Long.parseLong(id.getText()))).findFirst().get();

                        ChangeLogEntry entry = new ChangeLogEntry("Removed contractor with username: "
                                + selectedContractor.getUsername(),
                                "N/A",
                                "N/A",
                                HelloApplication.getLoggedInUser());
                        ChangeLogManager.logChange(entry);

                        LoginManager.removeUser(selectedContractor.getUsername());
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid input for ID!");
                    alert.showAndWait();
                }



            }catch (NumberFormatException e){
                logger.error("user entered a incorrect format of long", e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid input for ID!");
                alert.showAndWait();

            }
        }
    }
}
