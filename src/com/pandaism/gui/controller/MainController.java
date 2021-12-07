package com.pandaism.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class MainController {
    public AnchorPane menu_bar;
    @FXML public MenuBarController menu_barController;
    public TabPane tab_pane;

    public void initialize() {
        this.menu_barController.setParentController(this);
    }
}
