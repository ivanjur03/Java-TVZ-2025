package org.example.projectbidding.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.projectbidding.exception.DateOutOfRangeException;
import org.example.projectbidding.exception.EmptyFieldException;
import org.example.projectbidding.main.HelloApplication;
import org.example.projectbidding.model.Database;
import org.example.projectbidding.model.Project;
import org.example.projectbidding.model.ProjectRepository;
import org.example.projectbidding.serialization.ChangeLogEntry;
import org.example.projectbidding.serialization.ChangeLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ProjectAddController {

    @FXML
    private TextField projectNameTF;
    @FXML
    private TextArea projectDescriptionTA;
    @FXML
    private DatePicker projectStartDateDP;
    @FXML
    private DatePicker projectEndDateDP;

    ProjectRepository projectRepository = new ProjectRepository(Database.getConnection());

    private static final Logger logger = LoggerFactory.getLogger(ProjectAddController.class);

    /**
     * Handles the process of adding a new project.
     * - Retrieves input values from text fields and date pickers.
     * - Constructs a new Project instance using the provided details.
     * - Validates the project details, ensuring no fields are empty and the date range is valid.
     * - Inserts the project into the database if validation passes.
     * - Logs the addition in the  ChangeLogManager.
     * - Displays an appropriate success or error alert.
     * - Clears input fields upon successful project addition.
     *
     * @throws EmptyFieldException If any required field is empty.
     * @throws DateOutOfRangeException If the provided date range is invalid.
     */
    public void addProject(){
        String projectName = this.projectNameTF.getText();
        String projectDescription = this.projectDescriptionTA.getText();
        LocalDate projectStartDate = this.projectStartDateDP.getValue();
        LocalDate projectEndDate = this.projectEndDateDP.getValue();


        Project project = new Project.ProjectBuilder()
                .setProjectName(projectName)
                .setProjectDescription(projectDescription)
                .setProjectStartDate(projectStartDate)
                .setProjectEndDate(projectEndDate).build();
        try{
            project.validate();
            projectRepository.insert(project);

            ChangeLogEntry entry = new ChangeLogEntry("Added new project with the name: "
                    + project.getProjectName(),
                    "N/A",
                    "N/A",
                    HelloApplication.getLoggedInUser());
            ChangeLogManager.logChange(entry);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("You have successfully added the project!");
            alert.showAndWait();


            projectNameTF.clear();
            projectDescriptionTA.clear();
            projectStartDateDP.setValue(null);
            projectEndDateDP.setValue(null);
        } catch (EmptyFieldException | DateOutOfRangeException e) {
            logger.error("user missed a field, or picked a bad date", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field or Date Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }

    }


}
