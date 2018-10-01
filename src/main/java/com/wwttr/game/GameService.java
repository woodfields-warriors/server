package com.wwttr.game;

public class GameService {

  public Api.Game getGame() {
    Api.Game.Builder builder = Api.Game.newBuilder();
    builder.setId("abcdefgh");
    builder.setDisplayName("game 1");
    return builder.build();
  }

  public static void main(String[] args) {
    GameService service = new GameService();
    System.out.println(service.getGame());
  }
}
