package com.wwttr.models;

public class Route {

  private String routeId;
  private String firstCityId;
  // Game ID that the player is a part of.
  private String secondCityId;
  private TrainColor trainColor;
  private String playerId;

  public Route(String routeId, String firstCityId, String secondCityId, TrainColor trainColor, String playerId) {
    this.routeId = routeId;
    this.firstCityId = firstCityId;
    this.secondCityId = secondCityId;
    this.trainColor = trainColor;
    this.playerId = playerId;
  }

  public Route(String routeId, String firstCityId, String secondCityId, TrainColor trainColor) {
    this.routeId = routeId;
    this.firstCityId = firstCityId;
    this.secondCityId = secondCityId;
    this.trainColor = trainColor;
  }

  public String getRouteId() {
    return routeId;
  }

  public String getPlayerId() {
    return playerId;
  }

  public String getFirstCityId() {
    return firstCityId;
  }

  public String getSecondCityId() {
    return secondCityId;
  }

  public TrainColor getTrainColor() {
    return trainColor;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

}
