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

public class DeltaDAOSQL extends DeltaDAO {
  @Override
  public List<T> loadFromPersistance() {
    try {
      Connection con = DriverManager.getConnection(connectionString);
      Statement statement = con.createStatement();
      ResultSet result = statement.executeQuery("SELECT data FROM games where id = 1");
      ObjectInputStream objectInputStream = new ObjectInputStream(result.getBlob("data").getBinaryStream());
      DatabaseFacade toReturn = (DatabaseFacade) objectInputStream.readObject();
      result.close();
      statement.close();
      con.close();
      return toReturn;
    }
    catch (SQLException e){
      throw new IllegalArgumentException("SQL read exception");
    }
    catch (IOException e){
      throw new IllegalArgumentException("Object Conversion Error");
    }
    catch (ClassNotFoundException e){
      throw new IllegalArgumentException("Object Conversion to Database Error");
    }

  }

  @Override
  public void saveToPersistance(DatabaseFacade facade) {
    try {
      Connection con = DriverManager.getConnection(connectionString);
      PreparedStatement statement = con.prepareStatement("UPDATE games SET data = ? where id = 1");
      statement.setObject(1,facade);
      statement.executeUpdate();
      statement.close();
      con.close();
    }
    catch (SQLException e){
      throw new IllegalArgumentException("SQL read exception");
    }
  }
}
