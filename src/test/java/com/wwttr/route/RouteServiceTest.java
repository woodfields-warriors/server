package com.wwttr.route;

import com.wwttr.auth.AuthService;
import com.wwttr.card.CardService;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.game.GameService;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.Route;
import com.wwttr.api.NotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.wwttr.models.TrainCard;
import com.wwttr.models.User;
import com.wwttr.route.RouteService;

import static org.junit.Assert.*;

public class RouteServiceTest {
  DatabaseFacade df = DatabaseFacade.getInstance();
  RouteService service = RouteService.getInstance();
  GameService gameService = GameService.getInstance();
  CardService cs = CardService.getInstance();
  String user;
  String GameId;
  String playerId;


  @Before
  public void setUp() throws Exception {
    AuthService.getInstance().register("user","password");
    user = df.getUser("user").getUserID();
    CreateResponse cr = gameService.createGame("gameName", user, 4);
    GameId = cr.getGameID();
    gameService.createPlayer(user,GameId);
    playerId = cr.getPlayerID();
    cs.createFullDecksForGame(GameId);
  }

  @After
  public void tearDown() throws Exception {
    df.clearCards();
    df.clearUsers();
    df.clearGames();
    df.clearPlayers();
  }

  @Test
  public void StreamRoutes() {
    service.initRoutes("testgame1");
    service.initRoutes("testgame2");

    assertEquals(service.streamRoutes("testgame1").limit(98).count(), 98);
    // assertEquals(service.streamRoutes("testgame1").skip(98).count(), 0);
    // service.streamRoutes("testgame1").forEach((Route r) -> {
    //   System.out.println("GOT GAME");
    //   System.out.println(r.getGameId());
    //   System.out.println(r.getRouteId());
    //   throw new Error();
    // });
  }

