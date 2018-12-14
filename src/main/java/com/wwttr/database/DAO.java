package com.wwttr.database;

import com.wwttr.database.DatabaseFacade;

public interface DAO {

  /*  data:either instance of DatabaseFacade or a Delta to be serialized and saved to persistance */
  public void save(Object data);
  /*  facade:instance of DatabaseFacade to have data loaded into */
  public void load(DatabaseFacade facade);

  public void clear();
}
