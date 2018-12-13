package com.wwttr.dao;

import com.wwttr.database.CommandQueue;
import com.google.protobuf.Message;

public abstract class DeltaDAO implements DAO {

  public final String connectionString;
  public final int storageInterval;

  public DeltaDAO(){throw new IllegalArgumentException("connectionstring must be given");}

  public DeltaDAO(String connectionString, int storageInterval) {
    this.connectionString = connectionString;
    this.storageInterval = storageInterval;
  }

  @Override
  public final void save(Object data){
    saveToPersistance(facade);
  }

  @Override
  public final void load(CommandQueue queue){
    CommandQueue persistantQueue = loadFromPersistance();
    
  }

  public abstract void addCommandForGame(Message request, String id, Game game);
  public abstract void clear();
  public abstract List<Object> loadFromPersistance();
  public abstract void saveToPersistance(List<Object> queue);
}
