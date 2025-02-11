package org.example.projectbidding.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.projectbidding.main.HelloApplication;
import org.example.projectbidding.model.Database;
import org.example.projectbidding.model.Project;
import org.example.projectbidding.model.ProjectRepository;
import org.example.projectbidding.serialization.ChangeLogEntry;
import org.example.projectbidding.serialization.ChangeLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProjectEditController {
    @FXML
    private TextField projectID;
    @FXML
    private TextField projectName;
    @FXML
    private TextArea projectDescription;
    @FXML
    private DatePicker projectStartDate;
    @FXML
    private DatePicker projectEndDate;

    private static final Logger logger = LoggerFactory.getLogger(ProjectEditController.class);


    ProjectRepository projectRepository = new ProjectRepository(Database.getConnection());
    List<Project> projects = projectRepository.getAll();

    /**
     * Updates the details of an existing project.
     * <p>
     * - Validates that a project ID is provided and correctly formatted. <br>
     * - Checks if the project ID exists in the database. <br>
     * - Gathers the existing project data before making changes. <br>
     * - Collects updates from input fields if they are not empty. <br>
     * - Prompts the user for confirmation before applying updates. <br>
     * - Logs the changes in the {@link ChangeLogManager}. <br>
     * - Updates the project details in the {@link ProjectRepository}.
     */
    public void update() {
        if (projectID.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter an ID");
            alert.showAndWait();
            return;
        }

        Long id;
        try {
            id = Long.parseLong(projectID.getText());
        } catch (NumberFormatException e) {
            logger.error("user entered a incorrect format of long", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please enter a valid ID");
            alert.showAndWait();
            return;
        }

        Optional<Project> projectOpt = projects.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (projectOpt.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Project ID not found!");
            alert.showAndWait();
            return;
        }

        Project selectedProject = projectOpt.get();
        String userRole = HelloApplication.getLoggedInUser();


        Map<String, Object> oldValues = new HashMap<>();
        oldValues.put("project_name", selectedProject.getProjectName());
        oldValues.put("project_description", selectedProject.getProjectDescription());
        oldValues.put("project_start_date", selectedProject.getProjectStartDate());
        oldValues.put("project_end_date", selectedProject.getProjectEndDate());


        Map<String, Object> updates = new HashMap<>();

        if (!projectName.getText().isEmpty()) {
            updates.put("project_name", projectName.getText());
        }
        if (!projectDescription.getText().isEmpty()) {
            updates.put("project_description", projectDescription.getText());
        }
        if (projectStartDate.getValue() != null) {
            updates.put("project_start_date", projectStartDate.getValue());
        }
        if (projectEndDate.getValue() != null) {
            updates.put("project_end_date", projectEndDate.getValue());
        }

        if (!updates.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Edit Project");
            alert.setHeaderText("Are you sure you want to edit this project?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                for (Map.Entry<String, Object> update : updates.entrySet()) {
                    String field = update.getKey();
                    String newValue = update.getValue().toString();
                    String oldValue = oldValues.get(field).toString();


                    ChangeLogManager.logChange(new ChangeLogEntry("Updated " + field + " of project " + selectedProject.getProjectName()
                            , oldValue
                            , newValue
                            , userRole));
                }


                projectRepository.update(id, updates);
            }
        }
    }


}
