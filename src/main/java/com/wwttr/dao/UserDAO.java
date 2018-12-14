package com.wwttr.dao;

import com.wwttr.database.DAO;
import com.wwttr.database.DatabaseFacade;

public abstract class UserDAO implements DAO {

  protected final String connectionString;

  //I just included this so I didn't have to define any constructors in the child classes
  UserDAO(){throw new IllegalArgumentException("connectionstring must be given");}

  public UserDAO(String connectionString) {
    this.connectionString = connectionString;
  }

  @Override
  public final void save(DatabaseFacade facade){
    saveToPersistence(facade);
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
