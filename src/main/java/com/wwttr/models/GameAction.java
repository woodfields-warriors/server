package com.wwttr.models;


public class GameAction{
	private String action;
	private int timestamp;
	private String actionId;
	private String gameId;
	private String playerId;

	public GameAction(String actionId, String action, String playerId, String gameId, int timestamp){
		this.actionId = actionId;
		this.action = action;
		this.timestamp = timestamp;
		this.gameId = gameId;
		this.playerId = playerId;
	}

	public String getActionId(){
		return actionId;
	}
	public int getTimestamp(){
		return timestamp;
	}
	public String getAction(){
		return action;
	}
	public String getGameId(){
		return gameId;
	}
	public String getPlayerId(){
		return playerId;
	}
	public void setActionId(String actionId){
		this.actionId = actionId;
	}
	public void setTimestamp(int timestamp){
		this.timestamp = timestamp;
	}
	public void setAction(String action){
		this.action = action;
	}
	public void setGameId(String gameId){
		this.gameId = gameId;
	}

	public com.wwttr.game.Api.GameAction.Builder createBuilder(){
		com.wwttr.game.Api.GameAction.Builder builder = com.wwttr.game.Api.GameAction.newBuilder();
		builder.setActionId(actionId);
		builder.setTimestamp(timestamp);
		builder.setAction(action);
		builder.setPlayerId(playerId);
		return builder;
	}
}
