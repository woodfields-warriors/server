package com.wwttr.dao;

import com.wttr.database.IDAOFactory;

public class DAOFactoryNonRelational implements IDAOFactory{

  private final String connectionString = "";


  public DAO makeDAO(String type){
    switch(type){
      case "GameDAO":
        DAO toReturn = new GameDAOSQL(connectionString+"games");
        return toReturn;
        break;
        //create a relational gameDao
      case "DeltaDAO":
        DAO toReturn = new DeltaDAOSQL(connectionString);
        return toReturn;
        break;

      case "UserDAO":
        DAO toReturn = new UserDAOSQL(connectionString+"users");
        return toReturn;
        break;

      default:
        throw new IllegalArgumentException("Only three types of daos: GameDAO, DeltaDAO, and UserDao");
    }
  }
  }

}
