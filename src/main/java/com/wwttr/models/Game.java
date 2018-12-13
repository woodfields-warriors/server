package com.wwttr.models;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {

	private static final long serialVersionUID = 43226L;

	private String hostPlayerID;
	private List<String> playerIDs;
	private String displayName;
	private Integer maxPlayers;
	private String gameID;
	private Status gameStatus;
	public enum Status{
		PRE,STARTED,ENDED,LASTROUND
	}

	public Game(String hostPlayerID, List<String> playerIDs, String displayName,
							Integer maxPlayers, String gameID){
		this.hostPlayerID = hostPlayerID;
		this.playerIDs = playerIDs;
		this.displayName = displayName;
		this.maxPlayers = maxPlayers;
		this.gameID = gameID;
		this.gameStatus = Status.PRE;

	}


	public String getHostPlayerID(){
		return hostPlayerID;
	}

	public List<String> getPlayerIDs(){
		return playerIDs;
	}

	public String getDisplayName(){
		return displayName;
	}
	public Integer getMaxPlayers(){
		return maxPlayers;
	}
	public String getGameID(){
		return gameID;
	}
	public void setplayerIDs(List<String> players){
		playerIDs = players;
	}
	public void changeGameStatus(Status status){
		this.gameStatus = status;
	}
	public Status getGameStatus() {
		return gameStatus;
	}


	/*public void addPlayer(Integer playerIDToAdd){
		playerIDs.add(playerIDtoAdd);
	}
	public void removePlayer(playerIDtoRemove){
		for(int i = 0; i < player.UserIDs.length; i++){
			if (playerIDtoRemove == player.playerIDs[i]){
				playerIDs.remove(i);
			}
		}
	}*/


}
