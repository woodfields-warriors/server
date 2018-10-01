package com.wwttr.main;

import com.wwttr.auth.AuthService;
import com.wwttr.auth.AuthHandlers;
import com.wwttr.game.GameService;
import com.wwttr.game.GameHandlers;

import com.google.protobuf.BlockingService;
import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.Descriptors.MethodDescriptor;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Main {
  public static void main(String[] args) {

    List<BlockingService> services = new ArrayList<BlockingService>();

    GameService gameService = new GameService();
    AuthService authService = new AuthService();

    GameHandlers gameHandlers = new GameHandlers(gameService);
    AuthHandlers authHandlers = new AuthHandlers(authService);

    services.add(com.wwttr.game.Api.GameService.newReflectiveBlockingService(gameHandlers));
    services.add(com.wwttr.auth.Api.AuthService.newReflectiveBlockingService(authHandlers));

    HttpServer server = null;
    try {
      server = HttpServer.create(new InetSocketAddress(8080), 0);
    }
    catch (IOException e) {
      System.out.println(e);
      System.exit(5);
    }

    server.createContext("/", new Handler(services));
    server.setExecutor(null); // creates a default executor
    server.start();
  }
}

class Handler implements HttpHandler {

  private List<BlockingService> services;

  public Handler(List<BlockingService> services) {
    this.services = services;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    Controller controller = new Controller(exchange);



    for (BlockingService service : services) {
      for (MethodDescriptor method : service.getDescriptorForType().getMethods()) {
        method.
        try {
          // Get Response
          InputStream inStream = exchange.getRequestBody();
          BufferedReader buffered = new BufferedReader(new InputStreamReader(is));

        }
        finally {
        if (connection != null) {
         connection.disconnect();
        }
        }


        service.callBlockingMethod(method, controller, request);
      }
    }

  }
}

class Controller implements RpcController {

  private HttpExchange exchange;
  private Throwable error;

  public Controller(HttpExchange exchange) {
    this.exchange = exchange;
  }

  public String errorText() {
    if (error == null) {
      return null;
    }
    return error.getMessage();
  }

  public boolean failed() {
    return error != null;
  }

  public boolean isCanceled() {
    return false;
  }

  public void notifyOnCancel(RpcCallback<Object> callback) {

  }

  public void reset() {

  }

  public void setFailed(String reason) {

  }

  public void startCancel() {

  }
}
