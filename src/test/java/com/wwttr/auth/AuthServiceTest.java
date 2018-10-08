package com.wwttr.auth;

import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.LoginResponse;
import com.wwttr.models.User;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AuthServiceTest {
    private AuthService as = AuthService.getInstance();

    @Before
    public void setUp() throws Exception {
        DatabaseFacade.getInstance().clearUsers();
    }

    @Test
    public void invalidLogin() {
        try {
            LoginResponse response = as.login("username", "password");
            assertNotNull(response.getUserID());
        }
        catch (Exception e){
            assertNotNull(e);
        }
    }

    @Test
    public void validRegister() {
        try {
            LoginResponse response = as.register("username", "password");
            assertNotNull(response.getUserID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }

    }

    @Test
    public void invalidRegister() {
        try {
            LoginResponse response = as.register("username", "password");
            response = as.register("username", "password");
            fail();
        }
        catch (Exception e){
            assertNotNull(e);
        }
    }

    @Test
    public void validLogin() {
        try {
            LoginResponse response = as.register("username", "password");
            response = as.login("username", "password");
            assertNotNull(response.getUserID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void getUserByID(){
        LoginResponse response = as.register("username2", "password2");
        String username = as.getUsername(response.getUserID());
        assertEquals(username,"username2");
    }

}
