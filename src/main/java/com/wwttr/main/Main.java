package com.wwttr.main;

import com.wwttr.auth.AuthService;
import com.wwttr.auth.AuthHandlers;
import com.wwttr.game.GameService;
import com.wwttr.game.GameHandlers;
import com.wwttr.chat.ChatHandlers;
import com.wwttr.chat.ChatService;
import com.wwttr.card.CardHandlers;
import com.wwttr.card.CardService;
import com.wwttr.health.HealthHandlers;
import com.wwttr.server.Server;
import com.wwttr.route.RouteHandlers;
import com.wwttr.route.RouteService;
import com.wwttr.database.DatabaseFacade;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    GameService gameService = GameService.getInstance();
    AuthService authService = AuthService.getInstance();
    ChatService chatService = ChatService.getInstance();
    RouteService routeService = RouteService.getInstance();
    // CardService cardService = CardService.getInstance();

    GameHandlers gameHandlers = new GameHandlers(gameService, authService);
    AuthHandlers authHandlers = new AuthHandlers(authService);
    ChatHandlers chatHandlers = new ChatHandlers(chatService);
    CardHandlers cardHandlers = new CardHandlers();
    RouteHandlers routeHandlers = new RouteHandlers(routeService);
    HealthHandlers healthHandlers = new HealthHandlers();

    DatabaseFacade df = DatabaseFacade.getInstance();
    if(args.length < 3){
      System.out.println("Usage: persistance classname, then path to jar," +
                          "then number of deltas between checkpoints");
      return;
    }
    df.createDaos(args[0],args[1]);
    /*
    if (args[0].equals("r")){
      try{
        df.createDaos("com.wwttr.dao.DAOFactoryRelational");
      }
      catch(Exception e){
        return;
      }
    }
    else if(args[0].equals("nr")){
      try{
        df.createDaos("com.wwttr.dao.DAOFactoryNonRelational");
      }
      catch(Exception e){
        return;
      }
    }
    else{
      System.out.println("Enter either 'r' or 'nr' for persistance type");
    }*/
    df.setStorageInterval( Integer.parseInt(args[2]));
    Server server = new Server();

    server.register(gameHandlers);
    server.register(authHandlers);
    server.register(healthHandlers);
    server.register(chatHandlers);
    server.register(cardHandlers);
    server.register(routeHandlers);
    server.register(cardHandlers);

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
