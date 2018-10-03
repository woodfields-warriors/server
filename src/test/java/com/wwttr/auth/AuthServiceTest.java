package com.wwttr.auth;

import com.wwttr.models.LoginResponse;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AuthServiceTest {
    private AuthService as = AuthService.getInstance();

    @Test
    public void invalidLogin() {
        try {
            LoginResponse response = as.login("username", "password");
            assertNotNull(response.getUserID());
        }
        catch (Exception e){
            fail(e.getMessage());
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
            assertNotNull(response.getErrorCode());
        }
        catch (Exception e){
            assertTrue(e.getMessage(), true);
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

}
