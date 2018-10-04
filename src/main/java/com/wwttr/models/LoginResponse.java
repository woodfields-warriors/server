package com.wwttr.models;

public class LoginResponse {
    private String userID;
    private String errorCode;

    public LoginResponse(String userID, String errorCode) {
        this.errorCode = errorCode;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
