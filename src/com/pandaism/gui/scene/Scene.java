package com.pandaism.gui.scene;

import java.net.URL;

public abstract class Scene {
    private URL path;

    public Scene(URL path) {
        this.path = path;
    }

    public URL getPath() {
        return path;
    }
}
