package com.wwttr.card;

import com.wwttr.api.NotFoundException;
import com.wwttr.auth.AuthService;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.game.GameService;
import com.wwttr.models.DeckStats;
import com.wwttr.models.DestinationCard;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.TrainCard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class CardServiceTest {
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
  public void generateDeckTemplate() {
    System.out.println("generateDeckTemplateTest");
    String[] name = {"test", "test2"};
    Integer[] point = {1, 2};
    try {
      cs.generateDestinationDeckTemplate(name, name, point);
      assert (cs.getFullDestinationDeckTemplate().size() == 2);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void createFullDeckForGame() {
    System.out.println("createFullDeckForGameTest");
    try {
      df.clearTrainCards();
      cs.generateTrainCardDeckTemplate(trainCardColors,trainCardStates,trainCardCounts);
      cs.generateDestinationDeckTemplate(name, name, points);
      GameService.getInstance().startGame(GameId);
      //cs.createFullDecksForGame(GameId);
      assert (df.getDestinationCards().size() == name.length);
      assert (df.getTrainCards().size() == 25);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void getDestinationCard() {
    System.out.println("getDestinationCardTest");
    try {
      cs.createFullDecksForGame(GameId);
      List<DestinationCard> cards = cs.peekDestinationCards(GameId);
      DestinationCard card = cs.getDestinationCard(cards.get(0).getId());
      assertNotNull(card);
    } catch (NotFoundException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void peekDestinationCards() {
    System.out.println("peekDestinationCardsTest");
    try {
      cs.createFullDecksForGame(GameId);
      List<DestinationCard> cards = cs.peekDestinationCards(GameId);
      assertNotNull(cards);
      assertEquals(cards.get(0).getGameId(),GameId);
    } catch (NotFoundException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void claimDestinationCards(){
    System.out.println("claimDestinationCardsTest");
    try {
      cs.createFullDecksForGame(GameId);
      List<DestinationCard> cards = cs.peekDestinationCards(GameId);
      List<String> idsToClaim = new ArrayList<>();
      for (DestinationCard card : cards) {
        idsToClaim.add(card.getId());
      }
      cs.claimDestinationCards(idsToClaim, playerId);
      for (int i = 0; i < cards.size(); i++) {
        assert (cs.getDestinationCard(cards.get(i).getId()).getPlayerId().equals(playerId));
      }
    } catch (NotFoundException e) {
      e.printStackTrace();
      fail();
    }
  }


  @Test
  public void streamDeckStats() {
      try {
        System.out.println("streamDeckStats");
        cs.createFullDecksForGame(GameId);
        Stream<DeckStats> stream = cs.streamDeckStats(GameId).limit(9);
        List<DeckStats> list = stream.collect(Collectors.toList());
        assert(list.size()>0);
        for(DeckStats card : list){
          assertNotNull(card);
        }
      }
      catch (NotFoundException e){
        fail();
      }
  }

  @Test
  public void dealTrainCards() {
    try{
      cs.createFullDecksForGame(GameId);
      Integer visibleCards = 0;
      Integer cardsInHand = 0;
      for(TrainCard card : df.getTrainCardsForGame(GameId))
      {
        if(card.getState().equals(TrainCard.State.VISIBLE))
          visibleCards++;
        else if(card.getState().equals(TrainCard.State.OWNED))
          cardsInHand++;
      }
      assert(visibleCards == 5);
      Game game = df.getGame(GameId);
      Integer expectedCards = game.getPlayerIDs().size()*4;
      assertEquals(expectedCards,cardsInHand);
    }
    catch (NotFoundException e){
      fail();
    }
  }

  @Test
  public void claimTrainCardFromDeck() {
    try{
      cs.createFullDecksForGame(GameId);
      Integer startingCardsInHand = df.getTrainCardsForPlayer(playerId).size();
      cs.claimTrainCardFromDeck(playerId);
      Integer cardsInHand =  df.getTrainCardsForPlayer(playerId).size();
      Integer expectedCardsInHand = startingCardsInHand+1;
      assertEquals(expectedCardsInHand,cardsInHand);
    }
    catch (NotFoundException e){
      fail();
    }
  }

  @Test
  public void claimFaceUpTrainCard() {
    try{
      cs.createFullDecksForGame(GameId);
      Integer startingCardsInHand = df.getTrainCardsForPlayer(playerId).size();
      ArrayList<TrainCard> trainCards = df.getTrainCardsForGame(GameId);
      ArrayList<TrainCard> visibleCards = new ArrayList<>();
      for(TrainCard card : trainCards){
        if(card.getState().equals(TrainCard.State.VISIBLE))
          visibleCards.add(card);
      }
      TrainCard claimed = visibleCards.get(0);
      cs.claimFaceUpTrainCard(playerId,claimed.getId());
      Integer cardsInHand =  df.getTrainCardsForPlayer(playerId).size();
      Integer expectedCardsInHand = startingCardsInHand + 1;
      assertEquals(expectedCardsInHand,cardsInHand);
      assertEquals(TrainCard.State.OWNED,df.getTrainCard(claimed.getId()).getState());
    }
    catch (NotFoundException e){
      fail();
    }
  }

  @Test
  public void getTrainCardsInHand() {
    try{
      cs.createFullDecksForGame(GameId);
      List<TrainCard> cards = df.getTrainCardsForPlayer(playerId);
      assertEquals(4,cards.size());
    }
    catch (NotFoundException e){
      fail();
    }
  }

  @Test
  public void streamTrainCards() {
    try {
      System.out.println("streamTrainCardsStart");
      cs.createFullDecksForGame(GameId);
      Stream<TrainCard> stream = cs.streamTrainCards(playerId).limit(9);
      List<TrainCard> list = stream.collect(Collectors.toList());
      assertEquals(9,list.size());
      for(TrainCard card : list){
        assertNotEquals(TrainCard.State.UNSPECIFIED,card.getState());
        assertNotEquals(TrainCard.State.HIDDEN,card.getState());
      }
    }
    catch (NotFoundException e){
      fail();
    }
  }
}
