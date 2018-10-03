package com.wwttr.server;

import com.google.protobuf.BlockingService;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;

public class Server {

  Map<String, BlockingService> services = new HashMap<String, BlockingService>();
  HttpServer server;

  public void register(BlockingService service)  {
    services.put(service.getDescriptorForType().getFullName(), service);
  }

  public void start(int port) throws IOException {
    if (server != null) {
      stop();
    }
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/", new Handler(services));
    server.setExecutor(null); // creates a default executor
    System.out.println("listening on :8080");
    server.start();
  }

  public void stop() {
    if (server == null) {
      return;
    }
    server.stop(0);
    server = null;
  }
}
