package com.wwttr.models;

import java.util.*;

public class Game{
	private Integer hostUserID;
	private List<Integer> playerUserIDs;
	private String displayName;
	private Integer maxPlayers;
	private Integer gameID;
	private status gameStatus;
	public enum status{
		PRE,STARTED,ENDED
	}

	public Game(int hostUserID, List<Integer> playerUserIDs, String displayName,
		Integer maxPlayers, Integer gameID){
		this.hostUserID = hostUserID;
		this.playerUserIDs = playerUserIDs;
		this.displayName = displayName;
		this.maxPlayers = maxPlayers;
		this.gameID = gameID;

	}

	public Integer getHostUserID(){
		return hostUserID;
	}

	public List<Integer> getPlayerUserIDs(){
		return playerUserIDs;
	}

	public String getDisplayName(){
		return displayName;
	}
	public Integer getMaxPlayers(){
		return maxPlayers;
	}
	public Integer getGameID(){
		return gameID;
	}
	public void setPlayerUserIDs(List<Integer> players){
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
