package com.wwttr.dao;

import com.wwttr.database.DatabaseFacade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;

public class GameDAONoSQL extends GameDAO {

  protected final String connectionString;

  public GameDAONoSQL(String connectionString) {
    this.connectionString = connectionString;
  }

  @Override
  public DatabaseFacade loadFromPersistence() {
    try {
      FileInputStream fileInputStream = new FileInputStream(connectionString);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);      
      return (DatabaseFacade) objectInputStream.readObject();
    }
    catch (FileNotFoundException e){
      File file = new File(connectionString);
      try {
        file.createNewFile();
        return DatabaseFacade.getInstance();
      }
      catch (IOException f){
        f.printStackTrace();
        throw new IllegalArgumentException("file unable to be created");
      }
      //java.io.File = new File(connectionString);
      //throw new IllegalArgumentException("File not found");
    }
    catch (IOException e){
      e.printStackTrace();
      throw new IllegalArgumentException("IOException");
    }
    catch (ClassNotFoundException e){
      e.printStackTrace();
      throw new IllegalArgumentException("Class not Found");
    }
  }

  @Override
  public void saveToPersistence(DatabaseFacade facade) {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(connectionString, false );
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(facade);
      objectOutputStream.close();
      fileOutputStream.close();
    }
    catch (FileNotFoundException e){
      e.printStackTrace();
      throw new IllegalArgumentException("File not found");
    }
    catch (IOException e){
      e.printStackTrace();
      throw new IllegalArgumentException("IOException");
    }
  }
}
