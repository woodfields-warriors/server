package com.wwttr.main;

import com.wwttr.auth.AuthService;
import com.wwttr.auth.AuthHandlers;
import com.wwttr.game.GameService;
import com.wwttr.game.GameHandlers;
import com.wwttr.server.Server;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    GameService gameService = new GameService();
    AuthService authService = AuthService.getInstance();

    GameHandlers gameHandlers = new GameHandlers(gameService);
    AuthHandlers authHandlers = new AuthHandlers(authService);

    Server server = new Server();

    server.register(com.wwttr.game.Api.GameService.newReflectiveBlockingService(gameHandlers));
    server.register(com.wwttr.auth.Api.AuthService.newReflectiveBlockingService(authHandlers));

    try {
      server.start(8080);
      System.out.println("listening on port 8080");
    }
    catch (IOException e) {
      System.out.println(e);
      System.exit(1);
    }
  }
}
