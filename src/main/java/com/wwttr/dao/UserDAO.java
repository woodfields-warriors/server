package com.wwttr.dao;

import com.wwttr.database.DAO;
import com.wwttr.database.DatabaseFacade;

public abstract class UserDAO implements DAO {

  @Override
  public final void save(Object facade){
    saveToPersistence((DatabaseFacade)facade);
  }

  public void clear(){
    return;
  }

  @Override
  public final void load(DatabaseFacade facade){
    DatabaseFacade persistentFacade = loadFromPersistence();
    facade.setUsers(persistentFacade.getUsers());
  }

  public abstract DatabaseFacade loadFromPersistence();
  public abstract void saveToPersistence(DatabaseFacade facade);
}
