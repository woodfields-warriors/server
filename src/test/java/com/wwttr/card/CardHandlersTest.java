package com.wwttr.card;

import com.wwttr.auth.AuthService;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.game.GameService;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.TrainCard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardHandlersTest {

  CardHandlers ch = new CardHandlers();
  CardService cs = CardService.getInstance();
  DatabaseFacade df = DatabaseFacade.getInstance();
  private String[] name = {"test", "test2"};
  private Integer[] points = {1, 2};
  private TrainCard.Color[] trainCardColors = {TrainCard.Color.PINK,TrainCard.Color.GREEN};
  private TrainCard.State[] trainCardStates = {TrainCard.State.HIDDEN,TrainCard.State.HIDDEN};
  private Integer[] trainCardCounts = {5,20};
  GameService gs = GameService.getInstance();
  String GameId = null;
  String playerId = null;
  String user = null;

  @Before
  public void setUp() throws Exception {
    cs.setDefaultTemplates();
    AuthService.getInstance().register("user","password");
    user = df.getUser("user").getUserID();
    CreateResponse cr = gs.createGame("gameName", user, 4);
    GameId = cr.getGameID();
    gs.createPlayer(user,GameId);
    playerId = cr.getPlayerID();
  }

  @After
  public void tearDown() throws Exception {
    df.clearCards();
    df.clearUsers();
    df.clearGames();
    df.clearPlayers();
  }

  @Test
  public void streamTrainCards() {
    Api.StreamTrainCardsRequest.Builder builder =  Api.StreamTrainCardsRequest.newBuilder();
    builder.setPlayerId(playerId);
  }

  @Test
  public void streamDeckStats() {
  }


}