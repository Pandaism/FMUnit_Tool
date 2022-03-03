package com.pandaism.gui.scene;

import com.pandaism.FMUnitTool;
import com.pandaism.gui.controller.OrderTabController;
import com.pandaism.util.typing.Devices;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class OrderTab extends Tab {
    private OrderTabController orderTabController;

    public OrderTab(String salesOrder, Devices devices, String longitude, String latitude, String timezone) {

        super.setText("SO" + salesOrder);

        FXMLLoader loader = new FXMLLoader(FMUnitTool.guiManager.getScenes().get("Order Tab Scene").getPath());
        try {
            BorderPane order_tab = loader.load();
            this.orderTabController = loader.getController();
            this.orderTabController.setDevices(devices);
            this.orderTabController.setSalesOrder(salesOrder);
            this.orderTabController.setLongitude(longitude);
            this.orderTabController.setLatitude(latitude);
            this.orderTabController.setTimezone(timezone);

            this.setContent(order_tab);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
