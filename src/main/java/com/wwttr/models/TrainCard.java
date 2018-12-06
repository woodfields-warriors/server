package com.wwttr.models;

import java.io.Serializable;

public class TrainCard  implements Serializable {

  private static final long serialversionUID = 46407L;

  private String id;
  private String gameId;
  private String playerId;
  private Color color;
  private State state;
  public enum Color {
    UNSPECIFIED, ORANGE, PINK, GREEN, BLUE, BLACK, GREY, YELLOW, RED, WHITE, RAINBOW
  }
  public  enum State {
    UNSPECIFIED, HIDDEN, VISIBLE, OWNED
  }

  public TrainCard(String id, String gameId, String playerId, Color color, State state) {
    this.id = id;
    this.gameId = gameId;
    this.playerId = playerId;
    this.color = color;
    this.state = state;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public com.wwttr.card.Api.TrainCard.Builder createBuilder(){
    com.wwttr.card.Api.TrainCard.Builder builder = com.wwttr.card.Api.TrainCard.newBuilder();
    builder.setId(id);
    builder.setPlayerId(playerId);
    switch (color){
      case UNSPECIFIED:{
        builder.setColor(com.wwttr.card.Api.TrainColor.UNSPECIFIED);
        break;
      }
      case ORANGE:{
        builder.setColor(com.wwttr.card.Api.TrainColor.ORANGE);
        break;
      }
      case PINK:{
        builder.setColor(com.wwttr.card.Api.TrainColor.PINK);
        break;
      }
      case GREEN:{
        builder.setColor(com.wwttr.card.Api.TrainColor.GREEN);
        break;
      }
      case BLUE:{
        builder.setColor(com.wwttr.card.Api.TrainColor.BLUE);
        break;
      }
      case BLACK:{
        builder.setColor(com.wwttr.card.Api.TrainColor.BLACK);
        break;
      }
      case GREY:{
        builder.setColor(com.wwttr.card.Api.TrainColor.GREY);
        break;
      }
      case YELLOW:{
        builder.setColor(com.wwttr.card.Api.TrainColor.YELLOW);
        break;
      }
      case RED:{
        builder.setColor(com.wwttr.card.Api.TrainColor.RED);
        break;
      }
      case WHITE:{
        builder.setColor(com.wwttr.card.Api.TrainColor.WHITE);
        break;
      }
      case RAINBOW:{
        builder.setColor(com.wwttr.card.Api.TrainColor.RAINBOW);
        break;
      }
    }
    switch (state){
      case HIDDEN:{
        builder.setState(com.wwttr.card.Api.TrainCard.State.HIDDEN);
        break;
      }
      case VISIBLE:{
        builder.setState(com.wwttr.card.Api.TrainCard.State.VISIBLE);
        break;
      }
      case OWNED:{
        builder.setState(com.wwttr.card.Api.TrainCard.State.OWNED);
        break;
      }
      default: {
        builder.setState(com.wwttr.card.Api.TrainCard.State.UNSPECIFIED);
        break;
      }
    }
    return builder;
  }

  public void update(TrainCard newCard){
    id = newCard.getId();
    playerId = newCard.getPlayerId();
    color = newCard.getColor();
    gameId = newCard.getGameId();
    state = newCard.getState();
  }
}
