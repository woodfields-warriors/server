package com.wwttr.dao;

import com.wwttr.database.DAO;
import com.wwttr.database.DatabaseFacade;

public abstract class GameDAO implements DAO {

  @Override
  public final void save(Object facade){
    saveToPersistence((DatabaseFacade)facade);
  }

  @Override
  public final void load(DatabaseFacade facade){
    DatabaseFacade persistentFacade = loadFromPersistence();
    facade.setGames(persistentFacade.getGames());
    facade.setGameActions(persistentFacade.getGameActions());
    facade.setGameStream(persistentFacade.getGameStream());
    facade.setRoutes(persistentFacade.getRoutes());
    facade.setRouteQueue(persistentFacade.getRouteQueue());
    facade.setTrainCards(persistentFacade.getTrainCards());
    facade.setTrainCardQueue(persistentFacade.getTrainCardQueue());
    facade.setDestinationCards(persistentFacade.getDestinationCards());
    facade.setDestinationCardQueue(persistentFacade.getDestinationCardQueue());
    facade.setDeckStatsCommandQueue(persistentFacade.getDeckStatsCommandQueue());
    facade.setHistoryQueue(persistentFacade.getHistoryQueue());
    facade.setMessages(persistentFacade.getMessages());
    facade.setMessageQueue(persistentFacade.getMessageQueue());
    facade.setPlayers(persistentFacade.getPlayers());
    facade.setPlayerStatsQueue(persistentFacade.getPlayerStatsQueue());

  }

  public void clear(){
    return;
  }

  public abstract DatabaseFacade loadFromPersistence();
  public abstract void saveToPersistence(DatabaseFacade facade);
}
