package com.wwttr.auth;

import org.junit.Test;
import static org.junit.Assert.*;

public class AuthServiceTest {
    @Test public void testHasAccount() {
        AuthService service = new AuthService();
        assertNotNull("service should have an account", service.getAccount());
    }
}
