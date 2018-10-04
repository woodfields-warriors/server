package com.wwttr.auth;

import org.junit.Test;
import static org.junit.Assert.*;

public class AuthServiceTest {
    private AuthService as = AuthService.getInstance();

    @Test
    public void invalidLogin() {
        try {
            Api.LoginResponse response = as.login("username", "password");
            assertNotNull(response.getUserId());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void validRegister() {
        try {
            Api.LoginResponse response = as.register("username", "password");
            assertNotNull(response.getUserId());
        }
        catch (Exception e){
            fail(e.getMessage());
        }

    }

    @Test
    public void invalidRegister() {
        try {
            Api.LoginResponse response = as.register("username", "password");
            fail();
        }
        catch (Exception e){
            assertTrue(e.getMessage(), true);
        }
    }

    @Test
    public void validLogin() {
        try {
            Api.LoginResponse response = as.login("username", "password");
            assertNotNull(response.getUserId());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

}
