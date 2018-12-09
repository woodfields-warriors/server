package com.wwttr.dao;

import android.provider.ContactsContract;

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

  @Override
  public DatabaseFacade loadFromPersistance() {
    try {
      FileInputStream fileInputStream = new FileInputStream(connectionString);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      return (DatabaseFacade) objectInputStream.readObject();
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
  public void saveToPersistance(DatabaseFacade facade) {
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
