package com.pandaism.util.file;

import com.pandaism.FMUnitTool;
import com.pandaism.util.typing.Devices;
import com.pandaism.util.typing.RecordableAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Data {
    private Map<String, String> datas;
    private final File dataFolder;

    private final File outputFolder;
    
    public Data() {
        this.outputFolder = new File("./output/");
        this.datas = new HashMap<>();

        if(!this.outputFolder.exists()) {
            if(!this.outputFolder.mkdirs()) {
                FMUnitTool.log.log("Failed to create output folder");
            } else {
                FMUnitTool.log.log("Successfully created data folder");
            }
        } else {
            FMUnitTool.log.log("Succesfully identified output folder");
        }

        this.dataFolder = new File(FMUnitTool.settings.getTemplatePath());
        if(!this.dataFolder.exists()) {
            if(!this.dataFolder.mkdirs()) {
                FMUnitTool.log.log("Failed to create data folder");
            } else {
                FMUnitTool.log.log("Successfully created data folder");
            }
        } else {
            registerDataFiles();
        }
    }
    
    private void registerDataFiles() {
        FMUnitTool.log.log("===== Registering Data Files =====");
        File[] files = this.dataFolder.listFiles();
        
        if(files != null) {
            for(File file : files) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    StringBuilder data = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        data.append(line);
                    }

                    if(validateData(data.toString())) {
                        FMUnitTool.log.log(file + " is registered successfully");

                        String name = file.toString().substring(2);
                        this.datas.put(name.substring(name.indexOf("\\") + 1, name.indexOf(".")), data.toString());
                    } else {
                        FMUnitTool.log.log(file + " failed to register. Checking formatting");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FMUnitTool.log.log("==================================");
    }

    private boolean validateData(String data) {
        Moshi moshi = new Moshi.Builder().add(new RecordableAdapter()).build();
        JsonAdapter<Devices> jsonAdapter = moshi.adapter(Devices.class);
        try {
            Devices devices = jsonAdapter.fromJson(data);

            return (devices != null ? devices.getDevice() : null) != null && devices.getRecordable() != null && devices.getDescription() != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void reloadDataFiles(TableView<Devices> devices) {
        FMUnitTool.log.log("===== Reloading Data Files =====");
        registerDataFiles();
        devices.getItems().removeAll(devices.getItems());
        ObservableList<Devices> list = FXCollections.observableArrayList();
        Moshi moshi = new Moshi.Builder().add(new RecordableAdapter()).build();
        JsonAdapter<Devices> jsonAdapter = moshi.adapter(Devices.class);

        for(String k : FMUnitTool.data.getDatas().keySet()) {
            try {
                list.add(jsonAdapter.fromJson(FMUnitTool.data.getDatas().get(k)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        devices.setItems(list);

    }

    public void createDataFile(Devices devices) {
        Moshi moshi = new Moshi.Builder().add(new RecordableAdapter()).build();
        JsonAdapter<Devices> jsonAdapter = moshi.adapter(Devices.class);

        String json = jsonAdapter.toJson(devices);
        File data = new File(this.dataFolder.getPath() + "/" + devices.getDevice() + ".json");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(data));
            writer.write(json);
            writer.close();

            FMUnitTool.log.log(devices.getDevice() + " data file was created successfully");
        } catch (IOException e) {
            FMUnitTool.log.log(devices.getDevice() + " data file creation failed: \n" + e.getMessage());
        }
    }

    public Map<String, String> getDatas() {
        return datas;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public File getOutputFolder() {
        return outputFolder;
    }
}
