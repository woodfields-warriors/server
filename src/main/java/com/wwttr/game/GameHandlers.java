package com.wwttr.game;

import com.google.protobuf.RpcController;
import com.wwttr.models.CreateResponse;
import com.wwttr.api.ApiError;
import com.wwttr.auth.AuthService;
import com.wwttr.api.Code;

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
    // if (authService.getAccount(game.getHostUserId()) == null) {
    //   throw new ApiException(Code.NOT_FOUND, "host user " + game.getHostUserId() + " was not found");
    // }

    // Execute request
    CreateResponse response = service.createGame(game.getDisplayName(), game.getHostUserId(), maxPlayers);

    Api.CreateResponse.Builder builder = Api.CreateResponse.newBuilder();
    builder.setGameName(response.getGameName());
    builder.setTotalPlayers(response.getTotalPlayers());
    return builder.build();
  }

  public Api.JoinGameResponse joinGame(RpcController controller, Api.JoinGameRequest request) {
    return null;
  }

  public Api.Empty leaveGame(RpcController controller, Api.LeaveGameRequest request) {
    return null;
  }

  public Api.Game getGame(RpcController controller, Api.GetGameRequest request) {
    return null;
  }

  public Api.Game updateGame(RpcController controller, Api.UpdateGameRequest request) {
    return null;
  }

  public Api.Empty deleteGame(RpcController controller, Api.DeleteGameRequest request) {
    return null;
  }

  public Api.ListGamesResponse listGames(RpcController controller, Api.ListGamesRequest request) {
    return null;
  }
}
