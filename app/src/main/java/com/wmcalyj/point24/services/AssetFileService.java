package com.wmcalyj.point24.services;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.wmcalyj.point24.AllGames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mengchaowang on 1/26/17.
 */
public class AssetFileService {
    private static final String TAG = "AssetFileService";
    private static AssetFileService ourInstance = new AssetFileService();
    private static AssetManager assetManager;
    private static String FILE_NAME;

    private AssetFileService() {

    }

    public static AssetFileService loadPreCalculatedAnswers(AssetManager am, String file) {
        assetManager = am;
        FILE_NAME = file;
        if (AllGames.getInstance().isEmpty()) {
            loadAllGames();
        }
        return ourInstance;
    }

    private static void loadAllGames() {
        Gson gson = new Gson();
        try (InputStream fis = assetManager.open(FILE_NAME)) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String jsonString = gson.toJson(gson.fromJson(reader, JsonElement
                    .class));
//            Log.d(TAG, jsonString);
            AllGames.setInstance(gson.fromJson(jsonString, AllGames.class));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
