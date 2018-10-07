package com.wwttr.game;

import java.util.*;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.Player;
import com.wwttr.models.DeleteResponse;
//import com.wwttr.player.Api.Player;



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
  public CreateResponse createGame(String gameName, String userID, int numberOfPlayers){
      Player player = new Player("p" + Integer.toString(rn.nextInt()) ,userID, Player.Color.RED);
      Game game = new Game(player.getPlayerId(), new ArrayList<String>(), gameName, numberOfPlayers, "game" + Integer.toString(rn.nextInt()));
      player.setGameId(game.getGameID());
      database.addPlayer(player);
      database.addGame(game);
      CreateResponse toReturn = new CreateResponse(game.getGameID(), player.getPlayerId());
      return toReturn;
  }

  public List<Game> listGames(){
    return database.listGames();
  }

  public Game getGame(String gameID){
    return database.getGame(gameID);
  }

  public void leaveGame(String playerID, String gameID){
    Game game = database.getGame(gameID);
    List<String> playerIDs = game.getPlayerIDs();
    for (int i = 0; i < playerIDs.size(); i++){
      if (playerIDs.get(i) == playerID){
        playerIDs.remove(i);
        break;
      }
    }
  }



  public Game startGame(String gameID){
    Game game = database.getGame(gameID);
    game.changeGameStatus(Game.Status.STARTED);
    return game;
  }

  public void deleteGame(String gameID){
    database.deleteGame(gameID);
  }

  public String createPlayer(String userId, String gameId){
    Game game = database.getGame(gameId);
    if (game == null){
      //throw
    }
    int currentNumberofPlayers = game.getPlayerIDs().size();
    // plus one because we are adding a player to the game and his/her color
    //will be the next color in the list
    Player.Color playerColor = Player.Color.UNKOWN;
    //assigne player their given color.  Color goes in join order
    //i.e. player who creates gets RED
    // first player to joing get BLUE
    // second player gets GREEN
    //so on
    switch (currentNumberofPlayers +1){
      case 2: playerColor = Player.Color.BLUE;
              break;
      case 3: playerColor = Player.Color.GREEN;
              break;
      case 4: playerColor = Player.Color.YELLOW;
              break;
      case 5: playerColor = Player.Color.PURPLE;
              break;
      case 6: playerColor = Player.Color.ORANGE;
              break;
    }
    Player player = new Player("p" + Integer.toString(rn.nextInt()),userId, playerColor);
    player.setGameId(game.getGameID());
    return player.getPlayerId();
  }

/*      ---  DEPRECATED ---- /
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

    players.add(playerBuilder.build)
    database.updateGame(gameBuilder.build(),gameID);
    String playerID = "player" + rn.nextInt().toString();
    return toReturn;

  //   Game game = database.getGame(gameID);
  //   if(game == null){
  //     throw new IllegalArgumentException("Couldn't find a game with the given gameID");
  //   }
  //   Api.Game.Builder gameBuilder = game.toBuilder();
  //   List<String> players = gameBuilder.getPlayers();
  //   Api.Player.Builder playerBuilder = Api.Player.newBuilder();
  //   playerBuilder.setId("player" + rn.nextInt.toString());
  //   playerBuilder.setAccountId(userId);
  //   playerBuilder.setGameId(gameID);
  //   //need to use players.size() to set a color for the player
  //
  //
  // //  builder.build();
  //
  //   players.add(playerBuilder.build);
  //
  //
  //   database.updateGame(gameBuilder.build(),gameID);
  //   String playerID = "player" + rn.nextInt().toString();
  //   return toReturn;
  return null;
  }

  public LeaveResponse leaveGame(String userID, String gameID){
    Game game = database.getGame(gameID);
    if(game == null){
      return new LeaveResponse("Couldn't find a game with the given gameID");
    }
    List<String> players = game.getPlayerUserIDs();
    for(int i = 0; i < players.size(); i++){
      if(userID == players.get(i)){
        players.remove(i);
        // database.updateGame(game);
        LeaveResponse toReturn = new LeaveResponse(game.getDisplayName(), userID);
      }
    }
    return null;
  }
// -----------------*/

  public Api.Game getGame() {
    Api.Game.Builder builder = Api.Game.newBuilder();
    // builder.setId(12);
    builder.setDisplayName("game 1");
    return builder.build();
  }

  public static void main(String[] args) {
    GameService service = new GameService();
    System.out.println(service.getGame());
  }

}
