package com.pandaism.util.file;

import com.pandaism.util.typing.Devices;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CrossReferenceManager {

    private TableView content_pane;
    private String crossReferencePath;
    private Devices devices;
    private File crossReferenceOutputFile;

    public CrossReferenceManager(TableView content_pane, String crossReferencePath, Devices devices) {
        this.content_pane = content_pane;
        this.crossReferencePath = crossReferencePath;
        this.devices = devices;
        this.crossReferenceOutputFile = new File(this.crossReferencePath);

        createCrossreferenceTemplate();
        writeCrossreference();
    }

    private void writeCrossreference() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.crossReferenceOutputFile, true));
            List<ObservableListWrapper<String>> devices = this.content_pane.getItems();
            for (ObservableListWrapper<String> device : devices) {
                for (int j = 0; j < this.devices.getRecordable().length; j++) {
                    String assemblyID = this.devices.getDevice();
                    String assemblySN = device.get(0);
                    String componentID = ((TableColumn) this.content_pane.getColumns().get(j)).getText();

                    writer.write(assemblyID + "\t" + assemblySN + "\t" + componentID + "\t" + device.get(j) + "\t\t\t\t\t\t\t\tN\n");
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCrossreferenceTemplate() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.crossReferenceOutputFile));
            writer.write("Assembly ID\tAssembly SN\tComponent ID\tComponent SN\tOrder ID\tLine Number\tReference\tStart Date\tEnd Date\tRA Reason\tComment\tProcessed Flag\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
