package com.wwttr.route;

import com.google.protobuf.RpcController;
import java.util.*;
import com.wwttr.api.ApiError;
import com.wwttr.api.Code;
import com.google.protobuf.RpcCallback;
import com.wwttr.api.NotFoundException;
import com.wwttr.chat.Api.Message;
import com.wwttr.database.CommandQueue;
import com.wwttr.models.Player;
import com.wwttr.models.Route;
import com.wwttr.game.GameService;
import com.wwttr.api.FailedPreconditionException;


public class RouteHandlers extends Api.RouteService {

  private RouteService service;
  private GameService gameService;

  public RouteHandlers(RouteService service) {
    this.service = service;
    gameService = GameService.getInstance();
  }

  // calls addDelta method in GameService after determining gameId
    public void addDelta(RpcController controller, com.google.protobuf.Message request, RpcCallback<Api.Empty> callback) {
      Controller controllerWrapper = (Controller) controller;
      String id = controller.getId();
      String gameId;

      if (request instanceof Api.ClaimRouteRequest) {
        Player p = gameService.getPlayer(request.getPlayerId());
        gameId = p.getGameId();
      }

      gameService.addDelta(request, id, gameId);

      Api.Empty.Builder toReturn = Api.Empty.newBuilder();
      callback.run(toReturn.build());
    }

  public void streamRoutes(RpcController controller, Api.StreamRoutesRequest request, RpcCallback<Api.Route> callback) {
    if (request.getGameId().equals("")) {
      throw new ApiError(Code.INVALID_ARGUMENT, "missing argument game_id");
    }
    service.streamRoutes(request.getGameId())
      // .takeWhile((Route r) -> !controller.isCanceled())
      .forEach((Route r) -> {
        callback.run(r.toProto());
      });
  }

  public void claimRoute(RpcController controller, Api.ClaimRouteRequest request, RpcCallback<Api.ClaimRouteResponse> callback) {
    try{
      gameService.claimRoute(request.getPlayerId(), request.getRouteId(), request.getCardIdsList());
      Api.ClaimRouteResponse.Builder builder = Api.ClaimRouteResponse.newBuilder();
      callback.run(builder.build());
    }
    catch(NotFoundException e){
      throw new ApiError(Code.NOT_FOUND, e.getMessage());
    }
    catch (IllegalArgumentException e){
      throw new ApiError(Code.INVALID_ARGUMENT,e.getMessage());
    }
    catch (FailedPreconditionException e) {
      throw new ApiError(Code.FAILED_PRECONDITION, e.getMessage());
    }
  }
}
