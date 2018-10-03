package com.wwttr.main;

import com.wwttr.auth.AuthService;
import com.wwttr.auth.AuthHandlers;
import com.wwttr.game.GameService;
import com.wwttr.game.GameHandlers;

import com.google.protobuf.BlockingService;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.HashMap;

import java.io.IOException;

class Server {

  HashMap<String, BlockingService> services = new HashMap<String, BlockingService>();

  public Server() {
    GameService gameService = new GameService();
    AuthService authService = new AuthService();

    GameHandlers gameHandlers = new GameHandlers(gameService);
    AuthHandlers authHandlers = new AuthHandlers(authService);

    services.put("game", com.wwttr.game.Api.GameService.newReflectiveBlockingService(gameHandlers));
    services.put("auth", com.wwttr.auth.Api.AuthService.newReflectiveBlockingService(authHandlers));
  }

  public void listen(int port) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/", new Handler(services));
    server.setExecutor(null); // creates a default executor
    System.out.println("listening on :8080");
    server.start();
  }
}