  @Test
  public void validClaimColoredRoute() {
    ArrayList<TrainCard> trainCards = df.getTrainCards();
    ArrayList<String> trainCardIds = new ArrayList<>();
    Route route = df.getRoutebyId("Los Angeles - El Paso");
    for(TrainCard tc: trainCards){
      if(tc.getColor().equals(TrainCard.Color.BLACK)){
        tc.setPlayerId(playerId);
        tc.setState(TrainCard.State.OWNED);
        if(trainCardIds.size() < route.getLength()) {
          trainCardIds.add(tc.getId());
        }
        try {
          df.updateTrainCard(tc);
        }
        catch (Exception e){
          fail();
        }
      }
    }
    try {
      service.claimRoute(playerId, route.getRouteId(), trainCardIds);
      route = df.getRoutebyId(route.getRouteId());
      assertEquals(playerId,route.getPlayerId());
    }
    catch (Exception e){
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void validClaimGreyRoute(){
    ArrayList<TrainCard> trainCards = df.getTrainCards();
    ArrayList<String> trainCardIds = new ArrayList<>();
    Route route = df.getRoutebyId("Vancouver - Seattle 1");
    for(TrainCard tc: trainCards){
      if(tc.getColor().equals(TrainCard.Color.BLACK)){
        tc.setPlayerId(playerId);
        tc.setState(TrainCard.State.OWNED);
        if(trainCardIds.size() < route.getLength()) {
          trainCardIds.add(tc.getId());
        }
        try {
          df.updateTrainCard(tc);
        }
        catch (Exception e){
          fail();
        }
      }
    }
    try {
      service.claimRoute(playerId, route.getRouteId(), trainCardIds);
      route = df.getRoutebyId(route.getRouteId());
      assertEquals(playerId,route.getPlayerId());
    }
    catch (Exception e){
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void claimRouteWithLocomotives(){
    ArrayList<TrainCard> trainCards = df.getTrainCards();
    ArrayList<String> trainCardIds = new ArrayList<>();
    Route route = df.getRoutebyId("Los Angeles - El Paso");
    for(TrainCard tc: trainCards){
      if(tc.getColor().equals(TrainCard.Color.RAINBOW)){
        tc.setPlayerId(playerId);
        tc.setState(TrainCard.State.OWNED);
        if(trainCardIds.size() < route.getLength()) {
          trainCardIds.add(tc.getId());
        }
        try {
          df.updateTrainCard(tc);
        }
        catch (Exception e){
          fail();
        }
      }
    }
    try {
      service.claimRoute(playerId, route.getRouteId(), trainCardIds);
      route = df.getRoutebyId(route.getRouteId());
      assertEquals(playerId,route.getPlayerId());
    }
    catch (Exception e){
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void claimRouteWithMix(){
    ArrayList<TrainCard> trainCards = df.getTrainCards();
    ArrayList<String> trainCardIds = new ArrayList<>();
    Route route = df.getRoutebyId("Los Angeles - El Paso");
    for(TrainCard tc: trainCards){
      if(tc.getColor().equals(TrainCard.Color.BLACK) || tc.getColor().equals(TrainCard.Color.RAINBOW)){
        tc.setPlayerId(playerId);
        tc.setState(TrainCard.State.OWNED);
        if(trainCardIds.size() < route.getLength()) {
          trainCardIds.add(tc.getId());
        }
        else if(trainCardIds.size() == route.getLength() && tc.getColor().equals(TrainCard.Color.RAINBOW)){
          trainCardIds.remove(route.getLength()-1);
          trainCardIds.add(0,tc.getId());
        }
        else if(trainCardIds.size() == route.getLength() && tc.getColor().equals(TrainCard.Color.BLACK)){
          trainCardIds.remove(route.getLength()-1);
          trainCardIds.add(1,tc.getId());
        }
        try {
          df.updateTrainCard(tc);
        }
        catch (Exception e){
          fail();
        }
      }
    }
    try {
      service.claimRoute(playerId, route.getRouteId(), trainCardIds);
      route = df.getRoutebyId(route.getRouteId());
      assertEquals(playerId,route.getPlayerId());
    }
    catch (Exception e){
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void invalidColorClaimColoredRoute(){
    ArrayList<TrainCard> trainCards = df.getTrainCards();
    ArrayList<String> trainCardIds = new ArrayList<>();
    Route route = df.getRoutebyId("Los Angeles - El Paso");
    for(TrainCard tc: trainCards){
      if(tc.getColor().equals(TrainCard.Color.BLUE)){
        tc.setPlayerId(playerId);
        tc.setState(TrainCard.State.OWNED);
        if(trainCardIds.size() < route.getLength()) {
          trainCardIds.add(tc.getId());
        }
        try {
          df.updateTrainCard(tc);
        }
        catch (Exception e){
          fail();
        }
      }
    }
    try {
      service.claimRoute(playerId, route.getRouteId(), trainCardIds);
      route = df.getRoutebyId(route.getRouteId());
      assertEquals(playerId,route.getPlayerId());
    }
    catch (IllegalArgumentException e){
      assertNotNull(e);
    }
    catch (Exception e){
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void invalidCountClaimColoredRoute(){

    ArrayList<TrainCard> trainCards = df.getTrainCards();
    ArrayList<String> trainCardIds = new ArrayList<>();
    Route route = df.getRoutebyId("Los Angeles - El Paso");
    for(TrainCard tc: trainCards){
      if(tc.getColor().equals(TrainCard.Color.BLACK)){
        tc.setPlayerId(playerId);
        tc.setState(TrainCard.State.OWNED);
        if(trainCardIds.size() <= route.getLength()) {
          trainCardIds.add(tc.getId());
        }
        try {
          df.updateTrainCard(tc);
        }
        catch (Exception e){
          fail();
        }
      }
    }
    try {
      service.claimRoute(playerId, route.getRouteId(), trainCardIds);
      route = df.getRoutebyId(route.getRouteId());
      assertEquals(playerId,route.getPlayerId());
    }
    catch (IllegalArgumentException e){
      assertNotNull(e);
    }
    catch (Exception e){
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void invalidColorClaimGreyRoute(){
    ArrayList<TrainCard> trainCards = df.getTrainCards();
    ArrayList<String> trainCardIds = new ArrayList<>();
    Route route = df.getRoutebyId("Los Angeles - El Paso");
    for(TrainCard tc: trainCards){
      if(tc.getColor().equals(TrainCard.Color.BLACK) || tc.getColor().equals(TrainCard.Color.RAINBOW)){
        tc.setPlayerId(playerId);
        tc.setState(TrainCard.State.OWNED);
        if(trainCardIds.size() < route.getLength()) {
          trainCardIds.add(tc.getId());
        }
        else if(trainCardIds.size() == route.getLength() && tc.getColor().equals(TrainCard.Color.BLACK)){
          trainCardIds.remove(route.getLength()-1);
          trainCardIds.add(0,tc.getId());
        }
        else if(trainCardIds.size() == route.getLength() && tc.getColor().equals(TrainCard.Color.BLUE)){
          trainCardIds.remove(route.getLength()-1);
          trainCardIds.add(1,tc.getId());
        }
        try {
          df.updateTrainCard(tc);
        }
        catch (Exception e){
          fail();
        }
      }
    }
    try {
      service.claimRoute(playerId, route.getRouteId(), trainCardIds);
      route = df.getRoutebyId(route.getRouteId());
      assertEquals(playerId,route.getPlayerId());
    }
    catch (IllegalArgumentException e){
      assertNotNull(e);
    }
    catch (Exception e){
      e.printStackTrace();
      fail();
    }
  }
}
