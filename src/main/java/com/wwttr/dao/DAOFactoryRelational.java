package com.wwttr.dao;

import com.wwttr.database.DAO;
import com.wwttr.database.IDAOFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class DAOFactoryRelational implements IDAOFactory{

  Connection conn;


  public DAOFactoryRelational(){
    //grab the connection string from the environment variable
    // this.connectionString = System.getenv("ConnectionString");

    conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "myPassword");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(1);
    }
    System.out.println("Opened database successfully");
  }


  public DAO makeDAO(String type){
    try {
      // con = DriverManager.getConnection(connectionString);
      switch (type) {
        case "GameDAO": {
          DAO toReturn = new GameDAOSQL(conn);
          PreparedStatement statement = conn.prepareStatement(" CREATE TABLE IF NOT EXISTS games ( " +
              "gameId TEXT NOT NULL PRIMARY KEY, " +
              "data BYTEA NOT NULL );");
          statement.executeUpdate();
          statement.close();
          return toReturn;
        }
        //break;
        //create a relational gameDao
        case "DeltaDAO": {
          DAO toReturn = new DeltaDAOSQL(conn);//DeltaDAOSQL(connectionString);
          PreparedStatement statement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS deltas ( " +
              "id TEXT PRIMARY KEY, " +
              "data BYTEA NOT NULL );");
          statement.executeUpdate();
          statement.close();
          return toReturn;
        //  break;
        }

        case "UserDAO": {
          DAO toReturn = new UserDAOSQL(conn);
          PreparedStatement statement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users ( " +
              "userId TEXT NOT NULL PRIMARY KEY, " +
              "data BYTEA NOT NULL );");
          statement.executeUpdate();
          statement.close();
          return toReturn;
          //break;
        }

        default:
          throw new IllegalArgumentException("Only three types of daos: GameDAO, DeltaDAO, and UserDao");
      }
    }
    catch (Exception e){
      e.printStackTrace();
      return null;
    }
  }

}
