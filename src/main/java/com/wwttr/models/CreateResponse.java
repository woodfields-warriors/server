package com.wwttr.models;


public class CreateResponse{
	private String gameID;
	private String playerID;


	public CreateResponse(String gameID, String playerID){
		this.gameID = gameID;
		this.playerID = playerID;
	}
	public String getGameID(){
		return gameID;
	}
	public String getPlayerID(){
		return playerID;
	}

}
