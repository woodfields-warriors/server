package com.wwttr.models;

import java.util.*;

public class Game{
	private String hostUserID;
	private List<String> playerUserIDs;
	private String displayName;
	private Integer maxPlayers;
	private String gameID;
	private status gameStatus;
	public enum status{
		PRE,STARTED,ENDED
	}

	public Game(String hostUserID, List<String> playerUserIDs, String displayName,
		Integer maxPlayers, String gameID){
		this.hostUserID = hostUserID;
		this.playerUserIDs = playerUserIDs;
		this.displayName = displayName;
		this.maxPlayers = maxPlayers;
		this.gameID = gameID;

	}

	public String getHostUserID(){
		return hostUserID;
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
