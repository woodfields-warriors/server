package com.wwttr.game;

import java.util.*;

import com.wwttr.api.ApiError;
import com.wwttr.card.CardService;
import com.wwttr.route.RouteService;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.Player;
import com.wwttr.models.PlayerStats;
import com.wwttr.models.IPlayerTurnState;
import com.wwttr.models.User;
import com.wwttr.models.PlayerStats.PlayerTurnState;
import com.wwttr.models.GameAction;
import com.wwttr.models.DeleteResponse;
import com.wwttr.api.NotFoundException;
//import com.wwttr.player.Api.Player;
import com.wwttr.api.Code;
import java.util.stream.*;


// Game Service is of the Singleton Pattern
public class GameService {

  // the id of the player who is currently taking their turn
  private Player currentPlayerId;

  //singleton object
  private DatabaseFacade database;
  Random rn;

  private static GameService gameServiceInstance = null;

  private CardService cardService = CardService.getInstance();
  private RouteService routeService;

  public static GameService getInstance(){
    if(gameServiceInstance == null){
      gameServiceInstance = new GameService(DatabaseFacade.getInstance());
      gameServiceInstance.cardService = CardService.getInstance();
      gameServiceInstance.routeService = RouteService.getInstance();
    }
    return gameServiceInstance;
  }

  GameService(DatabaseFacade db){
    database = db;
    rn = new Random();
  }



