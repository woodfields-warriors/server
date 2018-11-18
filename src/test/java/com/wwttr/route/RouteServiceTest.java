package com.wwttr.route;

import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.Route;
import com.wwttr.api.NotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import com.wwttr.route.RouteService;

import static org.junit.Assert.*;

public class RouteServiceTest {
  DatabaseFacade df = DatabaseFacade.getInstance();
  RouteService service = RouteService.getInstance();

  @Test
  public void StreamRoutes() {
    service.initRoutes("testgame1");
    service.initRoutes("testgame2");

    assertEquals(service.streamRoutes("testgame1").limit(98).count(), 98);
    // assertEquals(service.streamRoutes("testgame1").skip(98).count(), 0);
    // service.streamRoutes("testgame1").forEach((Route r) -> {
    //   System.out.println("GOT GAME");
    //   System.out.println(r.getGameId());
    //   System.out.println(r.getRouteId());
    //   throw new Error();
    // });
  }

}
