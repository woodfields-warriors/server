package com.wwttr.models;

import java.util.*;

public class Game{

	private String hostPlayerID;
	private List<String> playerUserIDs;
	private String displayName;
	private Integer maxPlayers;
	private String gameID;
	private Status gameStatus;
	public enum Status{
		PRE,STARTED,ENDED
	}

	public Game(String hostPlayerID, String displayName,
							Integer maxPlayers, String gameID){
		this.hostPlayerID = hostPlayerID;
		this.playerUserIDs = playerUserIDs;
		this.displayName = displayName;
		this.maxPlayers = maxPlayers;
		this.gameID = gameID;
		this.gameStatus = Status.PRE;

	}


	public String getHostPlayerID(){
		return hostPlayerID;
	}

	public List<String> getPlayerUserIDs(){
		return playerUserIDs;
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
	public void setPlayerUserIDs(List<String> players){
		playerUserIDs = players;
	}
	public void changeGameStatus(Status status){
		this.gameStatus = status;
	}




	/*public void addPlayer(Integer playerUserIDToAdd){
		playerUserIDs.add(playerUserIDtoAdd);
	}
	public void removePlayer(playerUserIDtoRemove){
		for(int i = 0; i < player.UserIDs.length; i++){
			if (playerUserIDtoRemove == player.playerUserIDs[i]){
				playerUserIDs.remove(i);
			}
		}
	}*/


}
