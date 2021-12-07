package com.pandaism.gui.controller;

import com.pandaism.FMUnitTool;
import com.pandaism.util.file.ExcelManager;
import com.pandaism.util.typing.Devices;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class OrderTabController {
    public VBox input_pane;

    public TextField save_path;
    public TableView content_pane;
    public Label device_label;
    public Label count_label;

    private String salesOrder;
    private Devices devices;
    private List<TextField> textFields = new ArrayList<>();

    //TODO Add lat and long feature
    public String address;
    //TODO Add timezone feature
    public String timezone;

    public void initialize() {
        this.content_pane.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setDevices(Devices devices) {
        this.devices = devices;

        for(int i = 0; i < devices.getRecordable().length; i++) {
            VBox fieldContainer = new VBox();
            fieldContainer.setSpacing(5.0);
            Label fieldLabel = new Label(devices.getRecordable()[i].getField());
            TextField fieldTextField = new TextField();
            fieldTextField.setId("textfield_" + devices.getRecordable()[i]);
            this.textFields.add(fieldTextField);

            fieldContainer.getChildren().addAll(fieldLabel, fieldTextField);
            this.input_pane.getChildren().add(fieldContainer);

            TableColumn col = new TableColumn(devices.getRecordable()[i].getField());
            int finalI = i;
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(finalI).toString()));

            this.content_pane.getColumns().add(col);
        }

        this.device_label.setText("Work Device: " + this.devices.getDevice());
    }

    public void setSalesOrder(String salesOrder) {
        this.salesOrder = salesOrder;

        this.save_path.setPromptText(FMUnitTool.data.getOutputFolder().getPath() + "\\SO" + this.salesOrder + ".xlsx");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void onSubmit(ActionEvent actionEvent) {
        ObservableList<String> row = FXCollections.observableArrayList();
        for (TextField textfield : this.textFields) {
            row.add(textfield.getText());
            textfield.setText("");
        }

        this.textFields.get(0).requestFocus();
        this.content_pane.getItems().add(row);
        this.count_label.setText(String.valueOf(this.content_pane.getItems().size()));
    }

    /*TODO need to work on removing feature*/
    public void deleteRow(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        if(keyEvent.getCode() == KeyCode.DELETE) {
            this.content_pane.getSelectionModel().getSelectedIndices().forEach(i -> this.content_pane.getItems().remove(i));
        }
    }

    public void onClear(ActionEvent actionEvent) {
        for (TextField textField : this.textFields) {
            textField.setText("");
        }

        this.textFields.get(0).requestFocus();
    }

    public void export(ActionEvent actionEvent) {
        String savePath = this.save_path.getText().isEmpty() ? this.save_path.getPromptText() : this.save_path.getText();

        new ExcelManager(this.content_pane, this.salesOrder, savePath, this.devices);
    }
}
