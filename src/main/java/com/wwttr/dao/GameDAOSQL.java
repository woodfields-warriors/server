package com.wwttr.dao;

import com.wwttr.database.DatabaseFacade;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GameDAOSQL extends GameDAO {

  public GameDAOSQL(String connectionString) {
    super(connectionString);
  }

  @Override
  public DatabaseFacade loadFromPersistence() {
    try {
      Connection con = DriverManager.getConnection(connectionString);
      Statement statement = con.createStatement();
      ResultSet result = statement.executeQuery("SELECT data FROM games where gameId = 1");
      ObjectInputStream objectInputStream = new ObjectInputStream(result.getBlob("data").getBinaryStream());
      DatabaseFacade toReturn = (DatabaseFacade) objectInputStream.readObject();
      result.close();
      statement.close();
      con.close();
      return toReturn;
    }
    catch (SQLException e){
      e.printStackTrace();
      throw new IllegalArgumentException("SQL read exception");
    }
    catch (IOException e){
      e.printStackTrace();
      throw new IllegalArgumentException("Object Conversion Error");
    }
    catch (ClassNotFoundException e){
      e.printStackTrace();
      throw new IllegalArgumentException("Object Conversion to Database Error");
    }

  }

  @Override
  public void saveToPersistence(DatabaseFacade facade) {
    try {
      Connection con = DriverManager.getConnection(connectionString);
      PreparedStatement statement = con.prepareStatement("INSERT INTO games (gameId,data) VALUES(?,?) ON DUPLICATE KEY UPDATE games SET data = ? where gameId = 1");
      statement.setString(1,"1");
      statement.setObject(2,facade);
      statement.setObject(3,facade);
      statement.executeUpdate();
      statement.close();
      con.close();
    }
    catch (SQLException e){
      e.printStackTrace();
      throw new IllegalArgumentException("SQL read exception");
    }
  }
}
