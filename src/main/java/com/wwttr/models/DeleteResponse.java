package com.wwttr.models;

import java.util.*;


public class DeleteResponse{
	private String errorMessage;
	private String gameName;
	private List<String> orphanedUsers;

	public DeleteResponse(String errorMessage){
		this.errorMessage = errorMessage;
	}
	public DeleteResponse(String gameName, List<String> orphanedUsers){
		this.orphanedUsers = orphanedUsers;
		this.gameName = gameName;
	}

	public String getGameName(){
		return gameName;
	}
	public List<String> getOrphanedUsers(){
		return orphanedUsers;
	}
	public String getErrorMessage(){
		return errorMessage;
	}

}
