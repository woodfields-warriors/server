package com.wwttr.game;

import java.util.*;

import com.wwttr.api.ApiError;
import com.wwttr.card.CardService;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.Player;
import com.wwttr.models.DeleteResponse;
import com.wwttr.api.NotFoundException;
//import com.wwttr.player.Api.Player;
import com.wwttr.api.Code;



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

  public String createPlayer(String userId, String gameId)throws NotFoundException, GameFullException{
    Game game = database.getGame(gameId);
    if (game == null){
      throw new NotFoundException("");
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
    Player player = new Player("p" + Integer.toString(rn.nextInt()),userId, playerColor);
    player.setGameId(game.getGameID());
    game.getPlayerIDs().add(player.getPlayerId());

    database.addPlayer(player);

    return player.getPlayerId();
  }

  public Player getPlayer(String playerID) {
    return database.getPlayer(playerID);
  }

  public List<Player> listPlayers(String gameID) {
    return database.listPlayers(gameID);
  }



  public static void main(String[] args) {
    GameService service = new GameService();
    //System.out.println(service.getGame());
  }

}

//*********************************************************//
//------------------PLAYER STATES AND INTERFACE------------//

public Interface IPlayerTurnState{
  public void drawTrainCard(String playerId);
  public void claimRoute(String playerId);
  public void drawDestinationCards();
  public void drawFaceUpTrainCard(String playerId, String cardId);
}



public class pendingState implements IPlayerTurnState{
  public void drawTrainCard(String playerId){
    //Tell the client it isn't his/her turn
  }
  public void claimRoute(String playerId){
    //tell client it isn't his/her turn
  }
  public void drawDestinationCards(){
    //tell client it isn't his/her turn
  }
  public void drawFaceUpTrainCard(){
    //tell client it isn't his/her turn
  }
}



public class startState implements IPlayerTurnState{
  public void drawTrainCard(String playerId)throws NotFoundException{
    cardService.claimTrainCardFromDeck(playerId);
    Player player = database.getPlayer(playerId);
    player.setState(new MidState());
    //TODO handle returning...
  }
  public void claimRoute(String playerId){

    Player player = database.getPlayer(playerId);
    player.setState(new PendingState());
  }
  public void drawDestinationCards(String playerId, List<String> destinationCardIds)
                                   throws NotFoundException{
    cardService.claimDesinationCards(destinationCardIds,playerId);
    Player player = database.getPlayer(playerId);
    player.setState(new PendingState());
  }
  public void drawFaceUpTrainCard(String playerId, String cardId)throws NotFoundException{
    cardService.claimFaceUpTrainCard(playerId,cardId);
    boolean isLocomotiveCard  = cardService.isLocomotive(cardId);
    Player player = database.getPlayer(playerId);
    if(isLocomotiveCard){
      player.setState(new PendingState());
    }
    else{
      player.setState(new MidState());
    }
  }
}



public class MidState implements IPlayerTurnState{
  public void drawTrainCard(String playerId)throws NotFoundException{
    cardService.claimTrainCardFromDeck(playerId);
    Player player = database.getPlayer(playerId);
    player.setState(new PendingState());
  }
  public void claimRoute(String playerId){
    //tell client action isn't possible
  }
  public void drawDestinationCards(){
    //tell client action isn't possible
  }
  public void drawFaceUpTrainCard()throws NotFoundException{
    cardService.claimTrainCardFromDeck(playerId);
    Player player = database.getPlayer(playerId);
    player.setState(new PendingState());
  }
}
