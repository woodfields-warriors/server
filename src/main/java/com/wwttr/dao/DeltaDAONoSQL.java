package com.wwttr.dao;

//import com.sun.accessibility.internal.resources.accessibility_de;
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
//import java.util.java.util.ArrayList;


public class DeltaDAONoSQL extends DeltaDAO {

  public DeltaDAONoSQL(String connectionString) {
    super(connectionString);
  }

  public java.util.ArrayList<Delta> loadFileFromPersistance(String path) {
    File file = new File(path);
    if (!file.exists()) {
      try {
        file.createNewFile();
        return null;
      }
      catch (IOException f){
        f.printStackTrace();
        throw new IllegalArgumentException("file unable to be created");
      }
    }
    else {
      try {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (java.util.ArrayList<Delta>) objectInputStream.readObject();
      }
      catch(Exception e) {
        return new java.util.ArrayList<Delta>();
      }
      
    }
  }

  @Override
  public java.util.ArrayList<Delta> loadFromPersistance() {
    try {
      /* assuming the file names will be the gameids and the connnectionString
        in the constructor is the directory  */
      File directory = new File(connectionString);
      if (!directory.exists()) {
        directory.mkdir();
      }

      java.util.ArrayList<Delta> queue = new java.util.ArrayList<Delta>();
      File[] listOfFiles = directory.listFiles();
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          java.util.ArrayList<Delta> savedDeltas = loadFileFromPersistance(listOfFiles[i].getAbsolutePath());
          for (Delta d : savedDeltas) {
            queue.add(d);
          }
        }
        else {
          System.out.println("Found a non-file" + listOfFiles[i].getAbsolutePath());
        }
      }

      //Collections.sort(queue, new CustomComparator());
      return queue;
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }


  @Override
  public void clear() {
    /* assuming the file names will be the gameids and the connnectionString
        in the constructor is the directory  */
      File directory = new File(connectionString);
      if (!directory.exists()) {
        directory.mkdir();
      }

      File[] listOfFiles = directory.listFiles();
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          try {
            java.io.PrintWriter pw = new java.io.PrintWriter(listOfFiles[i].getAbsolutePath());
            pw.close();
          }
          catch (FileNotFoundException e){
            throw new IllegalArgumentException("File not found");
          }
        }
        else {
          System.out.println("Found a non-file" + listOfFiles[i].getAbsolutePath());
        }
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
    java.util.ArrayList<Delta> queue = loadFileFromPersistance(this.connectionString + "/" + d.getGameId() + ".txt");
    if (queue == null) {
      queue = new java.util.ArrayList<Delta>();
    }
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
        File file = new File(connectionString);
        try {
          file.createNewFile();
        }
        catch (IOException f){
          f.printStackTrace();
          throw new IllegalArgumentException("file unable to be created");
        }
      }
      catch (IOException e){
        e.printStackTrace();
        throw new IllegalArgumentException("IOException");
      }
    }

};
