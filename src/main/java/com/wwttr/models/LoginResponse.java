package com.wwttr.models;

public class LoginResponse {
    private Integer userID;
    private String errorCode;

    public LoginResponse(String errorCode) {
        this.errorCode = errorCode;
    }

    public LoginResponse(Integer userID) {

        this.userID = userID;
    }

    public Integer getUserID() {
        return userID;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
