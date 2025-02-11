package org.example.projectbidding.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.projectbidding.main.HelloApplication;
import org.example.projectbidding.model.*;
import org.example.projectbidding.serialization.ChangeLogEntry;
import org.example.projectbidding.serialization.ChangeLogManager;


import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContractorAddController {
    @FXML
    private TextField name;
    @FXML
    private TextField password;
    @FXML
    private TextField email;
    @FXML
    private TextField rating;


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
     * Handles the process of adding a new contractor.
     * - Validates input fields to ensure they are not empty and that the email is valid.
     * - Hashes the contractor's password and creates a new Contractor instance.
     * - Inserts the contractor into the database.
     * - Registers the contractor with the LoginManager.
     * - Logs the change in the ChangeLogManager.
     * - Displays appropriate success or error messages.
     * - Clears input fields upon successful addition.
     */


    public void addContractor() {

        ContractorRepository contractorRepository = new ContractorRepository(Database.getConnection());

        if (name.getText().isEmpty() || password.getText().isEmpty() || email.getText().isEmpty() || rating.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("please fill all the fields");
            alert.showAndWait();
        } else if(!validate(email.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("that's not a valid email!");
            alert.showAndWait();

        } else {
            Contractor contractor = new Contractor.ContractorBuilder()
                    .setUsername(name.getText())
                    .setPassword(LoginManager.hashPassword(password.getText()))
                    .setEmail(email.getText())
                    .setRating(new BigDecimal(rating.getText())).build();

            contractorRepository.insert(contractor);

            Pair<String, String> namePass = new Pair<>(name.getText(), password.getText());
            LoginManager.addUser(namePass);

            ChangeLogEntry entry = new ChangeLogEntry("Added new contractor with username: "
                    + contractor.getUsername(),
                    "N/A",
                    "N/A",
                    HelloApplication.getLoggedInUser());
            ChangeLogManager.logChange(entry);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("You have successfully added the contactor!");
            alert.showAndWait();
            name.clear();
            password.clear();
            email.clear();
            rating.clear();

        }


    }
}
