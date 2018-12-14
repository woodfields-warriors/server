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
      File file = new File(connectionString);

      try {
        file.createNewFile();
        return null;
      }
      catch (IOException f){
        f.printStackTrace();
        throw new IllegalArgumentException("file unable to be created");
      }
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
      java.io.PrintWriter pw = new java.io.PrintWriter(connectionString);
      pw.close();
    }
    catch (FileNotFoundException e){
      throw new IllegalArgumentException("File not found");
    }
  }

  @Override
  public void addCommandForGame(Delta d) {

    /* assuming the file names will be the gameids and the connnectionString
        in the constructor is the directory  */
    File directory = new File(connectionString);
    if (!directory.exists()) {
      directory.mkdir();
    }
    DeltaDAONoSQL deltaDAO = new DeltaDAONoSQL(this.connectionString + "/" + d.getGameId() + ".txt");
    List<Delta> queue = deltaDAO.loadFromPersistance();
    queue.add(d);
    Collections.sort(queue, new CustomComparator());

    try {
        FileOutputStream fileOutputStream = new FileOutputStream(connectionString, false );
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(queue);
        objectOutputStream.close();
        fileOutputStream.close();
      }
      catch (FileNotFoundException e){
        File file = new File(deltaDAO.connectionString);
        try {
          file.createNewFile();
        }
        catch (IOException f){
          f.printStackTrace();
          throw new IllegalArgumentException("file unable to be created");
        }
      }
      catch (IOException e){
        throw new IllegalArgumentException("IOException");
      }
    }
  
};
