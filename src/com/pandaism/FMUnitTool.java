package com.pandaism;

import com.pandaism.util.GUIManager;
import com.pandaism.util.typing.Settings;
import com.pandaism.util.thread.ThreadManager;
import com.pandaism.util.file.Data;
import com.pandaism.util.file.Log;
import com.squareup.okhttp.OkHttpClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FMUnitTool extends Application {
    public static GUIManager guiManager;
    public static Log log;
    public static Data data;
    public static Settings settings;
    public static ThreadManager threadManager;
    public static OkHttpClient httpClient;

    @Override
    public void start(Stage primaryStage) throws Exception {
        guiManager = new GUIManager();
        log = new Log();
        settings = new com.pandaism.util.file.Settings().readSettings();
        data = new Data();
        threadManager = new ThreadManager();

        httpClient = new OkHttpClient();

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
