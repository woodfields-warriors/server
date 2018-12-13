package com.wwttr.dao;

import android.provider.ContactsContract;

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
import java.util.Collections;

public class CustomComparator {
  public boolean compare(Object obj1, Object ob2) {
    Delta d1 = (Delta) obj1;
    Delta d2 = (Delta) d2;
    return d1.getId() < d2.getId();
  }
}

public class DeltaDAONoSQL extends DeltaDAO {

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
      /*for (Map.Entry<String,Message> : queue.entrySet()) {
        String id = entry.getKey();
        Message msg = entry.getValue();
        execute(msg);
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
