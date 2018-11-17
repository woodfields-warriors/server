package com.wwttr.route;

import java.util.*;

import com.wwttr.api.ApiError;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.api.NotFoundException;
import com.wwttr.models.TrainColor;
import com.wwttr.models.Route;


// Game Service is of the Singleton Pattern
public class RouteService {

  //singleton object
  private DatabaseFacade database;

  public static RouteService getInstance(){
    if(gameServiceInstance == null){
      gameServiceInstance = new RouteService();
    }
    return gameServiceInstance;
  }

  private RouteService() {
    database = DatabaseFacade.getInstance();
  }

  public void initRoutes(String gameId) {
      database.addRoute(new Route("vancouver-seattle", "Vancouver", "Seattle",  TrainColor.GREY, 1, gameId));
      database.addRoute(new Route("vancouver-seattle", "Vancouver", "Seattle",  TrainColor.GREY, 1, gameId));
      database.addRoute(new Route("vancouver-calgary", "Vancouver", "Calgary",    TrainColor.GREY, 3, gameId));
      database.addRoute(new Route("sanFrancisco-losAngeles", "San Francisco", "Los Angeles",   TrainColor.PINK, 3, gameId));
      database.addRoute(new Route("sanFrancisco-losAngeles", "San Francisco", "Los Angeles",   TrainColor.YELLOW, 3, gameId));
      database.addRoute(new Route("sanFrancisco-portland", "San Francisco", "Portland",  TrainColor.PINK, 5, gameId));
      database.addRoute(new Route("sanFrancisco-portland", "San Francisco", "Portland",  TrainColor.GREEN, 5, gameId));
      database.addRoute(new Route("sanFrancisco-saltLakeCity", "San Francisco", "Salt Lake City",  TrainColor.ORANGE, 5, gameId));
      database.addRoute(new Route("sanFrancisco-saltLakeCity", "San Francisco", "Salt Lake City",  TrainColor.WHITE, 5, gameId));
      database.addRoute(new Route("portland - Salt Lake City", "portland", "Salt Lake City", TrainColor.BLUE,  6, gameId));
      database.addRoute(new Route("Seattle - Calgary", "Seattle", "Calgary", TrainColor.GREY,  4, gameId));
      database.addRoute(new Route("Los Angeles - Las Vegas", "Los Angeles", "Las Vegas", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Los Angeles - Pheonix", "Los Angeles", "Pheonix", TrainColor.GREY,   3, gameId));
      database.addRoute(new Route("Los Angeles - El Paso", "Los Angeles", "El Paso", TrainColor.BLACK,  6, gameId));
      database.addRoute(new Route("Las Vegas - SaltLake City", "Las Vegas", "SaltLake City", TrainColor.ORANGE,  3, gameId));
      database.addRoute(new Route("Calgary - Helena", "Calgary", "Helena", TrainColor.GREY,    4, gameId));
      database.addRoute(new Route("Calgary - Winnipeg", "Calgary", "Winnipeg", TrainColor.WHITE,    6, gameId));
      database.addRoute(new Route("Helena - Winnipeg", "Helena", "Winnipeg", TrainColor.BLUE,    4, gameId));
      database.addRoute(new Route("Helena - Duluth", "Helena", "Duluth", TrainColor.ORANGE,   6, gameId));
      database.addRoute(new Route("Helena - Omaha", "Helena", "Omaha", TrainColor.RED,    5, gameId));
      database.addRoute(new Route("Helena - Denver", "Helena", "Denver", TrainColor.GREEN,   4, gameId));
      database.addRoute(new Route("Salt Lake City - Helena", "Salt Lake City", "Helena", TrainColor.PINK,  3, gameId));
      database.addRoute(new Route("Pheonix - El Paso", "Pheonix", "El Paso", TrainColor.GREY,  3, gameId));
      database.addRoute(new Route("Pheonix - Santa Fe", "Pheonix", "Santa Fe", TrainColor.GREY,   3, gameId));
      database.addRoute(new Route("Pheonix - Denver", "Pheonix", "Denver", TrainColor.WHITE,  5, gameId));
      database.addRoute(new Route("Santa Fe - Denver", "Santa Fe", "Denver", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Santa Fe - El Paso", "Santa Fe", "El Paso", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Santa Fe - Oklahoma City", "Santa Fe", "Oklahoma City", TrainColor.BLUE,   3, gameId));
      database.addRoute(new Route("El Paso - Oklahoma City", "El Paso", "Oklahoma City", TrainColor.YELLOW,  5, gameId));
      database.addRoute(new Route("El Paso - Houston", "El Paso", "Houston", TrainColor.GREEN,  6, gameId));
      database.addRoute(new Route("Dallas - El Paso", "Dallas", "El Paso", TrainColor.RED,  4, gameId));
      database.addRoute(new Route("Denver - Salt Lake City", "Denver", "Salt Lake City", TrainColor.RED,  3, gameId));
      database.addRoute(new Route("Denver - Salt Lake City", "Denver", "Salt Lake City", TrainColor.YELLOW,  3, gameId));
      database.addRoute(new Route("Denver - Oklahoma City", "Denver", "Oklahoma City", TrainColor.RED,  4, gameId));
      database.addRoute(new Route("Denver - Kansas City", "Denver", "Kansas City", TrainColor.ORANGE,  4, gameId));
      database.addRoute(new Route("Denver - Kansas City", "Denver", "Kansas City", TrainColor.BLACK,  4, gameId));
      database.addRoute(new Route("Denver - Omaha", "Denver", "Omaha", TrainColor.PINK,   4, gameId));
      database.addRoute(new Route("Duluth - Winnipeg", "Duluth", "Winnipeg", TrainColor.BLACK,   4, gameId));
      database.addRoute(new Route("Duluth - Sault St. Marie", "Duluth", "Sault St. Marie", TrainColor.GREY,   3, gameId));
      database.addRoute(new Route("Duluth - Toronto", "Duluth", "Toronto", TrainColor.PINK,  6, gameId));
      database.addRoute(new Route("Duluth - Chicago", "Duluth", "Chicago", TrainColor.RED,  3, gameId));
      database.addRoute(new Route("Duluth - Omaha", "Duluth", "Omaha", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Duluth - Omaha", "Duluth", "Omaha", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Winnipeg - Sault St. Mary", "Winnipeg", "Sault St. Mary", TrainColor.GREY,    6, gameId));
      database.addRoute(new Route("Omaha - Chicago", "Omaha", "Chicago", TrainColor.BLUE,   4, gameId));
      database.addRoute(new Route("Omaha - Kansas City", "Omaha", "Kansas City", TrainColor.BLUE,   1, gameId));
      database.addRoute(new Route("Omaha - Kansas City", "Omaha", "Kansas City", TrainColor.PINK,   1, gameId));
      database.addRoute(new Route("Oklahoma City - Kansas City", "Oklahoma City", "Kansas City", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("Oklahoma City - Kansas City", "Oklahoma City", "Kansas City", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("Oklahoma City - Little Rock", "Oklahoma City", "Little Rock", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("Oklahoma City - Dallas", "Oklahoma City", "Dallas", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("Oklahoma City - Dallas", "Oklahoma City", "Dallas", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("Dallas - Houston", "Dallas", "Houston", TrainColor.GREY,  1, gameId));
      database.addRoute(new Route("Dallas - Houston", "Dallas", "Houston", TrainColor.GREY,  1, gameId));
      database.addRoute(new Route("Dallas - Little Rock", "Dallas", "Little Rock", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("New Orleans - Little Rock", "New Orleans", "Little Rock", TrainColor.GREEN,  3, gameId));
      database.addRoute(new Route("New Orleans - Houston", "New Orleans", "Houston", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("New Orleans - Atlanta", "New Orleans", "Atlanta", TrainColor.YELLOW,   4, gameId));
      database.addRoute(new Route("New Orleans - Atlanta", "New Orleans", "Atlanta", TrainColor.ORANGE,   4, gameId));
      database.addRoute(new Route("New Orleans - Miami", "New Orleans", "Miami", TrainColor.RED,  6, gameId));
      database.addRoute(new Route("Saint Louis - Little Rock", "Saint Louis", "Little Rock", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Saint Louis - Nashville", "Saint Louis", "Nashville", TrainColor.GREY,    2, gameId));
      database.addRoute(new Route("Saint Louis - Pittsburgh", "Saint Louis", "Pittsburgh", TrainColor.GREEN,    5, gameId));
      database.addRoute(new Route("Saint Louis - Chicago", "Saint Louis", "Chicago", TrainColor.GREEN,   2, gameId));
      database.addRoute(new Route("Saint Louis - Chicago", "Saint Louis", "Chicago", TrainColor.WHITE,   2, gameId));
      database.addRoute(new Route("Nashville - Little Rock", "Nashville", "Little Rock", TrainColor.WHITE,   3, gameId));
      database.addRoute(new Route("Nashville - Atlanta", "Nashville", "Atlanta", TrainColor.GREY,    1, gameId));
      database.addRoute(new Route("Nashville - Raleigh", "Nashville", "Raleigh", TrainColor.BLACK,   3, gameId));
      database.addRoute(new Route("Nashville - Pittsburgh", "Nashville", "Pittsburgh", TrainColor.YELLOW,    4, gameId));
      database.addRoute(new Route("Pittsburgh - Chicago", "Pittsburgh", "Chicago", TrainColor.ORANGE,   3, gameId));
      database.addRoute(new Route("Pittsburgh - Chicago", "Pittsburgh", "Chicago", TrainColor.BLACK,   3, gameId));
      database.addRoute(new Route("Pittsburgh - Raleigh", "Pittsburgh", "Raleigh", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Pittsburgh - Washington", "Pittsburgh", "Washington", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Pittsburgh - New York", "Pittsburgh", "New York", TrainColor.WHITE,   2, gameId));
      database.addRoute(new Route("Pittsburgh - New York", "Pittsburgh", "New York", TrainColor.GREEN,   2, gameId));
      database.addRoute(new Route("Pittsburgh - Toronto", "Pittsburgh", "Toronto", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Atlanta - Miami", "Atlanta", "Miami", TrainColor.BLUE,   5, gameId));
      database.addRoute(new Route("Atlanta - Charleston", "Atlanta", "Charleston", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Atlanta - Raleigh", "Atlanta", "Raleigh", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Atlanta - Raleigh", "Atlanta", "Raleigh", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Charleston - Raleigh", "Charleston", "Raleigh", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("Charleston - Miami", "Charleston", "Miami", TrainColor.PINK,  4, gameId));
      database.addRoute(new Route("Releigh - Washington", "Releigh", "Washington", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("Releigh - Washington", "Releigh", "Washington", TrainColor.GREY,  2, gameId));
      database.addRoute(new Route("New York - Washington", "New York", "Washington", TrainColor.ORANGE,  2, gameId));
      database.addRoute(new Route("New York - Washington", "New York", "Washington", TrainColor.BLACK,  2, gameId));
      database.addRoute(new Route("New York - Boston", "New York", "Boston", TrainColor.YELLOW,  2, gameId));
      database.addRoute(new Route("New York - Boston", "New York", "Boston", TrainColor.RED,  2, gameId));
      database.addRoute(new Route("New York - Montreal", "New York", "Montreal", TrainColor.BLUE,   3, gameId));
      database.addRoute(new Route("Montreal - Boston", "Montreal", "Boston", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Montreal - Boston", "Montreal", "Boston", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Montreal - Toronto", "Montreal", "Toronto", TrainColor.GREY,   3, gameId));
      database.addRoute(new Route("Montreal - Sault St. Marie", "Montreal", "Sault St. Marie", TrainColor.BLACK,    5, gameId));
      database.addRoute(new Route("Toronto - Sault St. Marie", "Toronto", "Sault St. Marie", TrainColor.GREY,   2, gameId));
      database.addRoute(new Route("Toronto - Chicago", "Toronto", "Chicago", TrainColor.WHITE,  4, gameId));
      database.addRoute(new Route("seattle - Helena", "seattle", "Helena", TrainColor.YELLOW,  6, gameId));
      database.addRoute(new Route("Kansas City - Saint Louis", "Kansas City", "Saint Louis", TrainColor.PINK,   2, gameId));
      database.addRoute(new Route("Kansas City - Saint Louis", "Kansas City", "Saint Louis", TrainColor.BLUE,   2, gameId));
  }

  public List<Route> streamRoutes(String gameId) {
    return database.streamRoutes(gameId);
  }

  public Route getRoute(String routeId) {
    Route route = database.getRoute(routeId);
    return route;
  }


}
