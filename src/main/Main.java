package com.wwttr.main;

import com.wwttr.auth.AuthService;
import com.wwttr.game.GameService;

public class Main {
  public static void main(String[] args) {
    GameService service = new GameService();
    System.out.println(service.getGame());
  }
}
