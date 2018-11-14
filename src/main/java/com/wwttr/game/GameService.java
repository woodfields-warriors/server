package com.wwttr.game;

import java.util.*;

import com.wwttr.api.ApiError;
import com.wwttr.card.CardService;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.Player;
import com.wwttr.models.User;
import com.wwttr.models.GameAction;
import com.wwttr.models.DeleteResponse;
import com.wwttr.api.NotFoundException;
//import com.wwttr.player.Api.Player;
import com.wwttr.api.Code;
import java.util.stream.*;




// Game Service is of the Singleton Pattern
public class GameService {

  //singleton object
  private DatabaseFacade database;
  Random rn;
  private List<GameListener> gameListeners = new LinkedList<GameListener>();

  private static GameService gameServiceInstance = null;

  private CardService cardService = CardService.getInstance();

  public static GameService getInstance(){
    if(gameServiceInstance == null){
      gameServiceInstance = new GameService();
    }
    return gameServiceInstance;
  }

  private GameService(){
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

  public void leaveGame(String playerID, String gameID){
    Game game = database.getGame(gameID);
    List<String> playerIDs = game.getPlayerIDs();
    if(playerIDs.size() == 0 || playerID.equals(game.getHostPlayerID())){
      deleteGame(game.getGameID());
    }
    else {
      for (int i = 0; i < playerIDs.size(); i++) {
        if (playerIDs.get(i) == playerID) {
          playerIDs.remove(i);
          break;
        }
      }
    }
  }


  public Game startGame(String gameID) throws NotFoundException {
    Game game = database.getGame(gameID);
    if(game.getPlayerIDs().size() > 1) {
      game.changeGameStatus(Game.Status.STARTED);
      cardService.createFullDeckForGame(game.getGameID());
      return game;
    }
    else{
      throw new ApiError(Code.INVALID_ARGUMENT, "must have at least 2 players to begin game");
    }
  }

  public void deleteGame(String gameID){
    database.deleteGame(gameID);
  }


  //  This method will get the userName associated with the playerId and
  //    ADD it to the FRONT of the action taken.
  //  I did this for two reasons.  One, that way the individual services only
  //    have to concern themselves with the action that they perform and secondly
  //  that limits all interactions with the database facade (in terms of
  //    creating GameActions) only happens in this method rather than across
  //  multiple services.
  public GameAction createGameAction(String actionTaken, String playerId) {
    int unixTime = (int) (System.currentTimeMillis() / 1000L);
    Player player = database.getPlayer(playerId);
    String actionTakenWithUsername = player.getUsername() + " " + actionTaken;
    GameAction action = new GameAction("act" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE),
                       actionTakenWithUsername,player.getPlayerId(), player.getGameId(),unixTime);
    database.addGameAction(action);
    return action;
  }

  public Stream<GameAction> streamHistory(String gameId) {
    return database.streamHistory(gameId);
  }

  public GameAction getGameAction(String actionId){
    GameAction action = database.getGameActionById(actionId);
    return action;
  }



  //--------------------Player methods ------------------------------------------//

  public String createPlayer(String userId, String gameId)throws NotFoundException, GameFullException{
    Game game = database.getGame(gameId);
    if (game == null){
      throw new NotFoundException("Game with that ID not found");
    }
    int currentNumberofPlayers = game.getPlayerIDs().size();
    if(currentNumberofPlayers == game.getMaxPlayers()){
      throw new GameFullException("Game is Full");
    }
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
    User user = database.getUserByID(userId);
    if (user == null){
      throw new NotFoundException("User with that ID doesn't exist");
    }
    //(String playerID, String userID, String gameID,Color color, String username)
    Player player = new Player("p" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE),
                                userId, game.getGameID(), playerColor, user.getUsername());
    game.getPlayerIDs().add(player.getPlayerId());
    database.addPlayer(player);
    return player.getPlayerId();
  }

  public Player getPlayer(String playerID) {
    return database.getPlayer(playerID);
  }

//-----------------------------------------------------------//


  public void addGameListener(Context ctx, GameListener l) {

    ctx.addCallback(() -> gameListeners.remove(l));
    gameListeners.add(l);
  }

  private void notifyGameListeners(Game g) {
    for (GameListener l : gameListeners) {
      l.notify(g);
    }
  }

  public static void main(String[] args) {
    GameService service = new GameService();
    //System.out.println(service.getGame());
  }

}
