package com.pandaism.util.file;

import com.pandaism.FMUnitTool;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.*;

public class Settings {
    private File settings;

    public Settings() {
        this.settings = new File("./data/settings.json");

        if(!this.settings.exists()) {
            try {
                if(this.settings.createNewFile()) {
                    FMUnitTool.log.log("settings.json was created");

                    FileWriter writer = new FileWriter(this.settings);
                    writer.write("{");
                    writer.write("\"data template path\": \"./data/templates/\",\n");
                    writer.write("\"cross reference path\": \"./output/cross-references/\",\n");
                    writer.write("\"api key\": \"api_key\",\n");
                    writer.write("\"updater url\": \"updater url\"");
                    writer.write("}");
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public com.pandaism.util.typing.Settings readSettings() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(this.settings));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            sb.append(line);
        }

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<com.pandaism.util.typing.Settings> jsonAdapter = moshi.adapter(com.pandaism.util.typing.Settings.class);
        return jsonAdapter.fromJson(sb.toString());
    }


}
