package com.wwttr.models;

public class DeleteResponse{
	private String errorMessage;
	private String gameName;
	private List<Integer> orphanedUsers;

	public DeleteResponse(String errorMessage){
		this.errorMessage = errorMessage;
	}
	public DeleteResponse(String gameName, List<Integer> orphanedUsers){
		this.orphanedUsers = orphanedUsers;
		this.gameName = gameName;
	}

	public String getGameName(){
		return gameName;
	}
	public List<Integer> getOrphanedUsers(){
		return orphanedUsers;
	}
	public String getErrorMessage(){
		return errorMessage;
	}

}




