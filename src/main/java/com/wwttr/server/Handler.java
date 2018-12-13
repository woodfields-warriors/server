package com.wwttr.server;

import com.wwttr.api.Request;
import com.wwttr.api.Response;
import com.wwttr.api.Code;
import com.wwttr.api.ApiError;

import com.google.protobuf.Service;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.ByteString;
import com.google.protobuf.RpcCallback;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


class Handler implements HttpHandler {

  private Map<String, Service> services;

  public Handler(Map<String, Service> services) {
    this.services = services;
  }

  // determines whether a request should be saved as a persistant
  // delta, i.e. whether the method called is "game-changing"
  public boolean shouldSave(String method) {
    switch(method) {
      // "game-changing" methods in RouteService
      case "ClaimRoute" : //routeId, playerId, cardIds
        return true;
      // "game-changing" methods in GameService
      case "CreateGame" : // userId, displayName, maxPlayers
        return true;
      case "LeaveGame" :  // playerId, gameId
        return true;
      case "DeleteGame" : // gameId
        return true;
      case "StartGame" :  // gameId
        return true;
      case "CreatePlayer" : // userId, gameId
        return true;
      // "game-changing" methods in ChatService
      case "createMessage" :  // content, playerId  // ChatService methods not capitalized
        return true;
      // "game-changing" methods in CardService
      case "ClaimDestinationCards" :  // destinationCardIds, playerId
        return true;
      case "ClaimTrainCard" : // TODO ???
        return true;
      case "DrawTrainCardFromDeck" :  // id
        return true;
      case "DrawFaceUpTrainCard" :  // id, cardDrawnId
        return true;
      // "game-changing" methods in AuthService 
      case "Register" : // username, password
        return true;
      default:
        return false;
    }
  }

  @Overrid
  public void handle(HttpExchange exchange) throws IOException {

    // System.out.println(exchange.getRequestURI().getRawPath() + " " + exchange.getRequestMethod());
    // for (String key : exchange.getRequestHeaders().keySet()) {
    //   System.out.println(key + ": " + exchange.getRequestHeaders().get(key));
    // }

    String requestId = System.currentTimeMillis() + String.format("%010d", (new Random()).nextInt(Integer.MAX_VALUE));
    Message request;
    MethodDescriptor method;
    Service service;

    // specifies whether a request should be saved as a persistant delta
    boolean shouldSave = false;

    try {
      InputStream stream = exchange.getRequestBody();
      Request requestWrapper = Request.parseFrom(stream);

      service = services.get(requestWrapper.getService());
      if (service == null) {
        Response.Builder response = Response.newBuilder();
        response.setCode(Code.NOT_FOUND);
        System.out.println("service " + requestWrapper.getService() + " not found.");
        UnaryResponder responder = new UnaryResponder(exchange);
        response.setId("resp_" +requestId);
        responder.respond(response.build());
        return;
      }
      method = service
        .getDescriptorForType()
        .findMethodByName(requestWrapper.getMethod());
      if (method == null) {
        Response.Builder response = Response.newBuilder();
        response.setCode(Code.NOT_FOUND);
        System.out.println("method " + requestWrapper.getMethod() + " not found.");

        UnaryResponder responder = new UnaryResponder(exchange);
        response.setId("resp_" +requestId);
        responder.respond(response.build());
        return;
      }
      request = service
        .getRequestPrototype(method)
        .newBuilderForType()
        .mergeFrom(requestWrapper.getPayload())
        .build();

      shouldSave = shouldSave(requestWrapper.getMethod()); 
    }
    catch (Exception e) {
      // Error with request deserialization
      e.printStackTrace();
      Response.Builder response = Response.newBuilder();
      response.setCode(Code.INVALID_ARGUMENT);
      response.setMessage("error parsing request");

      UnaryResponder responder = new UnaryResponder(exchange);
      response.setId("resp_"+requestId);
      responder.respond(response.build());

      return;
    }

    Controller controller = new Controller(exchange, "req_"+requestId);
    
    RpcCallback<Message> callback;
    if (!method.toProto().getServerStreaming()) {
      callback = controller.unaryHandler();
    } else {
      try {
        callback = controller.streamHandler();
      }
      catch (IOException e) {
        e.printStackTrace();
        return;
      }
    }

    try {
      service.callMethod(method, controller, request, callback);

      if (shouldSave) {
        try {
          Service gameService = services.get("game.GameService");
          MethodDescriptor addDeltaMethod = gameService
                                              .getDescriptorForType()
                                              .findMethodByName("AddDelta");
          gameService.callMethod(addDeltaMethod, controller, request, callback);
        }
        catch(Exception e) {
          if (controller.isCanceled()) {
            return;
          }
          controller.startCancel();
          // Error with method execution
          Response.Builder response = Response.newBuilder();
          response.setCode(e.getCode());
          response.setMessage("DELTA ERROR: " + e.getMessage());
          response.setId("resp_"+requestId);
          try {
            if (controller.getResponder() != null) {
              controller.getResponder().respond(response.build());
              controller.getResponder().close();
              return;
            }
            UnaryResponder responder = new UnaryResponder(exchange);
            response.setId("resp_"+requestId);
            responder.respond(response.build());
          }
          catch (IOException ioE) {
            ioE.printStackTrace();
          }
          return;
        }
      }
    }
    catch (ApiError e) {
      if (controller.isCanceled()) {
        return;
      }
      controller.startCancel();
      // Error with method execution
      Response.Builder response = Response.newBuilder();
      response.setCode(e.getCode());
      response.setMessage(e.getMessage());
      response.setId("resp_"+requestId);
      try {
        if (controller.getResponder() != null) {
          controller.getResponder().respond(response.build());
          controller.getResponder().close();
          return;
        }
        UnaryResponder responder = new UnaryResponder(exchange);
        response.setId("resp_"+requestId);
        responder.respond(response.build());
      }
      catch (IOException ioE) {
        ioE.printStackTrace();
      }
      return;
    }
    catch (Throwable t) {
      if (controller.isCanceled()) {
        return;
      }
      controller.startCancel();
      // Error with method execution
      System.out.println(service.getDescriptorForType().getName() + "." + method.getName() + ":");
      t.printStackTrace();

      Response.Builder response = Response.newBuilder();
      response.setCode(Code.INTERNAL);
      response.setId("resp_"+requestId);
      response.setMessage("");

      try {
        if (controller.getResponder() != null) {
          controller.getResponder().respond(response.build());
          controller.getResponder().close();
          return;
        }
        UnaryResponder responder = new UnaryResponder(exchange);
        responder.respond(response.build());
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      return;
    }
  }
}
