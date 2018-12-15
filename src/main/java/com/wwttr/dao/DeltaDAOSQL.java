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
//import java.util.java.util.ArrayList;
import com.wwttr.models.Delta;
import java.io.PipedOutputStream;
import java.io.PipedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DeltaDAOSQL extends DeltaDAO {

  Connection conn;

  public DeltaDAOSQL(Connection conn) {
    this.conn = conn;
  }

  @Override
  public java.util.TreeMap<String, Delta> loadFromPersistance() {
    try {
      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery("SELECT data FROM deltas");
      if (!result.next()) {
        return new java.util.TreeMap<String,Delta>();
      }
      ObjectInputStream objectInputStream = new ObjectInputStream(result.getBinaryStream("data"));
      java.util.TreeMap<String, Delta> toReturn = (java.util.TreeMap<String, Delta>) objectInputStream.readObject();
      System.out.println("DeltaDAO loaded - read map of size " + Integer.toString(toReturn.size()));
      result.close();
      statement.close();
      return toReturn;
    }
    catch (SQLException e) {
      e.printStackTrace();
      return new java.util.TreeMap<String,Delta>();
    }
    catch (IOException e){
      e.printStackTrace();
      throw new IllegalArgumentException("Object Conversion Error");
    }
    catch (ClassNotFoundException e){
      e.printStackTrace();
      throw new IllegalArgumentException("Object Conversion to Database Error");
    }
    catch (Exception e) {
      e.printStackTrace();
      return new java.util.TreeMap<String, Delta>();
      // throw new IllegalArgumentException("SQL read exception");
    }

  }

  @Override
  public void clear() {
    try {
      java.util.TreeMap<String, Delta> queue = new java.util.TreeMap<String,Delta>();

      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      ObjectOutputStream outputStream = new ObjectOutputStream(buf);
      outputStream.writeObject(queue);
      outputStream.close();

      InputStream in = new ByteArrayInputStream(buf.toByteArray());

      PreparedStatement statement = conn.prepareStatement("INSERT INTO deltas (id,data) VALUES(?,?) ON CONFLICT (id) DO UPDATE SET data = EXCLUDED.data");
      statement.setString(1,"1");
      statement.setBinaryStream(2, in);
      statement.executeUpdate();
      statement.close();
    }
    catch (SQLException e){
      e.printStackTrace();
      throw new IllegalArgumentException("SQL read exception");      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }


  @Override
  public void addCommandForGame(Delta d) {
    try {
      java.util.TreeMap<String, Delta> queue = loadFromPersistance();

      queue.put(d.getId(), d);
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      ObjectOutputStream outputStream = new ObjectOutputStream(buf);
      outputStream.writeObject(queue);
      outputStream.close();

      InputStream in = new ByteArrayInputStream(buf.toByteArray());

      PreparedStatement statement = conn.prepareStatement("INSERT INTO deltas (id,data) VALUES(?,?) ON CONFLICT (id) DO UPDATE SET data = EXCLUDED.data");
      statement.setString(1,"1");
      statement.setBinaryStream(2, in);
      statement.executeUpdate();
      statement.close();
    }
    catch (SQLException e){
      e.printStackTrace();
      throw new IllegalArgumentException("SQL read exception");      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
