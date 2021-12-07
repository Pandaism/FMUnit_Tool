package com.pandaism.util.typing;

import com.pandaism.FMUnitTool;
import javafx.beans.property.SimpleBooleanProperty;

public class Devices {
    private String device;
    private Recordable[] recordable;
    private String description;

    public Devices(String device, Recordable[] recordable, String description) {
        this.device = device;
        this.recordable = recordable;
        this.description = description;
    }

    public String getDevice() {
        return device;
    }

    public Recordable[] getRecordable() {
        return recordable;
    }

    public String getDescription() {
        return description;
    }

    public Recordable match(String string) {
        for(Recordable recordable : this.recordable) {
            if(recordable.getField().equalsIgnoreCase(string)) {
                return recordable;
            }
        }

        FMUnitTool.log.log("ERROR: Recordable [" + string + "] not found in " + this.device);
        return null;
    }

    public static class Recordable {
        private String field = "Click here to edit";
        private SimpleBooleanProperty serialized = new SimpleBooleanProperty(false);

        public Recordable() {

        }

        public Recordable(String field, SimpleBooleanProperty serialized) {
            this.field = field;
            this.serialized = serialized;
        }

        public String getField() {
            return field;
        }

        public SimpleBooleanProperty serializedProperty() {
            return serialized;
        }

        public void setField(String field) {
            this.field = field;
        }

        public void setSerialized(boolean serialized) {
            this.serialized.set(serialized);
        }
    }
}
