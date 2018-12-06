package com.wwttr.dao;

import android.provider.ContactsContract;

import com.wwttr.database.DatabaseFacade;

public interface DAO {

  /*  facade:instance of DatabaseFacade to be serialized and saved to persistance */
  public void save(DatabaseFacade facade);
  /*  facade:instance of DatabaseFacade to have data loaded into */
  public void load(DatabaseFacade facade);
}
