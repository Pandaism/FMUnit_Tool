package com.pandaism;

import com.pandaism.util.GUIManager;
import com.pandaism.util.typing.Settings;
import com.pandaism.util.file.Data;
import com.pandaism.util.file.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FMUnitTool extends Application {
    public static GUIManager guiManager;
    public static Log log;
    public static Data data;
    public static Settings settings;
    public static ExecutorService cachedThreadPool;

    @Override
    public void start(Stage primaryStage) throws Exception {
        guiManager = new GUIManager();
        log = new Log();
        settings = new com.pandaism.util.file.Settings().readSettings();
        data = new Data();
        cachedThreadPool = Executors.newCachedThreadPool();

        FXMLLoader loader = new FXMLLoader(guiManager.getScenes().get("Main").getPath());
        Parent root = loader.load();
        primaryStage.setTitle("FMUnit Tool");
        primaryStage.setScene(new Scene(root, 930, 1000));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
