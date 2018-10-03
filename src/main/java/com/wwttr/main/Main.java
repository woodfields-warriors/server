package com.wwttr.main;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    try {
      Server server = new Server();
      System.out.println("listening on port 8080");
      server.listen(8080);
    }
    catch (IOException e) {
      System.out.println(e);
      System.exit(1);
    }
  }
}
