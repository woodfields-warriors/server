package com.wwttr.models;

public class Message {
  private String id;
  private String content;
  private String playerId;
  private String gameId;
  private int timestamp;

  public Message(String messageId, String messageContent, String playerId,
   String gameId, int timestamp){
    this.id = messageId;
    this.content = messageContent;
    this.playerId = playerId;
    this.gameId = gameId;
    this.timestamp = timestamp;
  }

  public String getMessageId(){
    return id;
  }
  public String getContent(){
    return content;
  }
  public String getPlayerId(){
    return playerId;
  }
  public int getTimestamp(){
    return timestamp;
  }
  public String getGameId(){
    return gameId;
  }
  public void setMessageId(String messageId){
    this.id = messageId;
  }
  public void setContent(String messageContent){
    this.content = messageContent;
  }
  public void setPlayerId(String playerId){
    this.playerId = playerId;
  }
  public void setGameId(String gameId){
    this.gameId = gameId;
  }
  public void setTimestamp(int time){
    this.timestamp = time;
  }

  public com.wwttr.chat.Api.Message.Builder createBuilder(){
    com.wwttr.chat.Api.Message.Builder builder = com.wwttr.chat.Api.Message.newBuilder();
    builder.setMessageId(id);
    builder.setContent(content);
    builder.setPlayerId(playerId);
    builder.setTimestamp(timestamp);
    return builder;
  }
}
