package org.example.projectbidding.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.projectbidding.model.Database;
import org.example.projectbidding.model.Project;
import org.example.projectbidding.model.ProjectRepository;

import java.util.List;

public class ProjectsOverviewController {
    @FXML
    private TextField projectName;
    @FXML
    private DatePicker beforeDate;
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
}
