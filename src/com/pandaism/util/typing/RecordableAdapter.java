package com.pandaism.util.typing;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordableAdapter {
    @ToJson
    public String toJson(Devices.Recordable recordable) {
        return recordable.getField() + "<" + recordable.serializedProperty().get() + ">";
    }

    @FromJson
    public Devices.Recordable fromJson(String recordable) {
        Pattern pattern = Pattern.compile("(\\w+((\\s)|-)*(\\w)*)(<(true|false)>)");
        Matcher matcher = pattern.matcher(recordable);

        if(matcher.matches()) {
            String field = matcher.group(1);
            String match = matcher.group(5);
            String substring = match.substring(1, match.length() - 1);
            boolean bool = Boolean.parseBoolean(substring);

            return new Devices.Recordable(field, new SimpleBooleanProperty(bool));
        }

        return null;
    }
}
