package com.wwttr.route;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Stream;

import com.wwttr.api.ApiError;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.api.NotFoundException;
import com.wwttr.models.Game;
import com.wwttr.models.TrainCard;
import com.wwttr.models.Route;
import com.wwttr.api.FailedPreconditionException;
import java.util.Random;

// route Service is of the Singleton Pattern
public class RouteService {

  //singleton object
  private DatabaseFacade database;
  private Random rn = new Random();

  private static RouteService globalInstance;
  public static RouteService getInstance(){
    if(globalInstance == null){
      globalInstance = new RouteService();
    }
    return globalInstance;
  }

  private RouteService() {
    database = DatabaseFacade.getInstance();
  }

  public void initRoutes(String gameId) {
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Vancouver", "Seattle",  TrainCard.Color.GREY, 1, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Vancouver", "Seattle",  TrainCard.Color.GREY, 1, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Portland", "Seattle",  TrainCard.Color.GREY, 1, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Portland", "Seattle",  TrainCard.Color.GREY, 1, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Vancouver", "Calgary",    TrainCard.Color.GREY, 3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "San Francisco", "Los Angeles",   TrainCard.Color.PINK, 3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "San Francisco", "Los Angeles",   TrainCard.Color.YELLOW, 3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "San Francisco", "Portland",  TrainCard.Color.PINK, 5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "San Francisco", "Portland",  TrainCard.Color.GREEN, 5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "San Francisco", "Salt Lake City",  TrainCard.Color.ORANGE, 5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "San Francisco", "Salt Lake City",  TrainCard.Color.WHITE, 5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Portland", "Salt Lake City", TrainCard.Color.BLUE,  6, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Seattle", "Calgary", TrainCard.Color.GREY,  4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Los Angeles", "Las Vegas", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Los Angeles", "Phoenix", TrainCard.Color.GREY,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Los Angeles", "El Paso", TrainCard.Color.BLACK,  6, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Las Vegas", "Salt Lake City", TrainCard.Color.ORANGE,  3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Calgary", "Helena", TrainCard.Color.GREY,    4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Calgary", "Winnipeg", TrainCard.Color.WHITE,    6, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Helena", "Winnipeg", TrainCard.Color.BLUE,    4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Helena", "Duluth", TrainCard.Color.ORANGE,   6, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Helena", "Omaha", TrainCard.Color.RED,    5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Helena", "Denver", TrainCard.Color.GREEN,   4, gameId));{}
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Salt Lake City", "Helena", TrainCard.Color.PINK,  3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Phoenix", "El Paso", TrainCard.Color.GREY,  3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Phoenix", "Santa Fe", TrainCard.Color.GREY,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Phoenix", "Denver", TrainCard.Color.WHITE,  5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Santa Fe", "Denver", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Santa Fe", "El Paso", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Santa Fe", "Oklahoma City", TrainCard.Color.BLUE,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "El Paso", "Oklahoma City", TrainCard.Color.YELLOW,  5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "El Paso", "Houston", TrainCard.Color.GREEN,  6, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Dallas", "El Paso", TrainCard.Color.RED,  4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Denver", "Salt Lake City", TrainCard.Color.RED,  3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Denver", "Salt Lake City", TrainCard.Color.YELLOW,  3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Denver", "Oklahoma City", TrainCard.Color.RED,  4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Denver", "Kansas City", TrainCard.Color.ORANGE,  4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Denver", "Kansas City", TrainCard.Color.BLACK,  4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Denver", "Omaha", TrainCard.Color.PINK,   4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Duluth", "Winnipeg", TrainCard.Color.BLACK,   4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Duluth", "Sault St. Marie", TrainCard.Color.GREY,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Duluth", "Toronto", TrainCard.Color.PINK,  6, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Duluth", "Chicago", TrainCard.Color.RED,  3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Duluth", "Omaha", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Duluth", "Omaha", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Winnipeg", "Sault St. Marie", TrainCard.Color.GREY,    6, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Omaha", "Chicago", TrainCard.Color.BLUE,   4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Omaha", "Kansas City", TrainCard.Color.BLUE,   1, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Omaha", "Kansas City", TrainCard.Color.PINK,   1, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Oklahoma City", "Kansas City", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Oklahoma City", "Kansas City", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Oklahoma City", "Little Rock", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Oklahoma City", "Dallas", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Oklahoma City", "Dallas", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Dallas", "Houston", TrainCard.Color.GREY,  1, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Dallas", "Houston", TrainCard.Color.GREY,  1, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Dallas", "Little Rock", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New Orleans", "Little Rock", TrainCard.Color.GREEN,  3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New Orleans", "Houston", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New Orleans", "Atlanta", TrainCard.Color.YELLOW,   4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New Orleans", "Atlanta", TrainCard.Color.ORANGE,   4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New Orleans", "Miami", TrainCard.Color.RED,  6, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Saint Louis", "Little Rock", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Saint Louis", "Nashville", TrainCard.Color.GREY,    2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Saint Louis", "Pittsburgh", TrainCard.Color.GREEN,    5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Saint Louis", "Chicago", TrainCard.Color.GREEN,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Saint Louis", "Chicago", TrainCard.Color.WHITE,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Nashville", "Little Rock", TrainCard.Color.WHITE,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Nashville", "Atlanta", TrainCard.Color.GREY,    1, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Nashville", "Raleigh", TrainCard.Color.BLACK,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Nashville", "Pittsburgh", TrainCard.Color.YELLOW,    4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Pittsburgh", "Chicago", TrainCard.Color.ORANGE,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Pittsburgh", "Chicago", TrainCard.Color.BLACK,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Pittsburgh", "Raleigh", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Pittsburgh", "Washington", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Pittsburgh", "New York", TrainCard.Color.WHITE,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Pittsburgh", "New York", TrainCard.Color.GREEN,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Pittsburgh", "Toronto", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Atlanta", "Miami", TrainCard.Color.BLUE,   5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Atlanta", "Charleston", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Atlanta", "Raleigh", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Atlanta", "Raleigh", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Charleston", "Raleigh", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Charleston", "Miami", TrainCard.Color.PINK,  4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Raleigh", "Washington", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Raleigh", "Washington", TrainCard.Color.GREY,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New York", "Washington", TrainCard.Color.ORANGE,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New York", "Washington", TrainCard.Color.BLACK,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New York", "Boston", TrainCard.Color.YELLOW,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New York", "Boston", TrainCard.Color.RED,  2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "New York", "Montreal", TrainCard.Color.BLUE,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Montreal", "Boston", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Montreal", "Boston", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Montreal", "Toronto", TrainCard.Color.GREY,   3, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Montreal", "Sault St. Marie", TrainCard.Color.BLACK,    5, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Toronto", "Sault St. Marie", TrainCard.Color.GREY,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Toronto", "Chicago", TrainCard.Color.WHITE,  4, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Seattle", "Helena", TrainCard.Color.YELLOW,  6, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Kansas City", "Saint Louis", TrainCard.Color.PINK,   2, gameId));
      database.addRoute(new Route(("route" + Integer.toString(rn.nextInt() & Integer.MAX_VALUE)), "Kansas City", "Saint Louis", TrainCard.Color.BLUE,   2, gameId));
  }

