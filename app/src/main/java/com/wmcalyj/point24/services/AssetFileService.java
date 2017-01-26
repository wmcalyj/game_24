package com.wmcalyj.point24.services;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.wmcalyj.point24.AllGames;
import com.wmcalyj.point24.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mengchaowang on 1/26/17.
 */
public class AssetFileService {
    private static final String TAG = "AssetFileService";
    private static AssetFileService ourInstance = new AssetFileService();
    private static Context mContext;

    private AssetFileService() {

    }

    public static AssetFileService loadPreCalculatedAnswers(Context context) {
        if (mContext == null) {
            mContext = context;
        }
        if (AllGames.getInstance().isEmpty()) {
            loadAllGames();
        }
        return ourInstance;
    }

    private static void loadAllGames() {
        AssetManager am = mContext.getAssets();
        Gson gson = new Gson();
        try (InputStream fis = am.open(mContext.getString(R.string
                .FILE_NAME))) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String jsonString = gson.toJson(gson.fromJson(reader, JsonElement
                    .class));
            Log.d(TAG, jsonString);
            AllGames.setInstance(gson.fromJson(jsonString, AllGames.class));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
