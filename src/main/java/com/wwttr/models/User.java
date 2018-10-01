package com.wwttr.models;

public class User {
    private String username;
    private String password;
    private Integer userID;
    private ArrayList<Game> gameArray;

    public User(String username, String password, Integer userID) {
        this.username = username;
        this.password = password;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public Integer getUserID() {
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
