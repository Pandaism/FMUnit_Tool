package com.pandaism.util.thread;

import com.pandaism.FMUnitTool;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.scene.control.TextField;

import java.io.IOException;

public class GooglePlaceLookupThread implements Runnable {

    private TextField address;
    private TextField timezone;

    public GooglePlaceLookupThread(TextField address, TextField timezone) {
        this.address = address;
        this.timezone = timezone;
    }
    @Override
    public void run() {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + this.address.getText().replaceAll(" ", "+") + "&key=" + FMUnitTool.settings.getGoogleAPIKey();
        Request request = new Request.Builder().url(url).method("GET", null).build();

        try {
            Response response = FMUnitTool.httpClient.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
