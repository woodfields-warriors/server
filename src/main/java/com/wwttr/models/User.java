package com.wwttr.models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String username;
    private String password;
    private String userID;
    private ArrayList<Game> gameArray;

    public User(String username, String password, String userID) {
        this.username = username;
        this.password = password;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }

    public ArrayList<Game> getGameArray() {
        return gameArray;
    }

    public String getPassword() {
        return password;
    }

    public void setGameArray(ArrayList<Game> gameArray) {
        this.gameArray = gameArray;
    }
}
