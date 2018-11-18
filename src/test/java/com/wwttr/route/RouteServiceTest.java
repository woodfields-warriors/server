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
    assertEquals(service.streamRoutes("testgame1").limit(98).count(), 98);
  }

}
