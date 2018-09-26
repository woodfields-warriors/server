package com.wwttr.auth;

public class AuthService {

  public Account getAccount() {
    Account.Builder builder = Account.newBuilder();
    builder.setId("abcdefgh");
    builder.setUsername("user1");
    return builder.build();
  }

  public static void main(String[] args) {
    AuthService service = new AuthService();
    System.out.println(service.getAccount());
  }
}
