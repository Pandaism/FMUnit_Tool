package com.pandaism.util.typing;

import com.squareup.moshi.Json;

public class Settings {
    @Json(name = "data template path")
    private String templatePath;
    @Json(name = "api key")
    private String googleAPIKey;
    @Json(name = "updater url")
    private String updateUrl;

    public String getTemplatePath() {
        return templatePath;
    }

    public String getGoogleAPIKey() {
        return googleAPIKey;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }
}
