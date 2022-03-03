package com.pandaism.util.thread;

import com.pandaism.FMUnitTool;
import com.pandaism.gui.controller.NewProjectController;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GoogleLookupThread implements Runnable {

    private TextField address;
    private TextField timezone;
    private NewProjectController newProjectController;

    public GoogleLookupThread(TextField address, TextField timezone, NewProjectController newProjectController) {
        this.address = address;
        this.timezone = timezone;
        this.newProjectController = newProjectController;
    }
    @Override
    public void run() {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + this.address.getText().replaceAll(" ", "+") + "&key=" + FMUnitTool.settings.getGoogleAPIKey());
            urlConnection = (HttpURLConnection) url.openConnection();

            FMUnitTool.log.log("Attempting to retrieve connection to: " + url);

            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            int status = urlConnection.getResponseCode();
            BufferedReader reader;
            StringBuilder sb = new StringBuilder();
            String line;

            if(status < 300) {
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<Place> placeJsonAdapter = moshi.adapter(Place.class);

                Place place = placeJsonAdapter.fromJson(sb.toString());
                if(place != null) {
                    String latitude = place.results.get(0).geometry.location.latitude;
                    String longitude = place.results.get(0).geometry.location.longitude;
                    this.newProjectController.setLatitude(latitude);
                    this.newProjectController.setLongitude(longitude);

                    url = new URL("https://maps.googleapis.com/maps/api/timezone/json" +
                            "?location=" + latitude + "%2C" + longitude +
                            "&timestamp=" + System.currentTimeMillis() / 1000 +
                            "&key=" + FMUnitTool.settings.getGoogleAPIKey());
                    urlConnection = (HttpURLConnection) url.openConnection();

                    FMUnitTool.log.log("Attempting to retrieve connection to: " + url);

                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);

                    status = urlConnection.getResponseCode();
                    sb = new StringBuilder();

                    if(status < 300) {
                        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        while((line = reader.readLine()) != null) {
                            sb.append(line);
                        }

                        JsonAdapter<Timezone> timezoneJsonAdapter = moshi.adapter(Timezone.class);
                        Timezone timezone = timezoneJsonAdapter.fromJson(sb.toString());

                        if(timezone != null) {
                            this.newProjectController.setTimezoneString(timezone.timeZoneId);
                            this.timezone.setText(timezone.timeZoneName);
                        }

                    } else {
                        reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                        line = reader.readLine();

                        JOptionPane.showConfirmDialog(null, "Invalid TimeZone" + line, "Error", JOptionPane.OK_OPTION);
                        FMUnitTool.log.log(line);
                    }
                }

            } else {
                reader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                line = reader.readLine();

                JOptionPane.showConfirmDialog(null, "Invalid Address" + line, "Error", JOptionPane.OK_OPTION);
                FMUnitTool.log.log(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }

    }

    public static class Timezone {
        private String timeZoneId;
        private String timeZoneName;
    }

    public static class Place {
        private List<Results> results;
    }

    public static class Results {
        private Geometry geometry;
    }

    public static class Geometry {
        private Location location;
    }

    public static class Location {
        @Json(name = "lat")
        private String latitude;
        @Json(name = "lng")
        private String longitude;
    }
}
