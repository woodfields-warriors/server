package com.wwttr.dao;

import com.wwttr.database.DatabaseFacade;

public abstract class GameDAO implements DAO {

  String connectionString;

  public GameDAO(String connectionString) {
    this.connectionString = connectionString;
  }

  @Override
  public void save(DatabaseFacade facade){

  }

  @Override
  public void load(DatabaseFacade facade){

  }

  public abstract void writeToPersistance();
}
