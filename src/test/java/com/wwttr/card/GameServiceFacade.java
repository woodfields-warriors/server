package com.wwttr.card;

import com.wwttr.api.ApiError;
import com.wwttr.api.NotFoundException;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameServiceFacade {



// Game Service is of the Singleton Pattern

  //singleton object
  private DatabaseFacade database;
  Random rn;

  private static GameServiceFacade gameServiceInstance = null;

  private CardService cardService = CardService.getInstance();

  public static GameServiceFacade getInstance(){
    if(gameServiceInstance == null){
      gameServiceInstance = new GameServiceFacade();
    }
    return gameServiceInstance;
  }

  private GameServiceFacade(){
    database = DatabaseFacade.getInstance();
    rn = new Random();
  }



  /* creates a game with given Name making the given userID the host*/
  /* hostID should have been verified by ServerFacade */
  public CreateResponse createGame(String gameName, String userID, int numberOfPlayers){
    Player player = new Player("p" + Integer.toString(rn.nextInt()) ,userID, Player.Color.RED);
    Game game = new Game(player.getPlayerId(), new ArrayList<String>(), gameName, numberOfPlayers, "game" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE));
    player.setGameId(game.getGameID());
    game.getPlayerIDs().add(player.getPlayerId());
    database.addPlayer(player);
    database.addGame(game);
    CreateResponse toReturn = new CreateResponse(game.getGameID(), player.getPlayerId());
    return toReturn;
  }

  public List<Game> listGames(){
    return database.listGames();
  }

  public Game getGame(String gameID){
    Game game = database.getGame(gameID);
    //  if(game == null){
    //  System.out.println("returning null from game service");
    //  }
    return game;
  }

  public String createPlayer(String userId, String gameId)throws NotFoundException {
    Game game = database.getGame(gameId);
    if (game == null){
      throw new NotFoundException("");
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
    game.getPlayerIDs().add(player.getPlayerId());

    database.addPlayer(player);

    return player.getPlayerId();
  }

  public Player getPlayer(String playerID) {
    return database.getPlayer(playerID);
  }




}

