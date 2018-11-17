package com.wwttr.models;


public class PlayerStats{
  String playerId;
  int trainCardPoints;
  int longestRoutePoints;
  int destinationCardPoints;
  int trainCount;
  int trainCardCount;
  int destinationCardCount;
  public enum PlayerTurnState{
    UNSPECIFIED_PLAYER_TURN_STATE,
    PENDING,
    START,
    MID,
  }

  public PlayerStats(){

  }
  public String getPlayerId(){
    return playerId;
  }
  public int getTrainCardPoints(){
    return trainCardPoints;
  }
  public int getLongestRoutePoints(){
    return longestRoutePoints;
  }
  public int getDestinationCardPoints(){
    return destinationCardPoints;
  }
  public int getTrainCount(){
    return trainCount;
  }
  public int getTrainCardCount(){
    return trainCardCount;
  }
  public int getDestinationCardCount(){
    return destinationCardCount;
  }


  //----setters----//

  public void setPlayerId(String playerId){
    this.playerId = playerId;
  }
  public void setTrainCardPoints(int trainCardPoints){
      this.trainCardPoints = trainCardPoints;
  }
  public void setLongestRoutePoints(int longestRoutePoints){
    this.longestRoutePoints = longestRoutePoints;
  }
  public void setDestinationCardPoints(int destinationCardPoints){
    this.destinationCardPoints = destinationCardPoints;
  }
  public void setTrainCount(int trainCount){
    this.trainCount = trainCount;
  }
  public void setTrainCardCount(int trainCardCount){
    this.trainCardCount = trainCardCount;
  }
  public void setDestinationCardCount(int destinationCardCount){
    this.destinationCardCount = destinationCardCount;
  }
}
