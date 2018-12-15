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
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Handler implements HttpHandler {

  private Map<String, Service> services;
  private static Handler instance;

  public Handler() {

  }

  public void init(Map<String, Service> services) {
    this.services = services;
  }

  public static Handler getInstance() {
    if( instance == null){
      instance = new Handler();
    }
    return instance;
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

  public void handleFromStrings(Message request, String requestId, String methodName, String serviceName) throws Exception {
    MethodDescriptor method;
    Service service;

    // specifies whether a request should be saved as a persistant delta
    //boolean shouldSave = false;

    try {
      service = services.get(serviceName);
      if (service == null) {
        throw new Exception("service not found");
      }
      method = service
        .getDescriptorForType()
        .findMethodByName(methodName);
      if (method == null) {
        throw new Exception("method not found");
      }
      //shouldSave = shouldSave(methodName);
    }
    catch (Exception e) {
      // Error with request deserialization
      e.printStackTrace();

      throw new Exception("error parsing request");

    }

    //HttpExchange exchange = new HttpExchange();
    Controller controller = new Controller(requestId);

    RpcCallback<Message> callback;
    if (!method.toProto().getServerStreaming()) {
      callback = controller.unaryHandler(requestId);
    } else {
      try {
        callback = controller.streamHandler(requestId);
      }
      catch (IOException e) {
        e.printStackTrace();
        throw e;
      }
    }

    try {
      service.callMethod(method, controller, request, (Message msg) -> {
        System.out.println(msg);
      });
    }
    catch (ApiError e) {
      if (controller.isCanceled()) {
        return;
      }
      controller.startCancel();
      // Error with method execution
      
      throw e;
    }
  }

  @Override
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
    //boolean shouldSave = false;

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

      //shouldSave = shouldSave(requestWrapper.getMethod());
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
      callback = controller.unaryHandler("resp_" + requestId);
    } else {
      try {
        callback = controller.streamHandler("resp_"+requestId);
      }
      catch (IOException e) {
        e.printStackTrace();
        return;
      }
    }

    try {
      /*if (method == null) {
        System.out.println("method is null");
      }
      if (controller == null) {
        System.out.println("controller is null");
      }
      if (request == null) {
        System.out.println("request is null");
      }
      if (callback == null) {
        System.out.println("callback is null");
      }
      if (shouldSave) {
        System.out.println("shouldsave is true");
      } */
      service.callMethod(method, controller, request, callback);

      /*if (shouldSave) {
        try {
          Service gameService = services.get("game.GameService");
          MethodDescriptor addDeltaMethod = gameService
                                              .getDescriptorForType()
                                              .findMethodByName("AddDelta");
          gameService.callMethod(addDeltaMethod, controller, request, callback);
        }
        catch(ApiError e) {
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
      } */
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
