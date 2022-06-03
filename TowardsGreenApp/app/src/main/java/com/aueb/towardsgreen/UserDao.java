package com.aueb.towardsgreen;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserDao {
    private File file;

    // Singleton object
    private static UserDao instance = null;

    private final String FILENAME = "user.txt";

    private final String EMAIL = "email";
    private final String PASSWORD = "password";

    private UserDao(Context context) {
        try {
            file = new File(context.getFilesDir(), FILENAME);
            if (!file.exists()) {
                file.createNewFile();

                // Initialize default preferences
                User user = new User();

                saveUser(user);


            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static UserDao getInstance(Context context) {
        if (instance == null) {
            instance = new UserDao(context);
        }

        return instance;
    }

    public void deleteUser() throws JSONException {
        saveUser(new User());
    }

    public void saveUser(User user) throws JSONException {
        JSONObject userJsonObject = new JSONObject();

        userJsonObject.put(EMAIL, user.getEmail() == null ? JSONObject.NULL : user.getEmail());
        userJsonObject.put(PASSWORD, user.getPassword() == null ? JSONObject.NULL : user.getPassword());

        String json = userJsonObject.toString();

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User retrieveUser() {
        User user = null;

        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader reader = new BufferedReader(fileReader);
            String json = reader.readLine();
            reader.close();
            JSONObject userJsonObject = new JSONObject(json);
            Log.i("tag", userJsonObject.toString());
            String email = userJsonObject.getString(EMAIL);
            String password = userJsonObject.getString(PASSWORD);
            user = new User(email, password);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}