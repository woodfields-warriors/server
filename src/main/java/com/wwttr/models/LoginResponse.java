package com.wwttr.models;

public class LoginResponse {
    private String userID;

    public LoginResponse(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }
}
