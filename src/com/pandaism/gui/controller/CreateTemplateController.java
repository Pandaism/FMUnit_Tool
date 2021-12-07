package com.pandaism.gui.controller;

import com.pandaism.FMUnitTool;
import com.pandaism.util.typing.Devices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class CreateTemplateController {
    public TextField name;
    public TableView<Devices.Recordable> scannable;
    public TextArea description;

    private Stage stage;
    private TableView<Devices> devices;

    public void initialize() {
        scannable.setEditable(true);

        TableColumn<Devices.Recordable, String> fieldCol = new TableColumn<>("Fields");
        fieldCol.setCellValueFactory(new PropertyValueFactory<>("field"));
        fieldCol.setCellFactory(TextFieldTableCell.forTableColumn());
        fieldCol.setOnEditCommit(event -> event.getTableView().getItems().get(event.getTablePosition().getRow()).setField(event.getNewValue()));

        TableColumn<Devices.Recordable, Boolean> serializeCol = new TableColumn<>("isSerializable");
        serializeCol.setCellValueFactory(new PropertyValueFactory<>("serialized"));
        serializeCol.setCellFactory(CheckBoxTableCell.forTableColumn(serializeCol));
        

        this.scannable.getColumns().add(fieldCol);
        this.scannable.getColumns().add(serializeCol);
    }

    public void addScannable(ActionEvent actionEvent) {
        Devices.Recordable recordable = new Devices.Recordable();
        recordable.serializedProperty().addListener(((observable, oldValue, newValue) -> {
            if(recordable.serializedProperty().get() != newValue) {
                recordable.setSerialized(newValue);
            }
        }));
        this.scannable.getItems().add(recordable);
    }

    public void removeScannable(ActionEvent actionEvent) {
        this.scannable.getItems().remove(this.scannable.getSelectionModel().getSelectedIndex());
    }

    public void createDeviceListing(ActionEvent actionEvent) {
        FMUnitTool.data.createDataFile(new Devices(this.name.getText(), this.scannable.getItems().toArray(new Devices.Recordable[0]), this.description.getText()));
        FMUnitTool.data.reloadDataFiles(this.devices);
        this.stage.close();
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void setDevices(TableView<Devices> devices) {
        this.devices = devices;
    }

    public void cancel(ActionEvent actionEvent) {
        this.stage.close();
    }
}
