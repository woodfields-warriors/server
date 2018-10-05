package com.wwttr.models;


public class LeaveResponse{
  private String gameName;
  private String newPlayerNumber;
  private String errorMessage;

  public LeaveResponse(String errorMessage){
    this.errorMessage = errorMessage;
  }
  public LeaveResponse(String gameName, String newPlayerNumber){
    this.gameName = gameName;
    this.newPlayerNumber = newPlayerNumber;
  }
  public String getErrorMessage(){
    return errorMessage;
  }
  public String getGameName(){
    return gameName;
  }
  public String getNewPlayerNumber(){
    return newPlayerNumber;
  }
}
