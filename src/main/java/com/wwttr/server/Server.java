package com.wwttr.server;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Service;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;

public class Server {

  Map<String, Service> services = new HashMap<String, Service>();
  HttpServer server;

  public void register(Service service)  {
    services.put(service.getDescriptorForType().getFullName(), service);
  }

  public void start(int port) throws IOException {
    if (server != null) {
      stop();
    }
    server = HttpServer.create(new InetSocketAddress(port), 0);

    server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
    Handler handler = Handler.getInstance();
    handler.init(services);
    server.createContext("/", handler);
  }

  public void listen() {
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
