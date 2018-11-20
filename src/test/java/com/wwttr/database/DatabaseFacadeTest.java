package com.wwttr.database;

import com.wwttr.api.NotFoundException;
import com.wwttr.models.DestinationCard;
import com.wwttr.models.Game;
import com.wwttr.models.Player;
import com.wwttr.models.User;
import com.wwttr.models.TrainCard;
import com.wwttr.card.CardService;
import com.wwttr.models.CreateResponse;
import com.wwttr.auth.AuthService;
import com.wwttr.game.GameService;


import org.junit.After;
import org.junit.Before;
import java.util.stream.Stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseFacadeTest {

    GameService gameService = GameService.getInstance();
    CardService cs = CardService.getInstance();
    AuthService authService = AuthService.getInstance();
    String user;
    String GameId;
    String playerId;

    @Test
    public void pubSub() throws Exception {
        CommandQueue<String> q = new CommandQueue<String>();

        Stream<String> stream = q.subscribe().limit(3);


        q.publish("TEST");
        q.publish("TEST");
        q.publish("TEST");

        q.subscribe()
          .limit(3)
          .forEach((String s) -> {
            assertEquals("TEST", s);
          });

        stream.forEach((String s) -> {
          assertEquals("TEST", s);
        });
    }

    private DatabaseFacade df = DatabaseFacade.getInstance();
  private final String[] FIRST_CITIES = {"Los Angeles", "Duluth", "Sault Ste Marie", "New York", "Portland", "Vancouver", "Duluth", "Toronto", "Portland", "Dallas",
      "Calgary", "Calgary", "Los Angeles", "Winnipeg", "San Francisco", "Kansas City", "Los Angeles", "Denver", "Chicago", "Vancouver",
      "Boston", "Chicago", "Montreal", "Seattle", "Denver", "Helena", "Winnipeg", "Montreal", "Sault Ste Marie", "Seattle"};
  private final String[] SECOND_CITIES = {"New York City", "Houston", "Nashville", "Atlanta", "Nashville", "Montreal", "El Paso", "Miami", "Phoenix", "New York City",
      "Salt Lake City", "Phoenix", "Miami", "Little Rock", "Atlanta", "Houston", "Chicago", "Pittsburgh", "Santa Fe", "Santa Fe",
      "Miami", "New Orleans", "Atlanta", "New York", "El Paso", "Los Angeles", "Houston", "New Orleans", "Oklahoma City", "Los Angeles"};
  private final Integer[] POINTS = {21,8,8,6,17,20,10,10,11,11,7,13,20,11,17,5,16,11,9,13,12,7,9,22,4,8,12,13,9,9};

  @Before
  public void setUp() throws Exception {
    User testUser = new User("username", "password", "id");
    User response = df.makeUser(testUser);
    List<DestinationCard> cards = new ArrayList<>();
    for(int i = 0; i < FIRST_CITIES.length; i++){
      DestinationCard tempCard = new DestinationCard("temp" + i,FIRST_CITIES[i],SECOND_CITIES[i],POINTS[i],"","game");
      cards.add(tempCard);
    }
    df.addDestinationCardDeck(cards);

    authService.register("user","password");
    user = df.getUser("user").getUserID();
    CreateResponse cr = gameService.createGame("gameName", user, 4);
    GameId = cr.getGameID();
    gameService.createPlayer(user,GameId);
    playerId = cr.getPlayerID();
    cs.createFullDecksForGame(GameId);
  }

  @After
  public void tearDown() throws  Exception {
    df.clearPlayers();
    df.clearUsers();
    df.clearGames();
    df.clearCards();
  }

    @Test
    public void makeUser() {
        try{
          df.clearUsers();
            User testUser = new User("username", "password", "id");
            User response = df.makeUser(testUser);
            assertNotNull(response.getUserID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    // @Test
    // public void getUser() {
    //     try{
    //         User response = df.getUser("username");
    //         assertNotNull(response);
    //         assertNotNull(response.getUserID());
    //     }
    //     catch (Exception e){
    //         e.printStackTrace();
    //         fail(e.getMessage());
    //     }
    // }

    @Test
    public void getUserByID() {
        try{
            User response = df.getUser("username");
            response = df.getUserByID(response.getUserID());
            assertNotNull(response.getUserID());
        }
        catch (Exception e){
            fail(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

  @Test
  public void getDestinationCard() {
    try {
      DestinationCard returnedCard = df.getDestinationCard("temp1");
      assertNotNull(returnedCard);
    }
    catch (NotFoundException e){
      fail();
    }
  }

  @Test
  public void failGetDestinationCard() {
    try {
      DestinationCard returnedCard = df.getDestinationCard("1");
      fail();
    }
    catch (NotFoundException e){
      assertNotNull(e);
    }
  }

  @Test
  public void listDestinationCards() {
    try {
      List<DestinationCard> returnedList = df.listDestinationCards(3, "game");
      assertTrue(returnedList.size() == 3);
    }
    catch (NotFoundException e){
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void failListDestinationCards() {
    try {
      df.clearCards();
      List<DestinationCard> returnedList = df.listDestinationCards(3, "game");
      fail();
    }
    catch (NotFoundException e){
      e.printStackTrace();
      assertNotNull(e);
    }
    catch (IllegalArgumentException e){
      assertNotNull(e);
    }
  }

/*  @Test
  public void updateDestinationCard() {
    try {
      DestinationCard tempCard = new DestinationCard("temp1", null,null, null, null, null);
      df.updateDestinationCard(tempCard);
      tempCard = df.getDestinationCard(tempCard.getId());
      assertNull(tempCard.getFirstCityId());
    }
    catch (NotFoundException e){
      fail();
    }
  }*/

  @Test
  public void addDestinationCardDeck() {
    df.clearCards();
    List<DestinationCard> cards = new ArrayList<>();
    for(int i = 0; i < FIRST_CITIES.length; i++){
      DestinationCard tempCard = new DestinationCard("temp" + i,FIRST_CITIES[i],SECOND_CITIES[i],POINTS[i],"player","game");
      cards.add(tempCard);
    }
    df.addDestinationCardDeck(cards);
    assertTrue(df.getDestinationCards().size() == FIRST_CITIES.length);
  }

  @Test
  public void requestMoreCardsThanAvailable() {
    System.out.println("running request more cards than available");
    df.clearCards();
    List<DestinationCard> cards = new ArrayList<>();
    for(int i = 0; i < FIRST_CITIES.length; i++){
      DestinationCard tempCard = new DestinationCard("temp" + i,FIRST_CITIES[i],SECOND_CITIES[i],POINTS[i],"player","game");
      cards.add(tempCard);
    }
    df.addDestinationCardDeck(cards);
    try {
      List<DestinationCard> returnedList = df.listDestinationCards(FIRST_CITIES.length + 1, "game");
      fail();
    }
    catch (NotFoundException e){
      e.printStackTrace();
      fail();
    }
    catch (IllegalArgumentException e){
      assertNotNull(e);
    }
  }

  @Test
  public void getTrainCard() {
  }

  @Test
  public void updateTrainCard() {
  }

  @Test
  public void addTrainCardDeck() {
  }

  @Test
  public void newFaceUpCard() {

  }

  @Test
  public void newFaceUpCardWith3Locomotives(){
    ArrayList<TrainCard> trainCards = df.getTrainCards();
    ArrayList<TrainCard> cardsInGame = new ArrayList<>();
    for(TrainCard card : trainCards){
      if(card.getGameId().equals(GameId)){
        cardsInGame.add(card);
      }
    }
    //Route route = df.getRoutebyId("Los Angeles - El Paso");
    int visible = 0;
    for(TrainCard tc: cardsInGame){
      //Make four rainbow cards face up.  The rest in the deck
      if(tc.getColor().equals(TrainCard.Color.RAINBOW) && visible <4){
        visible++;
        tc.setState(TrainCard.State.VISIBLE);
      }
      else{
        tc.setState(TrainCard.State.HIDDEN);
      }
      try {
        df.updateTrainCard(tc);
      }
      catch (Exception e){
        fail();
        }
    }
    try{
      df.newFaceUpCard(GameId);
      int visibleRainbows = 0;
      visible = 0;
      for(TrainCard tc: trainCards){
        if(tc.getColor().equals(TrainCard.Color.RAINBOW) && tc.getState().equals(TrainCard.State.VISIBLE)){
          visible ++;
          visibleRainbows ++;
        }
        else if(tc.getState().equals(TrainCard.State.VISIBLE)) {
          visible ++ ;
        }
      }
      System.out.println("Number of visible cards after newFaceUpCard = " + visible);
      System.out.println("Number of visible RAINBOW cards after newFaceUpCard = "+ visibleRainbows);
      assertTrue(visible <6);
      assertTrue(visibleRainbows < 3);
    }
    catch(Exception e){
      fail();
    }
  }

  @Test
  public void sendNewHiddenCard() {
  }

  @Test
  public void getTrainCardsForGame() {
  }

  @Test
  public void getTrainCardsForPlayer() {
  }

  @Test
  public void getRandomTrainCardFromDeck() {
  }

  @Test
  public void streamTrainCards() {
  }

  @Test
  public void streamDeckStats() {
  }

  @Test
  public void clearTrainCards() {
  }

  @Test
  public void getTrainCards() {
  }

  @Test
  public void getNextPlayer() {
    df.makeUser(new User("1","2","1"));
    df.addPlayer(new Player("playerid","1","gameid",Player.Color.BLUE,"1"));
    ArrayList<String> playerIdList = new ArrayList<>();
    playerIdList.add("playerid");
    playerIdList.add("playerid2");
    df.addGame(new Game("playerid",playerIdList,"1",2,"gameid" ));
    df.addPlayer(new Player("playerid2","1","gameid",Player.Color.GREEN,"1"));
    Player player = df.getNextPlayer("playerid","gameid");
    assert(player.getPlayerId().equals("playerid2"));

    player = df.getNextPlayer("playerid2","gameid");
    assert(player.getPlayerId().equals("playerid"));


  }
}
