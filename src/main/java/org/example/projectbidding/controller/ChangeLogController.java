package org.example.projectbidding.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.projectbidding.serialization.ChangeLogEntry;
import org.example.projectbidding.serialization.ChangeLogManager;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChangeLogController {
    @FXML
    private TableView<ChangeLogEntry> changeLogTable;
    @FXML
    private TableColumn<ChangeLogEntry, String> description;
    @FXML
    private TableColumn<ChangeLogEntry, String> oldValue;
    @FXML
    private TableColumn<ChangeLogEntry, String> newValue;
    @FXML
    private TableColumn<ChangeLogEntry, String> user;
    @FXML
    private TableColumn<ChangeLogEntry, String> timeStamp;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    /**
     * Initializes the Change Log table by setting up cell value factories and
     * scheduling periodic table refreshes every 3 seconds.
     * This method is automatically called when the FXML is loaded.
     */
    public void initialize() {
        changeLogTable.setFixedCellSize(50);
        description.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getFieldChanged()));
        oldValue.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getOldValue()));
        newValue.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getNewValue()));
        user.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getChangedByRole()));
        timeStamp.setCellValueFactory(cellvalue -> new SimpleStringProperty(cellvalue.getValue().getTimestamp().toString()));

        refreshTable();


        scheduler.scheduleAtFixedRate(() -> Platform.runLater(this::refreshTable), 3, 3, TimeUnit.SECONDS);
    }
    /**
     * Refreshes the Change Log table by reading the latest changes from the
     * ChangeLogManager and updating the table's data.
     */
    private void refreshTable() {
        List<ChangeLogEntry> changeLogEntries = ChangeLogManager.readChanges();
        changeLogTable.setItems(FXCollections.observableArrayList(changeLogEntries));
    }

}
