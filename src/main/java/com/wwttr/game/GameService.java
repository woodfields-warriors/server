package com.wwttr.game;


import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;

// Game Service is of the Singleton Pattern
public class GameService {

  //singleton object
  private DatabaseFacade database;

  private static GameService gameServiceInstance = null;

  public static GameService getInstance(){
    if(gameServiceInstance == null){
      gameServiceInstance = new GameService();
    }
    return gameServiceInstance;
  }

  public GameService(){
    database = DatabaseFacade.getInstance();
  }



  /* creates a game with given Name making the given userID the host*/
  /* hostID should have been verified by ServerFacade */
  public CreateResponse createGame(String gameName, String hostUserID, int numberOfPlayers){
    if(gameName == null || hostUserID == null || numberOfPlayers == null || numberOfPlayers < 2
       || numberOfPlayers > 6){
      CreateResponse toReturn = new CreateResponse("Invalid Arguments");
      return toReturn;
    }
    try{
      CreateResponse toReturn = database.createGame(gameName, hostUserID,numberOfPlayers);
      return toReturn;
     }
    catch(Exception e){
      CreateResponse toReturn = new CreateResponse(e.getMessage());
      return toReturn;
    } 
    
  }

  public JoinResponse joinGame(int userID, String gameID){

  }

  public LeaveResponse leaveGame(String userID, String gameID){

  }

  public List<Game> listGames(){

  }

  public Game getGame(String gameID){

  }

  public Game startGame(String gameID){

  }

  public DeleteResponse deleteGame(String gameID){
    DeleteResponse toReturn = database.deleteGame(gameID);
    return toReturn;
  }

   /* DATABASE FACADE METHODS

  + getGame(gameID:String):Game + listGames():List<Game>
  + createGame(gameName:String, hostUserID:String, numPlayers:int):CreateResponse
  + updateGame(game: Game)
  + deleteGame(gameID: string):DeleteResponse
  + getUser(username:String, password: String):User
  + makeUser(username:String, password:String):User

    */



  public Api.Game getGame() {
    Api.Game.Builder builder = Api.Game.newBuilder();
    builder.setId("abcdefgh");
    builder.setDisplayName("game 1");
    return builder.build();
  }

  public static void main(String[] args) {
    GameService service = new GameService();
    System.out.println(service.getGame());
  }

}
