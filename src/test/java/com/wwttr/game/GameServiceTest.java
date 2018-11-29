package com.wwttr.game;

import com.wwttr.auth.AuthService;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.Player;
import com.wwttr.models.GameAction;
import com.wwttr.models.LoginResponse;
import com.wwttr.api.NotFoundException;
import com.wwttr.models.Route;
import com.wwttr.models.TrainCard;
import com.wwttr.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameServiceTest {
    DatabaseFacade df = DatabaseFacade.getInstance();
    GameService service = GameService.getInstance();
    String username = "username";
    String password = "password";
    String userID;

    @Before
    public void setUp() throws Exception {
        AuthService authService = AuthService.getInstance();
        LoginResponse login = authService.register(username,password);
        userID = login.getUserID();
    }

    @After
    public void tearDown() throws Exception {
        df.clearUsers();
        df.clearGames();
        df.clearPlayers();
    }

    @Test
    public void getInstance() {
        try{
            service = service.getInstance();
            assertNotNull(service);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void CreateGame() {
        try {
            CreateResponse response = service.createGame("valid", userID, 2);
            assertNotNull(response.getGameID());
            assertNotNull(response.getPlayerID());
            Game game = service.getGame(response.getGameID());
            assert(game.getPlayerIDs().size() > 0);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void listGames() {
        try{
            CreateResponse response = service.createGame("valid", userID, 2);
            List<Game> gameList = service.listGames();
            assertNotNull(gameList);
            if(gameList.size() != 1){
                fail();
            }
        }
        catch (Exception e){
            fail();
        }
    }

    // @Test
    // public void streamGames() throws Exception {
    //     GameService service = new GameService(new DatabaseFacade());
    //     service.

    //     CreateResponse response = service.createGame("stream-game", userID, 2);
    //     service.streamGames().limit(1).forEach((Game g) -> {
    //         assertEquals(response.getGameID(), g.getGameID());
    //     });
    // }

    @Test
    public void getGame() {
        try{
            CreateResponse response = service.createGame("valid", userID, 2);
            Game game = service.getGame(response.getGameID());
            assertNotNull(game);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void getInvalidGame(){
      try{
        Game game = service.getGame("invalidGameID");
        assertNull(game);
      }
      catch(Exception e){
        fail();
      }
    }

    @Test
    public void leaveGame() {
        try{
            CreateResponse response = service.createGame("valid", userID, 2);
            service.leaveGame(response.getPlayerID(),response.getGameID());
            Game game = service.getGame(response.getGameID());
            assertNull(game);
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    public void startGame() {
        try{
            CreateResponse response = service.createGame("valid", userID, 2);
            service.createPlayer(userID,response.getGameID());
            Game game = service.startGame(response.getGameID());
            assert(game.getGameStatus() == Game.Status.STARTED);
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void deleteGame() {
        try{
            CreateResponse response = service.createGame("valid", userID, 2);
            service.deleteGame(response.getGameID());
            Game game = service.getGame(response.getGameID());
            assertNull(game);
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void createPlayer() {
        try{
            CreateResponse response = service.createGame("valid", userID, 2);
            String playerID = service.createPlayer(userID,response.getGameID());
            Game game = service.getGame(response.getGameID());
            boolean pass = false;
            for(String tempID : game.getPlayerIDs()){
                // System.out.println("temp ID = " +tempID);
                // System.out.println("playerID = " + playerID);
                if(playerID.equals(tempID)){
                    pass = true;
                    break;
                }
            }
            assert(pass);
        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }


    // tries to get a game that doesn't exist
    @Test
    public void invalidCreatePlayer(){
      try{
        String response = service.createPlayer(userID, "invalidGameID");
      }
      catch(NotFoundException e){
        assert(true);
      }
      catch(GameFullException e){
        fail();
      }
    }


    @Test
    public void getPlayer() {
      try{
        CreateResponse game = service.createGame("valid", userID, 2);
        String playerID = service.createPlayer(userID, game.getGameID());
        Player player = service.getPlayer(playerID);
        assertNotNull(player);
        assertEquals(playerID, player.getPlayerId());
        assertEquals(game.getGameID(), player.getGameId());
        assertEquals(userID, player.getUserId());
      }
      catch (Exception e){
        fail();
      }
    }


    @Test
    public void testGameAction(){
      try{
        CreateResponse game = service.createGame("valid", userID, 2);
        String playerId = service.createPlayer(userID, game.getGameID());
        Player player = service.getPlayer(playerId);
        GameAction action = service.createGameAction("claimed a route",playerId);
        System.out.println(action.getActionId());
        assertEquals(action.getGameId(),game.getGameID());
        assertEquals(action.getPlayerId(),playerId);
      }
      catch (NotFoundException e){
        fail();
      }
      catch(GameFullException e){
        fail();
      }
    }

    @Test
    public void testEndGameState() {
      try{

        CreateResponse response = service.createGame("valid", userID, 2);
        service.createPlayer(userID,response.getGameID());
        df.clearDestinationCards();
        df.clearRoutes();
        df.clearTrainCards();/*
    ArrayList<TrainCard> cards = new ArrayList<>();
    for(int i = 0; i < 8; i++){
      cards.add(new TrainCard(Integer.toString(i),GameId,playerId, TrainCard.Color.RAINBOW, TrainCard.State.OWNED));
    }
    df.addTrainCardDeck(cards);*/
        df.addRoute(new Route("1","1","2", TrainCard.Color.GREY,8,response.getGameID(),response.getPlayerID()));
        df.updatePlayerStats(response.getPlayerID());
        Player player = service.getPlayer(response.getPlayerID());
        player.setState(new StartState());
        df.updatePlayer(player);
        ArrayList<TrainCard> cards = new ArrayList<>();
        ArrayList<String> cardIds = new ArrayList<>();
        for(int i = 0; i < 8; i++){
          cards.add(new TrainCard(Integer.toString(i),response.getGameID(),response.getPlayerID(), TrainCard.Color.RAINBOW, TrainCard.State.OWNED));
          cardIds.add(Integer.toString(i));
        }
        df.addTrainCardDeck(cards);
        df.addRoute(new Route("2","1","2", TrainCard.Color.GREY,8,response.getGameID(),""));

        player.getPlayerState().claimRoute(player.getPlayerId(),"2",cardIds);
        player = service.getPlayer(player.getPlayerId());
        assertEquals(GameEnded.class,player.getPlayerState().getClass());
      }
      catch (Exception e){
        e.printStackTrace();
        fail();
      }

    }

    @Test
    public void endGameGeneralState(){
      try{

      CreateResponse response = service.createGame("valid", userID, 2);
      df.makeUser(new User("player2","","id2"));
      String player2 = service.createPlayer("id2",response.getGameID());
      df.clearDestinationCards();
      df.clearRoutes();
      df.clearTrainCards();

      List<String> playerIDs = df.getGame(response.getGameID()).getPlayerIDs();
      for(String id : playerIDs) {
        if(!id.equals(response.getPlayerID())) {
          Player player = df.getPlayer(id);
          player.setState(new GameEnded());
        }
        else{
          Player player = df.getPlayer(id);
          player.setState(new StartState());
        }
      }
        df.clearDestinationCards();
        df.clearRoutes();
        df.clearTrainCards();
        df.addRoute(new Route("2","1","2", TrainCard.Color.GREY,8,response.getGameID(),""));
        df.updatePlayerStats(response.getPlayerID());
        Player player = service.getPlayer(response.getPlayerID());
        assertEquals(StartState.class,player.getPlayerState().getClass());
        Game game = service.getGame(response.getGameID());
        assertEquals(Game.Status.PRE,game.getGameStatus());

        ArrayList<TrainCard> cards = new ArrayList<>();
        ArrayList<String> cardIds = new ArrayList<>();
        for(int i = 0; i < 8; i++){
          cards.add(new TrainCard(Integer.toString(i),response.getGameID(),response.getPlayerID(), TrainCard.Color.RAINBOW, TrainCard.State.OWNED));
          cardIds.add(Integer.toString(i));
        }
        df.addTrainCardDeck(cards);
        System.out.println(player.getPlayerState());
        player.getPlayerState().claimRoute(player.getPlayerId(),"2",cardIds);
        player = service.getPlayer(player.getPlayerId());
        game = service.getGame(response.getGameID());
        assertEquals(GameEnded.class,player.getPlayerState().getClass());
        assertEquals(Game.Status.ENDED,game.getGameStatus());



    }
    catch (Exception e){
      e.printStackTrace();
      fail();
    }

    }
}

