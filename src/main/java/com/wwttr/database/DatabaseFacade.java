package com.wwttr.database;

import java.util.ArrayList;
import java.util.Random;

import com.wwttr.models.User;
import com.wwttr.models.Game;


public class DatabaseFacade {
    ArrayList<User> Users;
    ArrayList<Game> Games;
    Random rn = new Random();
    static private DatabaseFacade instance;

    public User getUser(String username) {
        for (User temp : Users){
            if( temp.getUsername().equals(username)){
                return temp;
            }
        }
        return null;
    }

    public User makeUser(String username, String password){
        if(getUser(username) != null){
            throw new IllegalArgumentException("User already exists");
        }
        else{
            Integer tempID = rn.nextInt();
            while(getUserByID(tempID) != null)
                tempID = rn.nextInt();
            User temp = new User(username, password, tempID);
            Users.add(temp);
            return temp;
        }
    }

    public User getUserByID(Integer ID){
        for(User temp : Users) {
            if(temp.getUserID().equals(ID)){
                return temp;
            }
        }
        return null;
    }

    //***********************************************************************************//
    //-------------------------------Game Service Methods------------------------------------

    public Game getGame(Integer gameID){
        for(int i = 0; i < Games.length; i++){
            if(Games[i].getGameID() == gameID){
                return Games[i];
            }
        }
    }

    public List<Game> listGames(){
        return Games;
    }

    public CreateResponse createGame(String gameName, String hostUserID, Integer numberOfPlayers){
        //arguments checked in GameService
        Game game = new Game(hostUserID, new ArrayList(), gameName, numberOfPlayers, rn.nextInt());
        CreateResponse toReturn = new CreateResponse(game.getDisplayName(),game.getMaxPlayers());
        return toReturn;
    }

    public void updateGame(Game game){

    }

    public DeleteResponse deleteGame(Integer gameID){
        for (int i = 0; i < Games.length; i++){
            if(Games[i].getGameID() == gameID){
                List<Integer> players = Games[i].getPlayerUserIDs();
                String gameName = Games[i].getDisplayName();
                DeleteResponse toReturn = new DeleteResponse(gameName,players);
                Games.remove(i);
                return toReturn;
            }
        }
        DeleteResponse toReturn = new DeleteResponse("Couldn't find game with given GameID")
    }

    /*
    + getGame(gameID:String):Game
     + listGames():List<Game>
    + createGame(gameName:String, hostUserID:String, numPlayers:int):CreateResponse
     + updateGame(game: Game)
    + deleteGame(gameID: string):DeleteResponse

*/

    private DatabaseFacade(){

    }

    public static DatabaseFacade getInstance(){
        if(instance == null){
            instance = new DatabaseFacade();
        }
        return instance;
    }

    public static void main(String[] args) {
        DatabaseFacade service = new DatabaseFacade();
    }
}
