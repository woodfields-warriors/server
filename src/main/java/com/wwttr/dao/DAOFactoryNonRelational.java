package com.wwttr.dao;

import com.wwttr.database.IDAOFactory;
import com.wwttr.database.DAO;

public class DAOFactoryNonRelational implements IDAOFactory{

  private final String connectionString = "";


  public DAO makeDAO(String type){
    switch(type){
      case "GameDAO": {
        DAO toReturn = new GameDAONoSQL(connectionString + "games");
        return toReturn;
        break;
      }
        //create a relational gameDao
      case "DeltaDAO": {
        DAO toReturn = new DeltaDAOSQL(connectionString);
        return toReturn;
        break;
      }

      case "UserDAO": {
        DAO toReturn = new UserDAONoSQL(connectionString + "users");
        return toReturn;
        break;
      }

      default:
        throw new IllegalArgumentException("Only three types of daos: GameDAO, DeltaDAO, and UserDao");
    }
  }

}
