package com.wwttr.models;


public class LeaveResponse{
  private String gameName;
  private Integer newPlayerNumber;
  private String errorMessage;

  public LeaveResponse(String errorMessage){
    this.errorMessage = errorMessage;
  }
  public LeaveResponse(String gameName, Integer newPlayerNumber){
    this.gameName = gameName;
    this.newPlayerNumber = newPlayerNumber;
  }
  public String getErrorMessage(){
    return errorMessage;
  }
  public String getGameName(){
    return gameName;
  }
  public Integer getNewPlayerNumber(){
    return newPlayerNumber;
  }
}
