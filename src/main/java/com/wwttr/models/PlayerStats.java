package com.wwttr.models;


public class PlayerStats{
  String playerId;
  int routePoints;
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
  public int getroutePoints(){
    return routePoints;
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
  public void setroutePoints(int routePoints){
      this.routePoints = routePoints;
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
