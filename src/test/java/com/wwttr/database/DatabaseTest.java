package com.wwttr.database;

import com.wwttr.models.User;

import java.util.stream.Stream;

import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseTest {

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

    @Test
    public void makeUser() {
        try{
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
  }

  @Test
  public void listDestinationCards() {
  }

  @Test
  public void updateDestinationCard() {
  }

  @Test
  public void addDestinationCardDeck() {
  }
}
