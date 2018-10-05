package com.wwttr.game;

import com.google.protobuf.RpcController;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import java.util.*;

public class GameHandlers implements Api.GameService.BlockingInterface {

  private GameService service;

  public GameHandlers(GameService service) {
    this.service = service;
  }

  public Api.CreateResponse createGame(RpcController controller, Api.CreateGameRequest request) {
    //TODO check that the user actually exists
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
