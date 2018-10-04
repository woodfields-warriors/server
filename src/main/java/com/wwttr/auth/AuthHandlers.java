package com.wwttr.auth;

import com.google.protobuf.RpcController;
//import com.wwttr.api.InvalidArgumentException;


public class AuthHandlers implements Api.AuthService.BlockingInterface {

  private AuthService service;

  public AuthHandlers(AuthService service) {
    this.service = service;
  }

  public Api.LoginResponse login(RpcController controller, Api.LoginAccountRequest request) {
    try{
      return service.login(request.getUsername(), request.getPassword());
    }
    catch (Exception e){
      return null;
    }
  }

  public Api.LoginResponse register(RpcController controller, Api.LoginAccountRequest request) {
    try {
      return service.register(request.getUsername(), request.getPassword());
    }
    catch (Exception e){
      return null;
    }
  }

}
