package com.aueb.towardsgreen;

import com.aueb.towardsgreen.domain.Profile;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Connection {
    private Gson gson;
    private static Connection connection = null;
    private Socket socket;
    private ObjectInputStream objectIS;
    private ObjectOutputStream objectOS;
    private String address;
    private int port;
    private Profile profile;

    private Connection() {
        this.gson  = new Gson();
        this.address = "10.0.2.2";
        this.port = 8080;
    }

    public static Connection getInstance() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;
    }

    public void connect() {
        try {
            this.socket = new Socket(InetAddress.getByName(this.address), this.port);
            this.objectIS = new ObjectInputStream(socket.getInputStream());
            this.objectOS = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ArrayList<String> requestGetData(Request request) {
        String json = null;
        try {
            objectOS.writeObject(request);
            objectOS.flush();
            json = (String) objectIS.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.gson.fromJson(json, ArrayList.class);
    }

    public boolean requestSendData(Request request) {
        boolean result = false;
        try {
            objectOS.writeObject(request);
            objectOS.flush();
            Request responseRequest = (Request) objectIS.readObject();
            result = gson.fromJson(responseRequest.getContent(), boolean.class);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void requestSendDataWithoutResponse(Request request) {
        try {
            objectOS.writeObject(request);
            objectOS.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