  /* creates a game with given Name making the given userID the host*/
  /* hostID should have been verified by ServerFacade */
  public CreateResponse createGame(String gameName, String userID, int numberOfPlayers) throws NotFoundException {
      User user = database.getUserByID(userID);
      if (user == null) {
        throw new NotFoundException("user " + userID + " was not found");
      }
      Player player = new Player("p" + Integer.toString(rn.nextInt()) ,userID, Player.Color.RED, user.getUsername());
      player.setState(new FirstTurnState());
      Game game = new Game(player.getPlayerId(), new ArrayList<String>(), gameName, numberOfPlayers, "game" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE));
      player.setGameId(game.getGameID());
      game.getPlayerIDs().add(player.getPlayerId());
      database.addPlayer(player);
      database.addGame(game);
      routeService.initRoutes(game.getGameID());
      CreateResponse toReturn = new CreateResponse(game.getGameID(), player.getPlayerId());
      return toReturn;
  }

  public List<Game> listGames(){
    return database.listGames();
  }

  public Stream<Game> streamGames() {
    return database.streamGames();
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

  public Stream<PlayerStats> streamPlayerStats(String gameId){
    List<String> playerIds = database.getGame(gameId).getPlayerIDs();

    return database.streamPlayerStats().filter((PlayerStats stats) -> playerIds.contains(stats.getPlayerId()));
  }

  public Game startGame(String gameID) throws NotFoundException {
    Game game = database.getGame(gameID);
    if(game.getGameStatus().equals(Game.Status.STARTED)){
      throw new ApiError(Code.INVALID_ARGUMENT,"Game already started");
    }
    if(game.getPlayerIDs().size() > 1) {
      game.changeGameStatus(Game.Status.STARTED);
      database.updateGame(game,game.getGameID());
      cardService.createFullDecksForGame(game.getGameID());
      for (String playerId : game.getPlayerIDs()) {
        database.updatePlayerStats(playerId);
      }
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

  public void drawTrainCard(String playerId) throws NotFoundException {
    Player player = database.getPlayer(playerId);
    if (player == null){
      throw new NotFoundException("player with id " + playerId + " not found");
    }
    player.getPlayerState().drawTrainCard(playerId);
  }
  public void claimRoute(String playerId, String routeId, List<String> cardIds) throws NotFoundException {
    Player player = database.getPlayer(playerId);
    if (player == null){
      throw new NotFoundException("player with id " + playerId + " not found");
    }
    player.getPlayerState().claimRoute(playerId, routeId,cardIds);
  }
  public void drawDestinationCards(String playerId, List<String> destinationCardIds) throws NotFoundException {
    Player player = database.getPlayer(playerId);
    if (player == null){
      throw new NotFoundException("player with id " + playerId + " not found");
    }
    player.getPlayerState().drawDestinationCards(playerId,destinationCardIds);
  }
  public void drawFaceUpTrainCard(String playerId, String cardId) throws NotFoundException {
    Player player = database.getPlayer(playerId);
    if (player == null){
      throw new NotFoundException("player with id " + playerId + " not found");
    }
    player.getPlayerState().drawFaceUpTrainCard(playerId, cardId);
  }


//CLAIM ROUTE what's going on there?
//Stream Player Stats.  I need help with that.

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
    player.setState(new FirstTurnState());
    database.addPlayer(player);
    return player.getPlayerId();
  }

  public Player getPlayer(String playerID) {
    return database.getPlayer(playerID);
  }

  // public List<Player> listPlayers(String gameID) {
  //   return database.listPlayers(gameID);
  // }
//-----------------------------------------------------------//




  // public static void main(String[] args) {
  //   GameService service = new GameService();
  //   //System.out.println(service.getGame());
  //   ctx.addCallback(() -> gameListeners.remove(l));
  //   gameListeners.add(l);
  // }

}

//*********************************************************//
//------------------PLAYER STATES AND INTERFACE------------//


class PendingState implements IPlayerTurnState{

  public PlayerTurnState getTurnState() {
    return PlayerTurnState.PENDING;
  }

  public void drawTrainCard(String playerId) throws NotFoundException {
    //Tell the client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"It's not your turn");
  }
  public void claimRoute(String playerId, String routeId,List<String> cardIds) throws NotFoundException, IllegalArgumentException{
    //tell client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"It's not your turn");
  }
  public void drawDestinationCards(String playerId, List<String> destinationCardIds) throws NotFoundException {
    //tell client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"It's not your turn");
  }
  public void drawFaceUpTrainCard(String playerId, String cardId) throws NotFoundException {
    //tell client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"It's not your turn");
  }
}

class FirstTurnState implements IPlayerTurnState{

  DatabaseFacade database = DatabaseFacade.getInstance();
  CardService cardService = CardService.getInstance();
  RouteService routeService = RouteService.getInstance();

  public PlayerTurnState getTurnState() {
    return PlayerTurnState.FIRST;
  }

  public void drawTrainCard(String playerId) throws NotFoundException {
    //Tell the client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"You can only draw destination cards, not train cards from deck");
  }
  public void claimRoute(String playerId, String routeId,List<String> cardIds) throws NotFoundException, IllegalArgumentException{
    //tell client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"You can only draw destination cards, not Routes");
  }
  public void drawDestinationCards(String playerId, List<String> destinationCardIds) throws NotFoundException {
    cardService.claimDestinationCards(destinationCardIds,playerId);
    Player player = database.getPlayer(playerId);
    Game game = database.getGame(player.getGameId());
    List<String> playerIdsInGame = game.getPlayerIDs();
    if(playerId.equals(playerIdsInGame.get(0))){
      player.setState(new StartState());
    }
    else{
      player.setState(new PendingState());
    }
    database.updatePlayer(player);
    // database.updatePlayerStats(playerId);
  }
  public void drawFaceUpTrainCard(String playerId, String cardId) throws NotFoundException {
    //tell client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"You can only draw destination cards, not FaceUp Train Cards");
  }
}

class StartState implements IPlayerTurnState{

  DatabaseFacade database = DatabaseFacade.getInstance();
  CardService cardService = CardService.getInstance();
  RouteService routeService = RouteService.getInstance();

  public PlayerTurnState getTurnState() {
    return PlayerTurnState.START;
  }

  public void drawTrainCard(String playerId) throws NotFoundException {
    cardService.claimTrainCardFromDeck(playerId);
    Player player = database.getPlayer(playerId);
    player.setState(new MidState());
  }
  public void claimRoute(String playerId, String routeId,List<String> cardIds) throws NotFoundException, IllegalArgumentException{
    routeService.claimRoute(playerId,routeId,cardIds);
    Player player = database.getPlayer(playerId);
    if(database.getGame(player.getGameId()).getGameStatus().equals(Game.Status.LASTROUND)){
      player.setState(new GameEnded());
    }
    else {
      player.setState(new PendingState());
    }
    database.updatePlayer(player);
    Player nextPlayer = database.getNextPlayer(playerId,player.getGameId());
    if(nextPlayer.getPlayerState().getClass().equals(GameEnded.class)){
      Game game = database.getGame(player.getGameId());
      game.changeGameStatus(Game.Status.ENDED);
      database.updateGame(game,game.getGameID());
    }
    else {
      nextPlayer.setState(new StartState());
      database.updatePlayer(nextPlayer);
    }
  }

  public void drawDestinationCards(String playerId, List<String> destinationCardIds)
                                   throws NotFoundException{
    cardService.claimDestinationCards(destinationCardIds,playerId);
    Player player = database.getPlayer(playerId);
    if(database.getGame(player.getGameId()).getGameStatus().equals(Game.Status.LASTROUND)){
      player.setState(new GameEnded());
    }
    else {
      player.setState(new PendingState());
    }
    database.updatePlayer(player);
    Player nextPlayer = database.getNextPlayer(playerId,player.getGameId());
    if(nextPlayer.getPlayerState().getClass().equals(GameEnded.class)){
      Game game = database.getGame(player.getGameId());
      game.changeGameStatus(Game.Status.ENDED);
      database.updateGame(game,game.getGameID());
    }
    else {
      nextPlayer.setState(new StartState());
      database.updatePlayer(nextPlayer);
    }
  }
  public void drawFaceUpTrainCard(String playerId, String cardId)throws NotFoundException{
    cardService.claimFaceUpTrainCard(playerId,cardId);
    boolean isLocomotiveCard  = cardService.isLocomotive(cardId);
    Player player = database.getPlayer(playerId);
    if(isLocomotiveCard){
      if(database.getGame(player.getGameId()).getGameStatus().equals(Game.Status.LASTROUND)){
        player.setState(new GameEnded());
      }
      else {
        player.setState(new PendingState());
      }
      database.updatePlayer(player);
      Player nextPlayer = database.getNextPlayer(playerId,player.getGameId());
      if(nextPlayer.getPlayerState().getClass().equals(GameEnded.class)){
        Game game = database.getGame(player.getGameId());
        game.changeGameStatus(Game.Status.ENDED);
        database.updateGame(game,game.getGameID());
      }
      else {
        nextPlayer.setState(new StartState());
        database.updatePlayer(nextPlayer);
      }
    }
    else{
      player.setState(new MidState());
      database.updatePlayer(player);
    }
  }
}


class MidState implements IPlayerTurnState{

  DatabaseFacade database = DatabaseFacade.getInstance();
  CardService cardService = CardService.getInstance();

  public PlayerTurnState getTurnState() {
    return PlayerTurnState.MID;
  }

  public void drawTrainCard(String playerId)throws NotFoundException{
    cardService.claimTrainCardFromDeck(playerId);
    Player player = database.getPlayer(playerId);
    if(database.getGame(player.getGameId()).getGameStatus().equals(Game.Status.LASTROUND)){
      player.setState(new GameEnded());
    }
    else {
      player.setState(new PendingState());
    }
    database.updatePlayer(player);
    Player nextPlayer = database.getNextPlayer(playerId,player.getGameId());
    if(nextPlayer.getPlayerState().getClass().equals(GameEnded.class)){
      Game game = database.getGame(player.getGameId());
      game.changeGameStatus(Game.Status.ENDED);
      database.updateGame(game,game.getGameID());
    }
    else {
      nextPlayer.setState(new StartState());
      database.updatePlayer(nextPlayer);
    }
  }
  public void claimRoute(String playerId, String routeId,List<String> cardIds) throws NotFoundException, IllegalArgumentException{
    throw new ApiError(Code.FAILED_PRECONDITION,"Mid state.  You can only draw a card");
  }
  public void drawDestinationCards(String playerId, List<String> destinationCardIds) throws NotFoundException {
    throw new ApiError(Code.FAILED_PRECONDITION,"Mid state.  You can only draw a card");
  }
  public void drawFaceUpTrainCard(String playerId, String cardId)throws NotFoundException{
    if(!cardService.isLocomotive(cardId)) {
      cardService.claimFaceUpTrainCard(playerId, cardId);
      Player player = database.getPlayer(playerId);
      if(database.getGame(player.getGameId()).getGameStatus().equals(Game.Status.LASTROUND)){
        player.setState(new GameEnded());
      }
      else {
        player.setState(new PendingState());
      }
      database.updatePlayer(player);
      Player nextPlayer = database.getNextPlayer(playerId,player.getGameId());
      if(nextPlayer.getPlayerState().getClass().equals(GameEnded.class)){
        Game game = database.getGame(player.getGameId());
        game.changeGameStatus(Game.Status.ENDED);
        database.updateGame(game,game.getGameID());
      }
      else {
        nextPlayer.setState(new StartState());
        database.updatePlayer(nextPlayer);
      }
    }
    else{
      throw new ApiError(Code.FAILED_PRECONDITION,"Mid state.  You can only draw a non locomotive card");
    }
  }

}

class GameEnded implements IPlayerTurnState{

  public PlayerTurnState getTurnState() {
    return PlayerTurnState.GAME_ENDED;
  }

  public void drawTrainCard(String playerId) throws NotFoundException {
    //Tell the client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"game has ended");
  }
  public void claimRoute(String playerId, String routeId,List<String> cardIds) throws NotFoundException, IllegalArgumentException{
    //tell client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"game has ended");
  }
  public void drawDestinationCards(String playerId, List<String> destinationCardIds) throws NotFoundException {
    //tell client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"game has ended");
  }
  public void drawFaceUpTrainCard(String playerId, String cardId) throws NotFoundException {
    //tell client it isn't his/her turn
    throw new ApiError(Code.FAILED_PRECONDITION,"game has ended");
  }
}
