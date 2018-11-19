package com.wwttr.game;

import com.google.protobuf.RpcController;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.Player;
import com.wwttr.models.GameAction;
import java.util.*;
import com.wwttr.api.ApiError;
import com.wwttr.auth.AuthService;
import com.wwttr.api.Code;
import com.google.protobuf.RpcCallback;
import com.wwttr.game.GameFullException;
import com.wwttr.api.NotFoundException;
import com.wwttr.database.CommandQueue;
import java.util.stream.*;


public class GameHandlers extends Api.GameService {

  private GameService service;
  private AuthService authService;

  public GameHandlers(GameService service, AuthService authService) {
    this.service = service;
    this.authService = authService;
  }

  //Creates a player object, then creates a game and adds that to the
  // the database.  Then it gives the created player the game that was
  // created so that player knows what game it is a part of
  // then it returns the game Id and playerID
  public void createGame(RpcController controller, Api.CreateGameRequest request, RpcCallback<Api.CreateResponse> callback) {

    // Validate input
    if (request.getDisplayName() == "") {
      throw new ApiError(Code.INVALID_ARGUMENT, "argument 'display_name' is required");
    }
    if (request.getUserId() == "") {
      throw new ApiError(Code.INVALID_ARGUMENT, "argument 'host_user_id' is required");
    }
    int maxPlayers = request.getMaxPlayers();
    if (maxPlayers == 0) {
      maxPlayers = 5;
    }
    if(maxPlayers < 2 || maxPlayers > 6){
      throw new ApiError(Code.INVALID_ARGUMENT,"argument 'max_players' must be between 2 and 6");
    }
    try {
      CreateResponse response = service.createGame(request.getDisplayName(),request.getUserId(),
                                                 request.getMaxPlayers());
      Api.CreateResponse.Builder builder = Api.CreateResponse.newBuilder();
      builder.setGameId(response.getGameID());
      builder.setPlayerId(response.getPlayerID());
      callback.run(builder.build());
    }
    catch (NotFoundException e) {
      throw new ApiError(Code.NOT_FOUND, e.getMessage());
    }
  }

  public void leaveGame(RpcController controller, Api.LeaveGameRequest request, RpcCallback<Api.Empty> callback) {
    service.leaveGame(request.getPlayerId(),request.getGameId());
    Api.Empty.Builder toReturn = Api.Empty.newBuilder();
    callback.run(toReturn.build());
  }

  public void listGames(RpcController controller, Api.ListGamesRequest request, RpcCallback<Api.ListGamesResponse> callback) {
    List<Game> allGames = service.listGames();
    Api.ListGamesResponse.Builder builder = Api.ListGamesResponse.newBuilder();
    for(Game game: allGames){

      Api.Game.Status status;
      switch (game.getGameStatus()) {
      case PRE:
        status = Api.Game.Status.PRE;
        break;
      case STARTED:
        status = Api.Game.Status.STARTED;
        break;
      case ENDED:
        status = Api.Game.Status.FINISHED;
        break;
      default:
        status = Api.Game.Status.UNKNOWN;
        break;
      }

      Api.Game.Builder gameBuilder = Api.Game.newBuilder();
      gameBuilder.setGameId(game.getGameID());
      gameBuilder.setDisplayName(game.getDisplayName());
      gameBuilder.setMaxPlayers(game.getMaxPlayers());
      gameBuilder.setHostPlayerId(game.getHostPlayerID());
      gameBuilder.setStatus(status);
      for(String i: game.getPlayerIDs()){
        gameBuilder.addPlayerIds(i);
      }
      builder.addGames(gameBuilder.build());
    }

    callback.run(builder.build());

    // Context ctx = new Context();
    // service.addGameListener(ctx, (Game game) -> {
    //
    //   if (controller.isCanceled()) {
    //     ctx.cancel();
    //     return;
    //   }
    //
    //   Api.Game.Builder gameBuilder = Api.Game.newBuilder();
    //   gameBuilder.setGameId(game.getGameID());
    //   gameBuilder.setDisplayName(game.getDisplayName());
    //   gameBuilder.setMaxPlayers(game.getMaxPlayers());
    //   gameBuilder.setHostPlayerId(game.getHostPlayerID());
    //   for(String i: game.getPlayerIDs()){
    //     gameBuilder.addPlayerIds(i);
    //   }
    //
    //   callback.run(gameBuilder.build());
    // });
  }

