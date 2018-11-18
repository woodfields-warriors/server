package com.wwttr.route;

import com.google.protobuf.RpcController;
import java.util.*;
import com.wwttr.api.ApiError;
import com.wwttr.api.Code;
import com.google.protobuf.RpcCallback;
import com.wwttr.api.NotFoundException;
import com.wwttr.database.CommandQueue;
import com.wwttr.models.Route;

public class RouteHandlers extends Api.RouteService {

  private RouteService service;

  public RouteHandlers(RouteService service) {
    this.service = service;
  }

  public void streamRoutes(RpcController controller, Api.StreamRoutesRequest request, RpcCallback<Api.Route> callback) {
    System.out.println("STREAM ROUTES");
    System.out.println(request.getGameId());
    if (request.getGameId() == "") {
      throw new ApiError(Code.INVALID_ARGUMENT, "missing argument game_id");
    }
    service.streamRoutes(request.getGameId())
      // .takeWhile((Route r) -> !controller.isCanceled())
      .forEach((Route r) -> {
        System.out.println(r.getRouteId());
        callback.run(r.toProto());
      });
  }

  public void claimRoute(RpcController controller, Api.ClaimRouteRequest request, RpcCallback<Api.ClaimRouteResponse> callback) {
    // gameService.claimRoute(request.getRouteId(), request.getPlayerId());
  }
}
