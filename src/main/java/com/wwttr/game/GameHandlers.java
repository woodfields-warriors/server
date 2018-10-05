package com.wwttr.game;

import com.google.protobuf.RpcController;
import com.wwttr.models.CreateResponse;
<<<<<<< HEAD
import com.wwttr.models.Game;
import java.util.*;
=======
import com.wwttr.api.ApiError;
import com.wwttr.auth.AuthService;
import com.wwttr.api.Code;
>>>>>>> 5792e3c0bb8ec082920ef6acd7bb76409761b801

public class GameHandlers implements Api.GameService.BlockingInterface {

  private GameService service;
  private AuthService authService;

  public GameHandlers(GameService service, AuthService authService) {
    this.service = service;
    this.authService = authService;
  }

  public Api.CreateResponse createGame(RpcController controller, Api.CreateGameRequest request) {

    // Validate input
    Api.Game game = request.getGame();
    if (game == null) {
      throw new ApiError(Code.INVALID_ARGUMENT, "argument 'game' is required");
    }
    if (game.getDisplayName() == "") {
      throw new ApiError(Code.INVALID_ARGUMENT, "argument 'display_name' is required");
    }
    if (game.getHostUserId() == "") {
      throw new ApiError(Code.INVALID_ARGUMENT, "argument 'host_user_id' is required");
    }
    int maxPlayers = game.getMaxPlayers();
    if (maxPlayers == 0) {
      maxPlayers = 5;
    }
    CreateResponse response = service.createGame(request.getDisplayName(),request.getUserId(),
                                                 request.getMaxPlayers());
    Api.CreateResponse.Builder builder = Api.CreateResponse.newBuilder();
    builder.setGameId(response.getGameID());
    builder.setPlayerId(response.getPlayerID());
    return builder.build();
  }

  public Api.Empty leaveGame(RpcController controller, Api.LeaveGameRequest request) {
    service.leaveGame(request.getPlayerId(),request.getGameId());
    Api.Empty.Builder toReturn = Api.Empty.newBuilder();
    return toReturn.build();
  }

  public Api.ListGamesResponse listGames(RpcController controller, Api.ListGamesRequest request) {
    List<Game> allGames = service.listGames();
    List<Api.Game> protoGames = new ArrayList<Api.Game>();
    for(Game game: allGames){
      Api.Game.Builder gameBuilder = Api.Game.newBuilder();
      gameBuilder.setGameId(game.getGameID());
      gameBuilder.setDisplayName(game.getDisplayName());
      gameBuilder.setMaxPlayers(game.getMaxPlayers());
      gameBuilder.setHostPlayerId(game.getHostPlayerID());
      gameBuilder.setPlayerIds(game.getPlayerUserIDs());
      protoGames.add(gameBuilder.build());
    }
    Api.ListGamesResponse.Builder responseBuilder = Api.ListGamesResponse.newBuilder();
    responseBuilder.setGames(protoGames);
    return responseBuilder.build();



    // if (authService.getAccount(game.getHostUserId()) == null) {
    //   throw new ApiException(Code.NOT_FOUND, "host user " + game.getHostUserId() + " was not found");
    // }

    // Execute request
  }

  public Api.JoinGameResponse joinGame(RpcController controller, Api.JoinGameRequest request) {
    return null;
  }

  public Api.Empty leaveGame(RpcController controller, Api.LeaveGameRequest request) {
    return null;
  }

  public Api.Game getGame(RpcController controller, Api.GetGameRequest request) {
    Game game = service.getGame(request.getGameId());
    Api.Game.Builder gameBuilder = Api.Game.newBuilder();
    gameBuilder.setGameId(game.getGameID());
    gameBuilder.setDisplayName(game.getDisplayName());
    gameBuilder.setMaxPlayers(game.getMaxPlayers());
    gameBuilder.setHostPlayerId(game.getHostPlayerID());
    gameBuilder.setPlayerIds(game.getPlayerUserIDs().toArray());
    return gameBuilder.build();
  }

  public Api.Game startGame(RpcController controller, Api.StartGameRequest request){
    Game game = service.startGame(request.getGameId());
    Api.Game.Builder gameBuilder = Api.Game.newBuilder();
    gameBuilder.setGameId(game.getGameID());
    gameBuilder.setDisplayName(game.getDisplayName());
    gameBuilder.setMaxPlayers(game.getMaxPlayers());
    gameBuilder.setHostPlayerId(game.getHostPlayerID());
    gameBuilder.setPlayerIds(game.getPlayerUserIDs());
    return gameBuilder.build();
  }

  public Api.Empty deleteGame(RpcController controller, Api.DeleteGameRequest request) {
    service.deleteGame(request.getGameId());
    Api.Empty.Builder toReturn = Api.Empty.newBuilder();
    return toReturn.build();
  }

}
