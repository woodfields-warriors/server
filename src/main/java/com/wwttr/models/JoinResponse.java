package com.wwttr.models;


public class JoinResponse{
  private String gameName;
  private Integer newPlayerNumber;
  private String errorMessage;

  public JoinResponse(String errorMessage){
    this.errorMessage = errorMessage;
  }
  public JoinResponse(String gameName, Integer newPlayerNumber){
    this.gameName = gameName;
    this.newPlayerNumber = newPlayerNumber;
  }
  public String getGameName(){
    return gameName;
  }
  public Integer getNewPlayerNumber(){
		return newPlayerNumber;
	}
  public String getErrorMessage(){
    return errorMessage;
  }
}
