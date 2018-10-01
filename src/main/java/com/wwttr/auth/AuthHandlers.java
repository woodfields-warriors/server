package com.wwttr.auth;

import com.google.protobuf.RpcController;

public class AuthHandlers implements Api.AuthService.BlockingInterface {

  private AuthService service;

  public AuthHandlers(AuthService service) {
    this.service = service;
  }

  public Api.Account createAccount(RpcController controller, Api.CreateAccountRequest request) {
    return null;
  }

  public Api.Account getAccount(RpcController controller, Api.GetAccountRequest request) {
    return null;
  }

  public Api.Account updateAccount(RpcController controller, Api.UpdateAccountRequest request) {
    return null;
  }

  public Api.Empty deleteAccount(RpcController controller, Api.DeleteAccountRequest request) {
    return null;
  }

  public Api.ListAccountsResponse listAccounts(RpcController controller, Api.ListAccountsRequest request) {
    return null;
  }
}
