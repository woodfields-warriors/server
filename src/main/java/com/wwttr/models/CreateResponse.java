package com.wwttr.models;


public class CreateResponse{
	private String errorMessage;
	private String gameName;
	private Integer totalPlayers;

	public CreateResponse(String errorMessage){
		this.errorMessage = errorMessage;
	}
	public CreateResponse(String gameName, Integer totalPlayers){
		this.gameName = gameName;
		this.totalPlayers = totalPlayers;
	}
	public String getGameName(){
		return gameName;
	}
	public Integer getTotalPlayers(){
		return totalPlayers;
	}
	public String getErrorMessage(){
		return errorMessage;
	}
}
