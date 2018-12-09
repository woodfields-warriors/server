package com.wwttr.dao;

import com.wwttr.database.DatabaseFacade;

public abstract class GameDAO implements DAO {

  public final String connectionString;

  //I just included this so I didn't have to define any constructors in the child classes
  public GameDAO(){throw new IllegalArgumentException("connectionstring must be given");}

  public GameDAO(String connectionString) {
    this.connectionString = connectionString;
  }

  @Override
  public final void save(DatabaseFacade facade){
    saveToPersistance(facade);
  }

  @Override
  public final void load(DatabaseFacade facade){
    DatabaseFacade persistantFacade = loadFromPersistance();
    facade.setGames(persistantFacade.getGames());
    facade.setGameActions(persistantFacade.getGameActions());
    facade.setGameStream(persistantFacade.getGameStream());
    facade.setRoutes(persistantFacade.getRoutes());
    facade.setRouteQueue(persistantFacade.getRouteQueue());
    facade.setTrainCards(persistantFacade.getTrainCards());
    facade.setTrainCardQueue(persistantFacade.getTrainCardQueue());
    facade.setDestinationCards(persistantFacade.getDestinationCards());
    facade.setDestinationCardQueue(persistantFacade.getDestinationCardQueue());
    facade.setDeckStatsCommandQueue(persistantFacade.getDeckStatsCommandQueue());
    facade.setHistoryQueue(persistantFacade.getHistoryQueue());
    facade.setMessages(persistantFacade.getMessages());
    facade.setMessageQueue(persistantFacade.getMessageQueue());
    facade.setPlayers(persistantFacade.getPlayers());
    facade.setPlayerStatsQueue(persistantFacade.getPlayerStatsQueue());

  }

  public abstract DatabaseFacade loadFromPersistance();
  public abstract void saveToPersistance(DatabaseFacade facade);
}
