package com.wwttr.game;

import java.util.*;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.DeleteResponse;
import com.wwttr.models.JoinResponse;
import com.wwttr.models.LeaveResponse;


// Game Service is of the Singleton Pattern
public class GameService {

  //singleton object
  private DatabaseFacade database;
  Random rn;

  private static GameService gameServiceInstance = null;

  public static GameService getInstance(){
    if(gameServiceInstance == null){
      gameServiceInstance = new GameService();
    }
    return gameServiceInstance;
  }

  public GameService(){
    database = DatabaseFacade.getInstance();
    rn = new Random();
  }



  /* creates a game with given Name making the given userID the host*/
  /* hostID should have been verified by ServerFacade */
  public CreateResponse createGame(String gameName, String hostUserID, int numberOfPlayers){
    if(gameName == null || hostUserID == null || numberOfPlayers < 2
       throw new IllegalArgumentException("Invalid Arguments")
    }
      CreateResponse toReturn = database.createGame(gameName, hostUserID,numberOfPlayers);
      return toReturn;
  }

  public Player joinGame(String userID, String gameID){
    Game game = database.getGame(gameID);
    if(game == null){
      throw new IllegalArgumentException("Couldn't find a game with the given gameID")
    }
    Api.Game.Builder gameBuilder = game.toBuilder();
    List<String> players = gameBuilder.getPlayers();
    Api.Player.Builder playerBuilder = Api.Player.newBuilder();
    playerBuilder.setId("player" + rn.nextInt.toString());
    playerBuilder.setAccountId(userId);
    playerBuilder.setGameId(gameID);
    //need to use players.size() to set a color for the player


  //  builder.build();

    players.add(playerBuilder.build)


    database.updateGame(gameBuilder.build(),gameID);
    String playerID = "player" + rn.nextInt().toString();
    return toReturn;
  }

  public LeaveResponse leaveGame(Integer userID, Integer gameID){
    Game game = database.getGame(gameID);
    if(game == null){
      return new LeaveResponse("Couldn't find a game with the given gameID");
    }
    List<Integer> players = game.getPlayerUserIDs();
    for(int i = 0; i < players.size(); i++){
      if(userID == players.get(i)){
        players.remove(i);
        database.updateGame(game);
        LeaveResponse toReturn = new LeaveResponse(game.getDisplayName(), userID);
      }
    }
    return null;
  }

  public List<Game> listGames(){
    return database.listGames();
  }

  public Game getGame(Integer gameID){
    return database.getGame(gameID);
  }

  public Game startGame(Integer gameID){
    return database.getGame(gameID);
  }

  public DeleteResponse deleteGame(Integer gameID){
    DeleteResponse toReturn = database.deleteGame(gameID);
    return toReturn;
  }


  public Api.Game getGame() {
    Api.Game.Builder builder = Api.Game.newBuilder();
    builder.setId(12);
    builder.setDisplayName("game 1");
    return builder.build();
  }

  public static void main(String[] args) {
    GameService service = new GameService();
    System.out.println(service.getGame());
  }

}
