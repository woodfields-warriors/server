package com.wwttr.dao;

import com.wttr.database.IDAOFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DAOFactoryRelational implements IDAOFactory{

  private String connectionString;
  Connection con;


  public DAOFactoryRelational(){
    //grab the connection string from the environment variable
    this.connectionString = System.getenv("ConnectionString");
    con = DriverManager.getConnection(connectionString);
  }



  public DAO makeDAO(String type){
    switch(type){
      case "GameDAO":
        DAO toReturn = new GameDAOSQL(connectionString);
        PreparedStatement statement = con.prepareStatement(" CREATE TABLE IF NOT EXISTS games {
                                                             gameId TEXT NOT NULL PRIMARY KEY,
                                                             data BYTEA NOT NULL
                                                             };");
        statement.executeUpdate(tableCreateStatement);
        statement.close();
        return toReturn;
        break;
        //create a relational gameDao
      case "DeltaDAO":
        DAO toReturn = new DeltaDAOSQL(connectionString);
        PreparedStatement statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS deltas {
                                                            id SERIAL PRIMARY KEY,
                                                            command TEXT NOT NULL
                                                            };");
        statement.executeUpdate(tableCreateStatement);
        statement.close();
        return toReturn;
        break;

      case "UserDAO":
        DAO toReturn = new UserDAOSQL(connectionString);
        PreparedStatement statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS users {
                                                            userId TEXT NOT NULL PRIMARY KEY,
                                                            data BYTEA NOT NULL
                                                            };");
        statement.executeUpdate(tableCreateStatement);
        statement.close();
        return toReturn;
        break;

      default:
        throw new IllegalArgumentException("Only three types of daos: GameDAO, DeltaDAO, and UserDao");
    }
  }

}
