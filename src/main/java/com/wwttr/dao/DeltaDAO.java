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

import com.wwttr.route.Api;
import com.wwttr.game.Api;
import com.wwttr.game.Api.CreatePlayerRequest;
import com.wwttr.game.Api.LeaveGameRequest;
import com.wwttr.chat.Api;
import com.wwttr.chat.Api.CreateMessageRequest;
import com.wwttr.card.Api;
import com.wwttr.card.Api.ClaimDestinationCardsRequest;
import com.wwttr.card.Api.ClaimTrainCardRequest;
import com.wwttr.card.Api.DrawTrainCardFromDeckRequest;
import com.wwttr.auth.Api;
import com.wwttr.auth.Api.LoginAccountRequest;
import com.wwttr.server.Handler;

public abstract class DeltaDAO implements DAO {

  public final String connectionString;

  public DeltaDAO(){throw new IllegalArgumentException("connectionstring must be given");}

  public DeltaDAO(String connectionString) {
    this.connectionString = connectionString;
  }

  @Override
  public final void save(Object data){
    Delta d = (Delta) data;
    addCommandForGame(d);
  }

  @Override
  public final void load(DatabaseFacade facade){
    List<Delta> requests = loadFromPersistance();
  }


  public String getServiceFromMessage(Message m) {
    if (m instanceof ClaimRouteReuqest) {
      return "route.RouteService";
    }
    else if (m instanceof LoginAccountRequest) {
      return "auth.AuthService";
    }
    else if (m instanceof CreateMessageRequest) {
      return "chat.ChatService";
    }

    else if (m instanceof ClaimDestinationCardsRequest ||
            m instanceof ClaimTrainCardRequest ||
            m instanceof DrawTrainCardFromDeckRequest ||
            m instanceof DrawFaceUpTrainCardRequest) {
      return "card.CardService";
    }
    else  {
      return "game.GameService";
    }
  }



  public String getMethodFromMessage(Message m) {
    if (m instanceof ClaimRouteReuqest) {
      return "ClaimRoute";  //route.RouteService
    }
    else if (m instanceof CreateGameRequest) {
      return "CreateGame"; //game.GameService
    }
    else if (m instanceof LeaveGameRequest) {
      return "LeaveGame";
    }
    else if (m instanceof DeleteGameRequest) {
      return "DeleteGame";
    }
    else if (m instanceof StartGameRequest) {
      return "StartGame";
    }
    else if (m instanceof CreatePlayerRequest) {
      return "CreatePlayer";
    }
    else if (m instanceof CreateMessageRequest) {
      return "createMessage"; //chat.ChatService
    }
    else if (m instanceof ClaimDestinationCardsRequest) {
      return "ClaimDestinationCards"; //card.CardService
    }
    else if (m instanceof ClaimTrainCardRequest) {
      return "ClaimTrainCard";
    }
    else if (m instanceof DrawTrainCardFromDeckRequest) {
      return "DrawTrainCardFromDeck";
    }
    else if (m instanceof DrawFaceUpTrainCardRequest) {
      return "DrawFaceUpTrainCard";
    }
    else if (m instanceof LoginAccountRequest) {
      return "Register"; //auth.AuthService
    }
    else {
      return "NULL";
    }
  }

  public void execute(Message request) {
    String methodName = getMethodFromMessage(request);
    String serviceName = getServiceFromMessage(request);
    Handler handler = Handler.getInstance();
    handler.handleFromStrings(request, methodName, serviceName);
    
  }

  public abstract void addCommandForGame(Delta d);
  public abstract void clear();
  public abstract List<Object> loadFromPersistance();
  public abstract void saveToPersistance(List<Object> queue);
}

static class CustomComparator {
  public boolean compare(Object obj1, Object ob2) {
    Delta d1 = (Delta) obj1;
    Delta d2 = (Delta) d2;
    return d1.getId() < d2.getId();
  }

}