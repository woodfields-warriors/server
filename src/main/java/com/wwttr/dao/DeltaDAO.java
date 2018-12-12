package com.wwttr.dao;

import com.wwttr.database.CommandQueue;

public abstract class DeltaDAO implements DAO {

  public final String connectionString;
  public final int timesBetweenStorage = 10;

  public DeltaDAO(){throw new IllegalArgumentException("connectionstring must be given");}

  public DeltaDAO(String connectionString) {
    this.connectionString = connectionString;
  }

  @Override
  public final void save(DatabaseFacade facade){
    saveToPersistance(facade);
  }

  @Override
  public final void load(CommandQueue queue){
    CommandQueue persistantQueue = loadFromPersistance();
    
  }

  public abstract void addCommandForGame(Request req, Game game);
  public abstract void clear();
  public abstract List<T> loadFromPersistance();
  public abstract void saveToPersistance(List<T> queue);
}
