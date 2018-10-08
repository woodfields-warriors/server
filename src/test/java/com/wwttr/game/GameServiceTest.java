package com.wwttr.game;

import com.wwttr.auth.AuthService;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.CreateResponse;
import com.wwttr.models.Game;
import com.wwttr.models.LoginResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
                System.out.println("temp ID = " +tempID);
                System.out.println("playerID = " + playerID);
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

}