  public Stream<Route> streamRoutes(String gameId) {
    return database.streamRoutes().filter((Route r) -> r.getGameId().equals(gameId));
  }

  public void claimRoute(String playerId, String routeId, List<String> cardIds) throws NotFoundException, IllegalArgumentException, FailedPreconditionException{
    if(database.getPlayer(playerId) == null){
      throw new NotFoundException("player not found");
    }
    Route route = database.getRoutebyId(routeId);
    Game game = database.getGame(route.getGameId());
    if(game.getPlayerIDs().size() <= 2){
      String otherPlayer = "";
      for(String id : game.getPlayerIDs()){
        if(!id.equals(playerId)){
          otherPlayer = id;
        }
      }
      List<Route> myRoutes = database.getRoutesOwnedByPlayer(playerId);
      List<Route> otherRoutes = database.getRoutesOwnedByPlayer(otherPlayer);
      for(Route tempRoute : myRoutes){
        if(tempRoute.getFirstCityId().equals(route.getFirstCityId())){
          if(tempRoute.getSecondCityId().equals(route.getSecondCityId())){
            throw new IllegalArgumentException("double routes not claimable with 2 players");
          }
        }
      }
      for(Route tempRoute : otherRoutes){
        if(tempRoute.getFirstCityId().equals(route.getFirstCityId())){
          if(tempRoute.getSecondCityId().equals(route.getSecondCityId())){
            throw new IllegalArgumentException("double routes not claimable with 2 players");
          }
        }
      }
    }
    if(cardIds.size() != route.getLength()){
      throw new IllegalArgumentException("cards given != route length. cards = " + cardIds.size() + " routeLength = " + route.getLength());
    }
    ArrayList<TrainCard> cards = new ArrayList<>();
    for(String cardId : cardIds){
      cards.add(database.getTrainCard(cardId));
    }
    TrainCard.Color color = TrainCard.Color.UNSPECIFIED;
    //Checks if all colors match or are Locomotives
    //Checks if cards are owned by the player
    for(TrainCard card : cards){
      if(!card.getPlayerId().equals(playerId)){
        throw new IllegalArgumentException("card not owned by player");
      }
      if(!card.getState().equals(TrainCard.State.OWNED)){
        throw new IllegalArgumentException("card not owned");
      }
      if(color.equals(TrainCard.Color.UNSPECIFIED)){
        color = card.getColor();
      }
      else if(color.equals(TrainCard.Color.RAINBOW) && !card.getColor().equals(TrainCard.Color.RAINBOW)){
        color = card.getColor();
      }
      else if(!color.equals(card.getColor()) && !card.getColor().equals(TrainCard.Color.RAINBOW)){
          throw new IllegalArgumentException("Incompatible card color");
        }
    }
    //checks if colors match the color of the route
    if(!route.getTrainColor().equals(TrainCard.Color.GREY) && !color.equals(route.getTrainColor()) && !color.equals(TrainCard.Color.RAINBOW)){
      throw new IllegalArgumentException("Card colors don't match route color");
    }
    if(!route.getPlayerId().equals("")){
      throw new FailedPreconditionException();
    }
    route.setPlayerId(playerId);
    if (database.updateRoute(route) == null) {
      throw new NotFoundException("route " + route.getRouteId() + " not found");
    }
    //return cards to the deck
    for(TrainCard card: cards){
      card.setState(TrainCard.State.HIDDEN);
      card.setPlayerId("");
      database.updateTrainCard(card);
    }
  }


}
