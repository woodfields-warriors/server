package com.wwttr.models;


public class Player{

  private String playerID;
  private String userID;
  //Game ID that the player is a part of.
  private String gameID;
  private Color playerColor;
  public enum Color{
    UNKOWN, RED, BLUE,GREEN,YELLOW, PURPLE, ORANGE
  }

  public Player(String playerID, String userID, String gameID,Color color){
    this.playerID = playerID;
    this.userID = userID;
    this.gameID = gameID;
    playerColor = color;
  }
  public Player(String playerID, String userID, Color color){
    this.playerID = playerID;
    this.userID = userID;
    playerColor = color;
  }

  public String getPlayerId(){
    return playerID;
  }
  public String getUserId(){
    return userID;
  }
  public String getGameId(){
    return gameID;
  }
  public Player.Color getPlayerColor(){
    return playerColor;
  }
  public void setGameId(String gameID){
    this.gameID = gameID;
  }

}
