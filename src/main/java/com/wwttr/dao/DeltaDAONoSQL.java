package com.wwttr.dao;

import android.provider.ContactsContract;

import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;

public class DeltaDAONoSQL extends DeltaDAO {

  @Override
  public List<Object> loadFromPersistance() {
    try {
      FileInputStream fileInputStream = new FileInputStream(connectionString);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      return (List<Request>) objectInputStream.readObject();
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
  public void saveToPersistance(List<Object> queue) {
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
  public void addCommandForGame(Message request, String id, Game game) {
    String gameId = game.getGameID();

    /* assuming the file names will be the gameids - create
     deltadao with gameid as connectionstring */
    DeltaDAO dd = DeltaDAO(gameId);
    List<Request> queue = dd.loadFromPersistance();
    queue.add(req);

    if (queue.size() == this.storageInterval) {
      dd.clear();
      // TODO generate real gamedao
      GameDAO gd = GameDAO();
      
    }
  }
}
