package org.example.projectbidding.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContractorEditController {
    @FXML
    private TextField contractorID;
    @FXML
    private TextField contractorName;
    @FXML
    private TextField contractorPassword;
    @FXML
    private TextField contractorEmail;
    @FXML
    private TextField contractorRating;


    private static final Logger logger = LoggerFactory.getLogger(ContractorEditController.class);

    ContractorRepository contractorRepository = new ContractorRepository(Database.getConnection());
    List<Contractor> contractors = contractorRepository.getAll();

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Validates the given email string against a predefined regular expression pattern.
     *
     * @param emailStr The email address to validate.
     * @return {@code true} if the email format is valid, {@code false} otherwise.
     */
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
    /**
     * Handles the process of editing an existing contractor.
     * - Validates that a contractor ID is provided and correctly formatted. <br>
     * - Checks if the contractor ID exists in the database. <br>
     * - Validates the email format if provided. <br>
     * - Parses and verifies the rating input if provided. <br>
     * - Collects changes and prompts for confirmation before applying updates. <br>
     * - Updates the contractor details in the database. <br>
     * - Logs changes in the ChangeLogManager. <br>
     * - Updates user credentials in the LoginManager if necessary.
     */
    public void editContractor() {
        if (contractorID.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter an ID");
            alert.showAndWait();
            return;
        }

        Long id;
        try {
            id = Long.parseLong(contractorID.getText());
        } catch (NumberFormatException e) {
            logger.error("user entered a incorrect format of long", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter a valid ID");
            alert.showAndWait();
            return;
        }


        Optional<Contractor> contractorOpt = contractors.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (contractorOpt.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Contractor ID not found!");
            alert.showAndWait();
            return;
        }

        Contractor selectedContractor = contractorOpt.get();
        String userRole = HelloApplication.getLoggedInUser();

        Map<String, Object> oldValues = new HashMap<>();
        oldValues.put("username", selectedContractor.getUsername());
        oldValues.put("password", selectedContractor.getPassword());
        oldValues.put("email", selectedContractor.getEmail());
        oldValues.put("rating", selectedContractor.getRating());


        Map<String, Object> updates = new HashMap<>();

        if (!contractorName.getText().isEmpty()) {
            updates.put("username", contractorName.getText());
        }
        if (!contractorPassword.getText().isEmpty()) {
            updates.put("password", contractorPassword.getText());
        }
        if (!contractorEmail.getText().isEmpty()) {
            if (!validate(contractorEmail.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("That's not a valid email!");
                alert.showAndWait();
                return;
            }
            updates.put("email", contractorEmail.getText());
        }
        if (!contractorRating.getText().isEmpty()) {
            try {
                BigDecimal newRating = new BigDecimal(contractorRating.getText());
                updates.put("rating", newRating);
            } catch (NumberFormatException e) {
                logger.error("user entered a incorrect format of decimal", e);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid rating format!");
                alert.showAndWait();
                return;
            }
        }

        if (!updates.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Edit Contractor");
            alert.setHeaderText("Are you sure you want to edit this contractor?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                for (Map.Entry<String, Object> update : updates.entrySet()) {
                    String field = update.getKey();
                    String newValue = update.getValue().toString();
                    String oldValue = oldValues.get(field).toString();


                    ChangeLogManager.logChange(new ChangeLogEntry("Updated " + field +"of contractor " + selectedContractor.getUsername()
                            , oldValue
                            , newValue
                            , userRole));
                }


                contractorRepository.update(id, updates);


                LoginManager.updateUser(selectedContractor.getUsername(), contractorName.getText(), contractorPassword.getText());
            }
        }
    }

}