  public void streamGames(RpcController controller, Api.StreamGamesRequest request, RpcCallback<Api.Game> callback) {
    service.streamGames().forEach((Game game) -> {
      Api.Game.Builder builder = Api.Game.newBuilder();

      Api.Game.Status status;
      switch (game.getGameStatus()) {
      case PRE:
        status = Api.Game.Status.PRE;
        break;
      case STARTED:
        status = Api.Game.Status.STARTED;
        break;
      case ENDED:
        status = Api.Game.Status.FINISHED;
        break;
      default:
        status = Api.Game.Status.UNKNOWN;
        break;
      }

      builder.setGameId(game.getGameID());
      builder.setDisplayName(game.getDisplayName());
      builder.setMaxPlayers(game.getMaxPlayers());
      builder.setHostPlayerId(game.getHostPlayerID());
      builder.setStatus(status);
      for (String i : game.getPlayerIDs()) {
        builder.addPlayerIds(i);
      }

      callback.run(builder.build());
    });
  }

  public void getGame(RpcController controller, Api.GetGameRequest request, RpcCallback<Api.Game> callback) {
    //  System.out.println("The game ID coming in from the request is: " + request.getGameId());
    Game game = service.getGame(request.getGameId());
    if (game == null){
        throw new ApiError(Code.NOT_FOUND, "Game with that ID not found");
    }

    Api.Game.Status status;
    switch (game.getGameStatus()) {
    case PRE:
      status = Api.Game.Status.PRE;
      break;
    case STARTED:
      status = Api.Game.Status.STARTED;
      break;
    case ENDED:
      status = Api.Game.Status.FINISHED;
      break;
    default:
      status = Api.Game.Status.UNKNOWN;
      break;
    }

    Api.Game.Builder gameBuilder = Api.Game.newBuilder();
    gameBuilder.setGameId(game.getGameID());
    gameBuilder.setDisplayName(game.getDisplayName());
    gameBuilder.setMaxPlayers(game.getMaxPlayers());
    gameBuilder.setHostPlayerId(game.getHostPlayerID());
    gameBuilder.setStatus(status);
    for(String i: game.getPlayerIDs()){
      gameBuilder.addPlayerIds(i);
    }
    callback.run(gameBuilder.build());
  }

  public void startGame(RpcController controller, Api.StartGameRequest request, RpcCallback<Api.Game> callback){
    try {
      Game game = service.startGame(request.getGameId());
      Api.Game.Builder gameBuilder = Api.Game.newBuilder();
      gameBuilder.setGameId(game.getGameID());
      gameBuilder.setDisplayName(game.getDisplayName());
      gameBuilder.setMaxPlayers(game.getMaxPlayers());
      gameBuilder.setHostPlayerId(game.getHostPlayerID());
      for(String i: game.getPlayerIDs()){
        gameBuilder.addPlayerIds(i);
      }
      callback.run(gameBuilder.build());
    }
    catch (NotFoundException e) {
      throw new ApiError(Code.NOT_FOUND, "");
    }
  }

  public void deleteGame(RpcController controller, Api.DeleteGameRequest request, RpcCallback<Api.Empty> callback) {
    service.deleteGame(request.getGameId());
    Api.Empty.Builder toReturn = Api.Empty.newBuilder();
    callback.run(toReturn.build());
  }

