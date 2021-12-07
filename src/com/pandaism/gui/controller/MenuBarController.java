package com.pandaism.gui.controller;

import com.pandaism.FMUnitTool;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuBarController {
    private MainController parentController;

    public void createNewProject(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(FMUnitTool.guiManager.getScenes().get("New Project").getPath());
        Parent root = loader.load();

        Stage newProjectPopup = new Stage();
        newProjectPopup.setTitle("New Project");
        newProjectPopup.setScene(new Scene(root));
        newProjectPopup.show();

        NewProjectController controller = loader.getController();
        controller.setStage(newProjectPopup);
        controller.setMenuBarController(this);
        controller.setMainController(this.parentController);
    }

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
    }
}

