package org.example.projectbidding.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.projectbidding.main.HelloApplication;
import org.example.projectbidding.model.*;
import org.example.projectbidding.serialization.ChangeLogEntry;
import org.example.projectbidding.serialization.ChangeLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class MyBidsController {
    @FXML
    private TextField projectNameTF;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField bidID;
    @FXML
    private TableView<Bid> bidTable;
    @FXML
    private TableColumn<Bid, String> bidIDTC;
    @FXML
    private TableColumn<Bid, String> projectNameTC;
    @FXML
    private TableColumn<Bid, String> bidAmountTC;
    @FXML
    private TableColumn<Bid, String> bidDateTC;

    BidRepository bidRepository = new BidRepository(Database.getConnection());
    ContractorRepository contractorRepository = new ContractorRepository(Database.getConnection());
    List<Contractor> contractors = contractorRepository.getAll();
    Contractor contractor = contractors.stream().filter(c -> c.getUsername().equals(HelloApplication.getLoggedInUser())).findFirst().get();

    private static final Logger logger = LoggerFactory.getLogger(MyBidsController.class);

    /**
     * Initializes the bid table by setting up column value factories.
     * Populates the table with bid data when the FXML is loaded.
     */
    public void initialize() {
        bidIDTC.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getId().toString()));
        projectNameTC.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getProject().getProjectName()));
        bidAmountTC.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getBidAmount().toString()));
        bidDateTC.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getBidDate().toString()));
    }
    /**
     * Filters the bids based on the provided project name and/or bid date
     * - If only the project name is provided, filters bids that contain the given project name.
     * - If only the date is provided, filters bids that match the selected bid date.
     * - If both fields are provided, applies both filters.
     * - Updates the bid table with the filtered results.
     */
    public void filter(){
        List<Bid> bids = bidRepository.getAll();
        bids = bids.stream().filter(b->b.getContractor().getId().equals(contractor.getId())).toList();
        if(!projectNameTF.getText().isEmpty()&&datePicker.getValue()==null){
            bids = bids.stream().filter(b -> b.getProject().getProjectName().contains(projectNameTF.getText())).toList();
        }
        if(projectNameTF.getText().isEmpty()&&datePicker.getValue()!=null){
            bids = bids.stream().filter(b -> b.getBidDate().equals(datePicker.getValue())).toList();
        }
        if(!projectNameTF.getText().isEmpty()&&datePicker.getValue()!=null){
            bids = bids.stream().filter(b -> b.getProject().getProjectName().contains(projectNameTF.getText())).toList();
            bids = bids.stream().filter(b -> b.getBidDate().equals(datePicker.getValue())).toList();
        }

        bidTable.setItems(FXCollections.observableList(bids));


    }
    /**
     * Deletes a bid based on the provided bid ID.
     * - Validates if the ID is provided and correctly formatted.
     * - Checks if the bid exists in the database.
     * - Prompts the user for confirmation before deletion.
     * - Logs the deletion in the ChangeLogManager.
     * - Deletes the bid from the repository if confirmed.
     */
    public void delete(){
        List<Bid> bids = bidRepository.getAll();
        if(!bidID.getText().isEmpty()){
            try{
                Long.parseLong(bidID.getText());
                if(bids.stream().anyMatch(b->b.getId().equals(Long.parseLong(bidID.getText())))){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Remove Contractor");
                    alert.setHeaderText("Are you sure you want to remove this bid?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK){
                        ChangeLogEntry entry = new ChangeLogEntry("Deleted a bid",
                                "N/A",
                                "N/A",
                                HelloApplication.getLoggedInUser());
                        ChangeLogManager.logChange(entry);

                        bidRepository.deleteById(Long.parseLong(bidID.getText()));

                    }
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("please enter a valid ID");
                    alert.showAndWait();
                }

            }catch(NumberFormatException e){
                logger.error("user entered a incorrect format of long", e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("please enter a valid ID");
                alert.showAndWait();
            }


        }
    }
}
