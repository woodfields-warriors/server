package com.wwttr.dao;

import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.Delta;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;


public class DeltaDAOSQL extends DeltaDAO {
  @Override
  public List<Message> loadFromPersistance() {
    try {
      Connection con = DriverManager.getConnection(connectionString);
      Statement statement = con.createStatement();
      ResultSet result = statement.executeQuery("SELECT data FROM deltas where id = 1");
      ObjectInputStream objectInputStream = new ObjectInputStream(result.getBlob("data").getBinaryStream());
      List<Message> toReturn = (List<Message>) objectInputStream.readObject();
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
  public void addCommandForGame(Delta d) {
    try {
      Connection con = DriverManager.getConnection(connectionString);

      GameDAO gameDAO = GameDAO();
      //DatabaseFacade persistantFacade = gameDAO.loadFromPersistance();
      DatabaseFacade df = DatabaseFacade.getInstance();
  
      Message request = d.getRequest();
      String id = d.getId();
      String gameId = d.getGameId();
  
      int storageInterval = df.getCommandStorageInterval();

      DeltaDAO deltaDAO = DeltaDAO(this.connectionString + "/" + gameId);
      List<Delta> queue = deltaDAO.loadFromPersistance();
      queue.add(d);
      Collections.sort(queue, new CustomComparator());

      if (queue.size() == storageInterval) {
        deltaDAO.clear();
        /*for (Delta delt : queue) {
            Message msg = delt.getRequest();
            df.execute(msg)
          } */

        gameDAO.save(df);
      }
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