  public void streamHistory(RpcController controller, Api.StreamHistoryRequest request, RpcCallback<Api.GameAction> callback){
    Stream<GameAction> gameActions = service.streamHistory(request.getGameId());
    gameActions.forEach((GameAction action) -> {
      Api.GameAction.Builder builder = action.createBuilder();
      callback.run(builder.build());
    });
  }


  // This method is the replacement for JoinGame().
  // It takes the given request and creates a Player object,
  //   adds that player to the requested game then returns the player ID
  // in the createPlayerResponse
  public void createPlayer(RpcController controller, Api.CreatePlayerRequest request, RpcCallback<Api.CreatePlayerResponse> callback){
    try{
      String newPlayerID = service.createPlayer(request.getUserId(), request.getGameId());
      Api.CreatePlayerResponse.Builder toReturn = Api.CreatePlayerResponse.newBuilder();
      toReturn.setPlayerId(newPlayerID);
      callback.run(toReturn.build());
    }
    catch(NotFoundException e){
      throw new ApiError(Code.NOT_FOUND, "");
    }
    catch(GameFullException e){
      throw new ApiError(Code.INVALID_ARGUMENT, "");
    }
  }

  public void getPlayer(RpcController controller, Api.GetPlayerRequest request, RpcCallback<Api.Player> callback) {
    if (request.getPlayerId() == "") {
      throw new ApiError(Code.INVALID_ARGUMENT, "argument player_id is required");
    }

    Player player = service.getPlayer(request.getPlayerId());
    if (player == null) {
        throw new ApiError(Code.NOT_FOUND, "");
    }
    Api.Player.Builder builder = Api.Player.newBuilder();
    builder.setId(player.getPlayerId());
    builder.setAccountId(player.getUserId());
    builder.setGameId(player.getGameId());
    builder.setUsername(player.getUsername());
    Api.Player.Color color;
    switch (player.getPlayerColor()) {
    case RED:
      color = Api.Player.Color.RED;
      break;
    case BLUE:
      color = Api.Player.Color.BLUE;
      break;
    case GREEN:
      color = Api.Player.Color.GREEN;
      break;
    case YELLOW:
      color = Api.Player.Color.YELLOW;
      break;
    case PURPLE:
      color = Api.Player.Color.PURPLE;
      break;
    case ORANGE:
      color = Api.Player.Color.ORANGE;
      break;
    default:
      color = Api.Player.Color.UNKNOWN;
      System.out.printf("[WARN] player %s has invalid color\n", player.getPlayerId());
    }
    builder.setColor(color);

    callback.run(builder.build());
  }

  public void togglePlayerStats(RpcController controller, Api.Empty req, RpcCallback<Api.Empty> callback) {
    callback.run(Api.Empty.newBuilder().build());
  }

  CommandQueue<Api.PlayerStats> playerStatsQueue = new CommandQueue<Api.PlayerStats>();
  boolean playerStatsState;

  public void streamPlayerStats(RpcController controller, Api.StreamPlayerStatsRequest request, RpcCallback<Api.PlayerStats> callback) {
    playerStatsQueue.subscribe()
      .forEach((Api.PlayerStats stats) -> {
        callback.run(stats);
      });
  }

  // void publishPlayerStats() {
  //   playerStatsState = !playerStatsState;
  //   Api.PlayerStats.Builder builder = Api.PlayerStats.newBuilder();

  //   if (playerStatsState == true) {
  //     builder.setPoints(0);
  //     builder.setTrainCount(80);
  //     builder.setDestinationCardCount(0);
  //     builder.setTrainCardCount(0);
  //   } else {
  //     builder.setPoints(30);
  //     builder.setTrainCount(40);
  //     builder.setDestinationCardCount(2);
  //     builder.setTrainCardCount(6);
  //   }

  //   builder.setPlayerId("player1");
  //   playerStatsQueue.publish(builder.build());
  //   builder.setPlayerId("player2");
  //   playerStatsQueue.publish(builder.build());
  //   builder.setPlayerId("player3");
  //   playerStatsQueue.publish(builder.build());
  // }
}
