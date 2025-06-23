package com.example.controlescolar;

import android.content.Context;

import com.google.gson.Gson;

import java.io.InputStream;

public class JSONHelper {
    private Context context;

    public JSONHelper(Context context) {
        this.context = context;
    }

    public Data readDataFromAssets() {
        try {
            InputStream inputStream = context.getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            Gson gson = new Gson();
            return gson.fromJson(json, Data.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
