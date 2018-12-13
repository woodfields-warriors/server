package com.wwttr.models;

import java.io.Serializable;

public class DestinationCard  implements Serializable {

  private static final long serialVersionUID = 65256L;

  private String id;
  private String firstCityId;
  private String secondCityId;
  private Integer pointValue;
  private String playerId;
  private String gameId;

  public DestinationCard(String id, String firstCityId, String secondCityId, Integer pointValue, String playerId, String gameId) {
    this.id = id;
    this.firstCityId = firstCityId;
    this.secondCityId = secondCityId;
    this.pointValue = pointValue;
    this.playerId = playerId;
    this.gameId = gameId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstCityId() {
    return firstCityId;
  }

  public void setFirstCityId(String firstCityId) {
    this.firstCityId = firstCityId;
  }

  public String getSecondCityId() {
    return secondCityId;
  }

  public void setSecondCityId(String secondCityId) {
    this.secondCityId = secondCityId;
  }

  public Integer getPointValue() {
    return pointValue;
  }

  public void setPointValue(Integer pointValue) {
    this.pointValue = pointValue;
  }

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public com.wwttr.card.Api.DestinationCard.Builder createBuilder(){
    com.wwttr.card.Api.DestinationCard.Builder builder = com.wwttr.card.Api.DestinationCard.newBuilder();
    builder.setId(id);
    builder.setFirstCityId(firstCityId);
    builder.setSecondCityId(secondCityId);
    builder.setPointValue(pointValue);
    builder.setPlayerId(playerId);
    return builder;
  }

  public void update(DestinationCard newCard){
    firstCityId = newCard.getFirstCityId();
    secondCityId = newCard.getSecondCityId();
    pointValue = newCard.getPointValue();
    playerId = newCard.getPlayerId();
    gameId = newCard.getGameId();
  }
}
