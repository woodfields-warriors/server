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
  public List<Delta> loadFromPersistance() {
    try {
      Connection con = DriverManager.getConnection(connectionString);
      Statement statement = con.createStatement();
      ResultSet result = statement.executeQuery("SELECT data FROM deltas where id = 1");
      ObjectInputStream objectInputStream = new ObjectInputStream(result.getBlob("data").getBinaryStream());
      List<Delta> toReturn = (List<Delta>) objectInputStream.readObject();
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
  public void clear() {
    try {
      Connection con = DriverManager.getConnection(connectionString);

      PreparedStatement statement = con.prepareStatement("UPDATE delta SET data = ? where id = 1");
      statement.setObject(1,null);
      statement.executeUpdate();
      statement.close();
      con.close();
    }
    catch (SQLException e){
      throw new IllegalArgumentException("SQL write exception");
    }
  }


  @Override
  public void addCommandForGame(Delta d) {
    try {
      Connection con = DriverManager.getConnection(connectionString);

      List<Delta> queue = loadFromPersistance();
      queue.add(d);
      Collections.sort(queue, new CustomComparator());

      PreparedStatement statement = con.prepareStatement("UPDATE delta SET data = ? where id = 1");
      statement.setObject(1,queue);
      statement.executeUpdate();
      statement.close();
      con.close();

      
    }
    catch (SQLException e){
      throw new IllegalArgumentException("SQL read exception");
    }
  }
}
