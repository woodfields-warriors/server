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

  public GameDAONoSQL(String connectionString) {
    super(connectionString);
  }

  @Override
  public DatabaseFacade loadFromPersistence() {
    try {
      FileInputStream fileInputStream = new FileInputStream(connectionString);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      return (DatabaseFacade) objectInputStream.readObject();
    }
    catch (FileNotFoundException e){
      java.io.File = new File(connectionString);
      //throw new IllegalArgumentException("File not found");
    }
    catch (IOException e){
      throw new IllegalArgumentException("IOException");
    }
    catch (ClassNotFoundException e){
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
      throw new IllegalArgumentException("File not found");
    }
    catch (IOException e){
      throw new IllegalArgumentException("IOException");
    }
  }
}
