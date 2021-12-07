package com.pandaism.gui.scene;

import com.pandaism.FMUnitTool;
import com.pandaism.gui.controller.OrderTabController;
import com.pandaism.util.typing.Devices;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class OrderTab extends Tab {
    public OrderTab(String salesOrder, Devices devices, String address, String timezone) {

        super.setText("SO" + salesOrder);

        FXMLLoader loader = new FXMLLoader(FMUnitTool.guiManager.getScenes().get("Order Tab Scene").getPath());
        try {
            BorderPane order_tab = loader.load();
            OrderTabController orderTabController = loader.getController();
            orderTabController.setDevices(devices);
            orderTabController.setSalesOrder(salesOrder);
            orderTabController.setAddress(address);
            orderTabController.setTimezone(timezone);

            this.setContent(order_tab);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
