package com.wwttr.models;

import com.wwttr.models.Game;

public class StartResponse{
  private Game game;
  private String errorMessage;

  public StartResponse(String errorMessage){
    this.errorMessage = errorMessage;
  }
  public StartResponse(Game game){
    this.game = game;
  }
  public String getErrorMessage(){
    return errorMessage;
  }
  public Game getGame(){
    return game;
  }
}
