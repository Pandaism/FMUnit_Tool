package com.pandaism.util.thread;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.TimeZone;

public class TimeThread implements Runnable {
    private final Label timeLabel;
    private final String timezone;

    public TimeThread(Label timeLabel, String timezone) {
        this.timeLabel = timeLabel;
        this.timezone = timezone;

        Arrays.stream(TimeZone.getAvailableIDs()).forEach(System.out::println);
    }

    @Override
    public void run() {
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(this.timezone));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Platform.runLater(() -> this.timeLabel.setText(df.format(zonedDateTime)));
    }
}
