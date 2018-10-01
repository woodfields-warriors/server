package com.wwttr.game;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameServiceTest {
    @Test public void testHasGame() {
        GameService service = new GameService();
        assertNotNull("service should have a game", service.getGame());
    }
}
