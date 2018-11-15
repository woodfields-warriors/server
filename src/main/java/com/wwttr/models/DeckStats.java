package com.wwttr.models;

public class DeckStats {

  private Integer hiddenTrainCardCount;
  private Integer hiddenDestinationCardCount;
  private String gameId;

  public DeckStats(Integer hiddenTrainCardCount, Integer hiddenDestinationCardCount, String gameId) {
    this.hiddenTrainCardCount = hiddenTrainCardCount;
    this.hiddenDestinationCardCount = hiddenDestinationCardCount;
    this.gameId = gameId;
  }

  public Integer getHiddenTrainCardCount() {
    return hiddenTrainCardCount;
  }

  public void setHiddenTrainCardCount(Integer hiddenTrainCardCount) {
    this.hiddenTrainCardCount = hiddenTrainCardCount;
  }

  public Integer getHiddenDestinationCardCount() {
    return hiddenDestinationCardCount;
  }

  public void setHiddenDestinationCardCount(Integer hiddenDestinationCardCount) {
    this.hiddenDestinationCardCount = hiddenDestinationCardCount;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public com.wwttr.card.Api.DeckStats.Builder createBuilder() {
    com.wwttr.card.Api.DeckStats.Builder builder = com.wwttr.card.Api.DeckStats.newBuilder();
    builder.setHiddenTrainCardCount(hiddenTrainCardCount);
    builder.setHiddenDestinationCardCount(hiddenDestinationCardCount);
    return builder;
  }
}
