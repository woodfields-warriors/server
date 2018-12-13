package com.wwttr.database;

//

public interface  IDAOFactory {
  //type refers to deltaDao or GameDao or UserDao
  //not to RelationalDAO or NonRelationalDAo
  public DAO makeDAO(String type);

}
