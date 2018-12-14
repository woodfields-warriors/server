package com.wwttr.dao;


import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.Game;
import com.wwttr.models.Delta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;


public class DeltaDAONoSQL extends DeltaDAO {

  public DeltaDAONoSQL(String connectionString) {
    super(connectionString);
  }

  @Override
  public List<Delta> loadFromPersistance() {
    try {
      FileInputStream fileInputStream = new FileInputStream(connectionString);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      return (List<Delta>) objectInputStream.readObject();
    }
    catch (FileNotFoundException e){
      throw new IllegalArgumentException("File not found");
    }
    catch (IOException e){
      throw new IllegalArgumentException("IOException");
    }
    catch (ClassNotFoundException e){
      throw new IllegalArgumentException("Class not Found");
    }

  }


  @Override
  public void clear() {
    try {
      PrintWriter pw = new PrintWriter(connectionString);
      pw.close();
    }
    catch (FileNotFoundException e){
      throw new IllegalArgumentException("File not found");
    }
    catch (IOException e){
      throw new IllegalArgumentException("IOException");
    }
  }

  @Override
  public void saveToPersistance(List<Object> queue) {

  }

  @Override
  public void addCommandForGame(Delta d) {
    // TODO generate real gamedao
    GameDAO gameDAO = GameDAO();
    //DatabaseFacade persistantFacade = gameDAO.loadFromPersistance();
    DatabaseFacade df = DatabaseFacade.getInstance();

    Message request = d.getRequest();
    String id = d.getId();
    String gameId = g.getGameId();

    int storageInterval = df.getCommandStorageInterval();

    /* assuming the file names will be the gameids and the connnectionString
        in the constructor is the directory  */
    DeltaDAO deltaDAO = DeltaDAO(this.connectionString + "/" + gameId);
    List<Delta> queue = deltaDAO.loadFromPersistance();
    queue.add(d);
    Collections.sort(queue, new CustomComparator());

    if (queue.size() == storageInterval) {
      deltaDAO.clear();
      /*for (Delta delt : queue) {
        Message msg = delt.getRequest();
        df.execute(msg)
      }
      }*/
      gameDAO.save(df);
    }
    else {
      try {
        FileOutputStream fileOutputStream = new FileOutputStream(connectionString, false );
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(queue);
        objectOutputStream.close();
        fileOutputStream.close();
      }
      catch (FileNotFoundException e){
        throw new IllegalArgumentException("File not found");
      }
      catch (IOException e){
        throw new IllegalArgumentException("IOException");
      }
    }
  }
}


