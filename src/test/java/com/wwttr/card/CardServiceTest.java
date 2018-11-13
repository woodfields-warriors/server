package com.wwttr.card;

import com.wwttr.api.NotFoundException;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.game.GameService;
import com.wwttr.models.DestinationCard;
import com.wwttr.models.CreateResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CardServiceTest {
  CardService cs = CardService.getInstance();
  DatabaseFacade df = DatabaseFacade.getInstance();
  private String[] name = {"test", "test2"};
  private Integer[] points = {1, 2};
  GameService gs = GameService.getInstance();
  String GameId = null;
  String playerId = null;

  @Before
  public void setUp() throws Exception {
    cs.setDefaultTemplates();
    CreateResponse cr = gs.createGame("gameName", "user", 4);
    GameId = cr.getGameID();
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
    try {
      cs.generateDestinationDeckTemplate(name, name, points);
      cs.createFullDecksForGame(GameId);
      assert (df.getDestinationCards().size() == name.length);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void getDestinationCard() {
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
    try {
      cs.createFullDecksForGame(GameId);
      List<DestinationCard> cards = cs.peekDestinationCards(GameId);
      assertNotNull(cards);
    } catch (NotFoundException e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void claimDesinationCards() {
    try {
      cs.createFullDecksForGame(GameId);
      List<DestinationCard> cards = cs.peekDestinationCards(GameId);
      List<String> idsToClaim = new ArrayList<>();
      for (DestinationCard card : cards) {
        System.out.println(card.getId());
        idsToClaim.add(card.getId());
      }
      cs.claimDesinationCards(idsToClaim, playerId);
      for (int i = 0; i < cards.size(); i++) {
        assert (cs.getDestinationCard(cards.get(i).getId()).getPlayerId().equals(playerId));
      }
    } catch (NotFoundException e) {
      e.printStackTrace();
      fail();
    }
  }
}
