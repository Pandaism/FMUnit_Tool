package com.pandaism.gui.controller;

import com.pandaism.FMUnitTool;
import com.pandaism.gui.scene.OrderTab;
import com.pandaism.util.thread.GoogleLookupThread;
import com.pandaism.util.typing.Devices;
import com.pandaism.util.typing.RecordableAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class NewProjectController {
    public SplitPane splitPane;
    public TextArea description_box;
    public TableView<Devices> device_table;
    public TextField sales_order;
    public TextField address;
    public TextField timezone;
    public Button create_button;

    private Stage stage;
    private MainController mainController;

    private String latitude;
    private String longitude;
    private String timeZoneId;

    public void initialize() {
        this.splitPane.getDividers().get(0).setPosition(.33);

        this.device_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> description_box.setText(observable.getValue().getDescription()));

        TableColumn<Devices, String> column = new TableColumn<>("Projects");
        column.setCellValueFactory(new PropertyValueFactory<>("device"));

        ObservableList<Devices> list = FXCollections.observableArrayList();
        Moshi moshi = new Moshi.Builder().add(new RecordableAdapter()).build();
        JsonAdapter<Devices> jsonAdapter = moshi.adapter(Devices.class);

        for(String k : FMUnitTool.data.getDatas().keySet()) {
            try {
                list.add(jsonAdapter.fromJson(FMUnitTool.data.getDatas().get(k)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.device_table.setItems(list);
        this.device_table.getColumns().add(column);

        this.address.focusedProperty().addListener((observable, oldValue, newValue) -> {
            //If not focused
            if(!observable.getValue()) {
                FMUnitTool.cachedThreadPool.execute(new GoogleLookupThread(this.address, this.timezone, this));
            }
        });

        this.timezone.textProperty().addListener(((observable, oldValue, newValue) -> {
            this.create_button.setDisable(newValue.isEmpty());
        }));
    }

    public void openCreateTemplate(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(FMUnitTool.guiManager.getScenes().get("Create Template").getPath());
        Parent root = loader.load();
        Stage newProjectPopup = new Stage();
        newProjectPopup.setTitle("Create Template");
        newProjectPopup.setScene(new Scene(root));
        newProjectPopup.show();

        CreateTemplateController createTemplateController = loader.getController();
        createTemplateController.setStage(newProjectPopup);
        createTemplateController.setDevices(this.device_table);
    }

    public void createProject(ActionEvent actionEvent) {
        Devices devices = this.device_table.getSelectionModel().getSelectedItem();

        this.mainController.tab_pane.getTabs().add(new OrderTab(this.sales_order.getText(), devices, this.longitude, this.latitude, this.timeZoneId));
        this.stage.close();
        FMUnitTool.log.log(devices.getDevice() + " project created");
    }

    public void cancel(ActionEvent actionEvent) {
        this.stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setTimezoneString(String timeZoneId) {

        this.timeZoneId = timeZoneId;
    }
}
