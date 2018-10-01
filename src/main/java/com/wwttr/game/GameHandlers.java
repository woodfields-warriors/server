package com.wwttr.game;

import com.google.protobuf.RpcController;

public class GameHandlers implements Api.GameService.BlockingInterface {

  private GameService service;

  public GameHandlers(GameService service) {
    this.service = service;
  }

  public Api.Game createGame(RpcController controller, Api.CreateGameRequest request) {
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
