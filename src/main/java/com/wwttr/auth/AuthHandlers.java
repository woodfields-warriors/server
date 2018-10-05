package com.wwttr.auth;

import com.google.protobuf.RpcController;
import com.wwttr.models.LoginResponse;
import com.wwttr.api.Code;
import com.wwttr.api.ApiError;
//import com.wwttr.api.InvalidArgumentException;


public class AuthHandlers implements Api.AuthService.BlockingInterface {

  private AuthService service;

  public AuthHandlers(AuthService service) {
    this.service = service;
  }

  public Api.LoginResponse login(RpcController controller, Api.LoginAccountRequest request) {
    try{
      LoginResponse response = service.login(request.getUsername(), request.getPassword());
      Api.LoginResponse.Builder builder = Api.LoginResponse.newBuilder();
      builder.setUserId(response.getUserID());
      return builder.build();
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL, "");
    }
  }

  public Api.LoginResponse register(RpcController controller, Api.LoginAccountRequest request) {
    try {
      LoginResponse response = service.register(request.getUsername(), request.getPassword());
      Api.LoginResponse.Builder builder = Api.LoginResponse.newBuilder();
      builder.setUserId(response.getUserID());
      return builder.build();
    }
    catch (Exception e){
      e.printStackTrace();
      throw new ApiError(Code.INTERNAL, "");
    }
  }

}
