package com.wwttr.game;

import com.google.protobuf.RpcController;
import com.wwttr.models.CreateResponse;

public class GameHandlers implements Api.GameService.BlockingInterface {

  private GameService service;

  public GameHandlers(GameService service) {
    this.service = service;
  }

  public Api.CreateResponse createGame(RpcController controller, Api.CreateGameRequest request) {
    CreateResponse response = service.createGame(request.getGameName());
    Api.CreateResponse.Builder builder = Api.Game.newBuilder();
    Api.CreateResponse.setGameName(response.getGameName());
    Api.CreateResponse.setTotalPlayers(response.getTotalPlayers());
    return builder.build();
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
