package com.pandaism.gui.controller;

import com.pandaism.FMUnitTool;
import com.pandaism.util.file.ExcelManager;
import com.pandaism.util.thread.TimeThread;
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

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderTabController {
    public VBox input_pane;

    public TextField save_path;
    public TableView content_pane;
    public Label device_label;
    public Label count_label;
    public Label latitude_label;
    public Label longitude_label;
    public Label time_label;

    private String salesOrder;
    private Devices devices;
    private List<TextField> textFields = new ArrayList<>();

    public String latitude;
    public String longitude;
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

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        this.latitude_label.setText(latitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        this.longitude_label.setText(longitude);
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
        TimeThread timeThread = new TimeThread(this.time_label, this.timezone);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(timeThread, 0, 1, TimeUnit.SECONDS);

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
        if(this.content_pane.getItems().size() > 0) {
            GregorianCalendar calendar = new GregorianCalendar();
            DateFormat df = new SimpleDateFormat("MM-dd-yy");

            String excelPath = this.save_path.getText().isEmpty() ? this.save_path.getPromptText() : this.save_path.getText();
            String crossReferencePath = excelPath.substring(0, excelPath.lastIndexOf("\\") + 1) + this.salesOrder + "-" + this.content_pane.getItems().size() + "-" + df.format(calendar.getTime()) + ".txt";

            new ExcelManager(this.content_pane, this.salesOrder, excelPath, this.devices);
            //TODO implement CrossReferencing
            //new CrossReferenceManager(this.content_pane, crossReferencePath, this.devices);
        } else {
            JOptionPane.showConfirmDialog(null, "Table Information is empty. There is nothing to export.");
        }

        System.out.println(this.latitude + " : " + this.longitude);

    }


}
