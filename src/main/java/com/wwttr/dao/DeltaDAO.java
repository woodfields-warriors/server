package com.wwttr.dao;

import com.wwttr.api.Request;
import com.wwttr.api.Response;
import com.wwttr.api.Code;
import com.wwttr.api.ApiError;

import com.wwttr.database.CommandQueue;
import com.wwttr.database.DatabaseFacade;
import com.wwttr.models.Delta;
import com.google.protobuf.Message;
import com.google.protobuf.CodedOutputStream;
import java.util.Collections;

//import java.util.java.util.ArrayList;

public abstract class DeltaDAO implements com.wwttr.database.DAO {

  public final String connectionString;

  public DeltaDAO(){throw new IllegalArgumentException("connectionstring must be given");}

  public DeltaDAO(String connectionString) {
    this.connectionString = connectionString;
  }

  @Override
  public final void save(Object data){
    Delta d = (Delta) data;
    System.out.println(d.getId());
    addCommandForGame(d);
  }

  @Override
  public final void load(DatabaseFacade facade){
    System.out.println("in DeltaDAO.load");
    java.util.TreeMap<String, Delta> requests = loadFromPersistance();
    System.out.println("loaded " + Integer.toString(requests.size()) + " deltas from persistence");

    for (Delta d : requests.values()) {
      facade.execute(d);
    }
  }




  public abstract void addCommandForGame(Delta d);
  public abstract void clear();
  public abstract java.util.TreeMap<String, Delta> loadFromPersistance();
  // public abstract void saveToPersistance(List<Object> queue);
}