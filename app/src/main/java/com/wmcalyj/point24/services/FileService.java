package com.wmcalyj.point24.services;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.wmcalyj.point24.AllGames;
import com.wmcalyj.point24.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mengchaowang on 1/26/17.
 */

public class FileService {
    private static final String MyTag = "FileServiceTag";
    private static FileService instance;

    private FileService() {
        // Nothing
    }

    public static FileService getInstance() {
        if (instance == null) {
            instance = new FileService();
        }
        return instance;
    }

    public void saveAllGameResults(Context context) {
        try (FileOutputStream fos = context.openFileOutput(context.getString(R.string.FILE_NAME),
                Context.MODE_PRIVATE)) {
            String resultString = AllGames.getInstance().toGsonString();
            Log.d(MyTag, "Save all Id Strings" + resultString);
            fos.write(resultString.getBytes());
        } catch (IOException e) {
            Log.e(MyTag, e.getMessage());
        }
    }


    public AllGames loadAllGameResults(Context context) {
        Gson gson = new Gson();
        try (FileInputStream fis = context.openFileInput(context.getString(R.string.FILE_NAME))) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String jsonString = gson.toJson(gson.fromJson(reader, JsonElement
                    .class));
            Log.d(MyTag, "Reading json string: " + jsonString);
            return gson.fromJson(jsonString, AllGames.class);
        } catch (Exception e) {
            Log.e(MyTag, e.getMessage());
        }
        return null;
    }
}
