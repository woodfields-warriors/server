package com.wwttr.models;

public class Route {

  private String routeId;
  private String firstCityId;
  // Game ID that the player is a part of.
  private String secondCityId;
  private TrainColor trainColor;
  private String playerId;
  private String gameId;

  public Route(String routeId, String firstCityId, String secondCityId, TrainColor trainColor, int length, String gameId, String playerId) {
    this.routeId = routeId;
    this.firstCityId = firstCityId;
    this.secondCityId = secondCityId;
    this.trainColor = trainColor;
    this.playerId = playerId;
    this.length = length;
    this.gameId = gameId;
  }

  public Route(String routeId, String firstCityId, String secondCityId, TrainColor trainColor, int length, String gameId) {
    this.routeId = routeId;
    this.firstCityId = firstCityId;
    this.secondCityId = secondCityId;
    this.trainColor = trainColor;
    this.length = length;
    this.gameId = gameId;
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

  public int getLength() {
    return length;
  }

  public String getGameId() {
    return gameId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public com.wwttr.route.Api.Route toProto() {
    com.wwttr.route.Api.Route.Builder builder = com.wwttr.route.Api.Route.newBuilder();
    builder.setId(id);
    builder.setFirstCityId(firstCityId);
    builder.setSecondCityId(secondCityId);
    builder.setLength(length);
    builder.setPlayerId(playerId);
    builder.setTrainColor(trainColor);
    builder.setGameId(gameId);
    return builder.build();
  }
}
