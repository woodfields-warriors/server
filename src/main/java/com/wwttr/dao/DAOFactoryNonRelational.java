package com.wwttr.dao;

import com.wwttr.database.IDAOFactory;
import com.wwttr.database.DAO;

public class DAOFactoryNonRelational implements IDAOFactory{

  private final String connectionString = "/wwttrdata";


  public DAO makeDAO(String type){
    switch(type){
      case "GameDAO": {
        DAO toReturn = new GameDAONoSQL(connectionString + "games");
        return toReturn;
        //break;
      }

      case "DeltaDAO": {
        DAO toReturn = new DeltaDAONoSQL(connectionString);
        return toReturn;
        //break;
      }

      case "UserDAO": {
        DAO toReturn = new UserDAONoSQL(connectionString + "users");
        return toReturn;
      //  break;
      }
      default:
        throw new IllegalArgumentException("Only three types of daos: GameDAO, DeltaDAO, and UserDao");
    }
  }

}
