package com.wwttr.auth;

public class AuthService {

  public Api.Account getAccount() {
    AuthService s = new AuthService();
    Api.Account.Builder builder = Api.Account.newBuilder();
    builder.setId("abcdefgh");
    builder.setUsername("user1");
    return builder.build();
  }

  public static void main(String[] args) {
    AuthService service = new AuthService();
    System.out.println(service.getAccount());
  }
}
