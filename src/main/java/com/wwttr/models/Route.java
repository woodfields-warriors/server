package com.wwttr.models;

public class Route {

  private String routeId;
  private String firstCityId;
  // Game ID that the player is a part of.
  private String secondCityId;
  private TrainCard.Color trainColor;
  private String playerId;
  private String gameId;
  private int length;

  public Route(String routeId, String firstCityId, String secondCityId, TrainCard.Color trainColor, int length, String gameId, String playerId) {
    this.routeId = routeId;
    this.firstCityId = firstCityId;
    this.secondCityId = secondCityId;
    this.trainColor = trainColor;
    this.playerId = playerId;
    this.length = length;
    this.gameId = gameId;
  }

  public Route(String routeId, String firstCityId, String secondCityId, TrainCard.Color trainColor, int length, String gameId) {
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

  public TrainCard.Color getTrainColor() {
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
    builder.setId(routeId);
    builder.setFirstCityId(firstCityId);
    builder.setSecondCityId(secondCityId);
    builder.setLength(length);
    builder.setPlayerId(playerId);
    switch (trainColor) {
    case UNSPECIFIED:
      builder.setColor(com.wwttr.card.Api.TrainColor.UNSPECIFIED);
      break;
    case ORANGE: 
        builder.setColor(com.wwttr.card.Api.TrainColor.ORANGE);
        break;
    case PINK: 
        builder.setColor(com.wwttr.card.Api.TrainColor.PINK);
        break;
    case GREEN: 
        builder.setColor(com.wwttr.card.Api.TrainColor.GREEN);
        break;
    case BLUE: 
        builder.setColor(com.wwttr.card.Api.TrainColor.BLUE);
        break;
    case BLACK: 
        builder.setColor(com.wwttr.card.Api.TrainColor.BLACK);
        break;
    case GREY: 
        builder.setColor(com.wwttr.card.Api.TrainColor.GREY);
        break;
    case YELLOW: 
        builder.setColor(com.wwttr.card.Api.TrainColor.YELLOW);
        break;
    case RED: 
        builder.setColor(com.wwttr.card.Api.TrainColor.RED);
        break;
    case WHITE: 
        builder.setColor(com.wwttr.card.Api.TrainColor.WHITE);
        break;
    case RAINBOW: 
        builder.setColor(com.wwttr.card.Api.TrainColor.RAINBOW);
        break;
 }
    builder.setGameId(gameId);
    return builder.build();
  }
}
