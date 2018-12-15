package com.wwttr.auth;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;

import com.wwttr.api.NotFoundException;
import com.wwttr.models.LoginResponse;
import com.wwttr.api.Code;
import com.wwttr.api.ApiError;
//import com.wwttr.api.InvalidArgumentException;
import com.google.protobuf.Message;
import com.wwttr.server.Controller;
import com.wwttr.game.Api.Empty;


public class AuthHandlers extends Api.AuthService {

  private AuthService service;

  public AuthHandlers(AuthService service) {
    this.service = service;
  }

  // // calls addDelta method in AuthService, no gameId
  // public void addDelta(RpcController controller, Message request, RpcCallback<com.wwttr.game.Api.Empty> callback) {
  //   Controller controllerWrapper = (Controller) controller;
  //   String id = controllerWrapper.getId();
  //   service.addDelta(request, id, "NULL");
  //   com.wwttr.game.Api.Empty.Builder toReturn = com.wwttr.game.Api.Empty.newBuilder();
  //   callback.run(toReturn.build());
  // }

  public void login(RpcController controller, Api.LoginAccountRequest request, RpcCallback<Api.LoginResponse> callback) {
    try{
      LoginResponse response = service.login(request.getUsername(), request.getPassword());
      Api.LoginResponse.Builder builder = Api.LoginResponse.newBuilder();
      builder.setUserId(response.getUserID());
      callback.run(builder.build());
    }
    catch (NotFoundException e) {
      throw new ApiError(Code.NOT_FOUND, "");
    }
    catch (AccessDeniedException e) {
      throw new ApiError(Code.ACCESS_DENIED, "");
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL, "");
    }
  }

  public void register(RpcController controller, Api.LoginAccountRequest request, RpcCallback<Api.LoginResponse> callback) {
    try {
      Controller controllerWrapper = (Controller) controller;
      String id = controllerWrapper.getId();

      LoginResponse response = service.register(request.getUsername(), request.getPassword(), id);
      Api.LoginResponse.Builder builder = Api.LoginResponse.newBuilder();
      builder.setUserId(response.getUserID());
      
      service.addDelta(request, id, "NULL");
      callback.run(builder.build());
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INVALID_ARGUMENT, "");
    }
  }

  public void getUsername(RpcController controller, Api.GetUsernameRequest request, RpcCallback<Api.GetUsernameResponse> callback){
    String username = service.getUsername(request.getUserId());
    if (username == null){
      throw new ApiError(Code.NOT_FOUND,"username not in use");
    }
    Api.GetUsernameResponse.Builder builder = Api.GetUsernameResponse.newBuilder();
    builder.setUsername(username);
    callback.run(builder.build());
  }

}
