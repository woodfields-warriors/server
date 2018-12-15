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
import java.io.PipedOutputStream;
import java.io.PipedInputStream;

public class GameDAOSQL extends GameDAO {

  Connection conn;

  public GameDAOSQL(Connection conn) {
    this.conn = conn;
  }

  @Override
  public DatabaseFacade loadFromPersistence() {
    try {
      Statement statement = conn.createStatement();
      ResultSet result = statement.executeQuery("SELECT data FROM games where gameId = '1'");
      if (!result.next()) {
        return new DatabaseFacade();
      }
      ObjectInputStream objectInputStream = new ObjectInputStream(result.getBinaryStream("data"));
      DatabaseFacade toReturn = (DatabaseFacade) objectInputStream.readObject();
      result.close();
      statement.close();
      return toReturn;
    }
    catch (SQLException e){
      e.printStackTrace();
      //throw new IllegalArgumentException("SQL read exception");
      return new DatabaseFacade();
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
      return new DatabaseFacade();
    }

  }

  @Override
  public void saveToPersistence(DatabaseFacade facade) {
    
    try {

      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      ObjectOutputStream outputStream = new ObjectOutputStream(buf);
      outputStream.writeObject(facade);
      outputStream.close();

      InputStream in = new ByteArrayInputStream(buf.toByteArray());

      PreparedStatement statement = conn.prepareStatement("INSERT INTO games (gameId,data) VALUES(?,?) ON CONFLICT (gameId) DO UPDATE SET data = EXCLUDED.data");
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
