package com.wwttr.main;

import com.wwttr.auth.AuthService;
import com.wwttr.auth.AuthHandlers;
import com.wwttr.game.GameService;
import com.wwttr.game.GameHandlers;
import com.wwttr.health.HealthHandlers;
import com.wwttr.server.Server;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    GameService gameService = GameService.getInstance();
    AuthService authService = AuthService.getInstance();

    GameHandlers gameHandlers = new GameHandlers(gameService, authService);
    AuthHandlers authHandlers = new AuthHandlers(authService);
    HealthHandlers healthHandlers = new HealthHandlers();

    Server server = new Server();

    server.register(gameHandlers);
    server.register(authHandlers);
    server.register(healthHandlers);

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
