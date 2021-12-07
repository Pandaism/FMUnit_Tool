package com.pandaism.util;

import com.pandaism.gui.scene.*;

import java.util.HashMap;
import java.util.Map;

public class GUIManager {
    private Map<String, Scene> scenes;

    public GUIManager() {
        this.scenes = new HashMap<>();

        this.scenes.put("Main", new MainScene());
        this.scenes.put("New Project", new NewProjectScene());
        this.scenes.put("Create Template", new CreateTemplateScene());
        this.scenes.put("Order Tab Scene", new OrderTabScene());
    }

    public Map<String, Scene> getScenes() {
        return scenes;
    }
}
