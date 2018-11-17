package com.wwttr.models;

public class Player{

  private String playerID;
  private String userID;
  private String username;
  //Game ID that the player is a part of.
  private String gameID;
  private int points;
  private Color playerColor;

  private IPlayerTurnState state;

  public enum Color{
    UNKOWN, RED, BLUE,GREEN,YELLOW, PURPLE, ORANGE
  }

  public Player(String playerID, String userID, String gameID,Color color, String username){
    this.playerID = playerID;
    this.userID = userID;
    this.gameID = gameID;
    this.playerColor = color;
    this.username = username;
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
  public String getUsername(){
    return username;
  }
  public void setGameId(String gameID){
    this.gameID = gameID;
  }

  public void setState(IPlayerTurnState state) {
    this.state = state;
  }
}
