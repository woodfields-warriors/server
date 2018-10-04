package com.wwttr.database;

import com.wwttr.models.User;

import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseFacadeTest {

    private DatabaseFacade df = DatabaseFacade.getInstance();

    @Test
    public void makeUser() {
        try{
            User response = df.makeUser("username", "password");
            assertNotNull(response.getUserID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void getUser() {
        try{
            User response = df.getUser("username");
            assertNotNull(response.getUserID());
        }
        catch (Exception e){
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

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
}