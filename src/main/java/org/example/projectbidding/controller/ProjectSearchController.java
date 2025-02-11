package org.example.projectbidding.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.util.List;
import java.util.Optional;

public class ProjectSearchController {
    @FXML
    private TextField projectName;
    @FXML
    private DatePicker beforeDate;
    @FXML
    private TextField projectID;
    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, String> projectIDTC;
    @FXML
    private TableColumn<Project, String> projectNameTC;
    @FXML
    private TableColumn<Project, String> projectdescriptionTC;
    @FXML
    private TableColumn<Project, String> projectStartDateTC;
    @FXML
    private TableColumn<Project, String> projectEndDateTC;

    ProjectRepository projectRepository = new ProjectRepository(Database.getConnection());
    List<Project> projectList = projectRepository.getAll();

    private static final Logger logger = LoggerFactory.getLogger(ProjectSearchController.class);

    /**
     * Initializes the project table by setting up column value factories.
     * Populates the table with project data when the FXML is loaded.
     */
    public void initialize() {
        projectIDTC.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getId().toString()));
        projectNameTC.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getProjectName()));
        projectdescriptionTC.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getProjectDescription()));
        projectStartDateTC.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getProjectStartDate().toString()));
        projectEndDateTC.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getProjectEndDate().toString()));
    }

    /**
     * Filters projects based on the provided project name and/or start date.
     * <p>
     * - If only the project name is provided, filters projects containing the given name (case insensitive). <br>
     * - If only the date is provided, filters projects that started before the given date. <br>
     * - If both fields are provided, applies both filters. <br>
     * - Updates the project table with the filtered results.
     */
    public void filterProjects(){
        List<Project> projects = projectRepository.getAll();
        if(!projectName.getText().isEmpty()&&beforeDate.getValue()==null){
            projects = projects.stream().filter(p -> p.getProjectName().toLowerCase().contains(projectName.getText().toLowerCase())).toList();
        }
        if(projectName.getText().isEmpty()&&beforeDate.getValue()!=null){
            projects=projects.stream().filter(p -> p.getProjectStartDate().isBefore(beforeDate.getValue())).toList();
        }
        if(!projectName.getText().isEmpty()&&beforeDate.getValue()!=null){
            projects = projects.stream().filter(p -> p.getProjectName().toLowerCase().contains(projectName.getText().toLowerCase())).toList();
            projects=projects.stream().filter(p -> p.getProjectStartDate().isBefore(beforeDate.getValue())).toList();
        }

        ObservableList<Project> projectObservableList = FXCollections.observableArrayList(projects);
        projectTable.setItems(projectObservableList);
    }

    /**
     * Removes a project based on the provided project ID.
     * <p>
     * - Validates if the ID is provided and correctly formatted. <br>
     * - Checks if the project exists in the database. <br>
     * - Prompts the user for confirmation before deletion. <br>
     * - Logs the removal in the {@link ChangeLogManager}. <br>
     * - Deletes the project from the repository if confirmed.
     */
    public void removeProject(){
        if(!projectID.getText().isEmpty()){
            try{
                Long.parseLong(projectID.getText());
                if(projectList.stream().anyMatch(p -> p.getId().equals(Long.parseLong(projectID.getText())))){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Remove Project");
                    alert.setHeaderText("Are you sure you want to remove this project?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        ChangeLogEntry entry = new ChangeLogEntry("Removed project with the name: "
                                + projectRepository.getById(Long.parseLong(projectID.getText())).get().getProjectName(),
                                "N/A",
                                "N/A",
                                HelloApplication.getLoggedInUser());
                        ChangeLogManager.logChange(entry);

                        projectRepository.deleteById(Long.parseLong(projectID.getText()));
                    }
                }else {
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
