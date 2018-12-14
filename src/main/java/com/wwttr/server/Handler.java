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

  public void handleFromStrings(Message request, String requestId, String methodName, String serviceName) throws IOException {
    MethodDescriptor method;
    Service service;

    // specifies whether a request should be saved as a persistant delta
    boolean shouldSave = false;


    HttpExchange exchange = new HttpExchange(){

      @Override
      public void setStreams(InputStream arg0, OutputStream arg1) {

      }

      @Override
      public void setAttribute(String arg0, Object arg1) {

      }

      @Override
      public void sendResponseHeaders(int arg0, long arg1) throws IOException {

      }

      @Override
      public Headers getResponseHeaders() {
        return null;
      }

      @Override
      public int getResponseCode() {
        return 0;
      }

      @Override
      public OutputStream getResponseBody() {
        return null;
      }

      @Override
      public URI getRequestURI() {
        return null;
      }

      @Override
      public String getRequestMethod() {
        return null;
      }

      @Override
      public Headers getRequestHeaders() {
        return null;
      }

      @Override
      public InputStream getRequestBody() {
        return null;
      }

      @Override
      public InetSocketAddress getRemoteAddress() {
        return null;
      }

      @Override
      public String getProtocol() {
        return null;
      }

      @Override
      public HttpPrincipal getPrincipal() {
        return null;
      }

      @Override
      public InetSocketAddress getLocalAddress() {
        return null;
      }

      @Override
      public HttpContext getHttpContext() {
        return null;
      }

      @Override
      public Object getAttribute(String arg0) {
        return null;
      }

      @Override
      public void close() {

      }
    };

    try {
      service = services.get(serviceName);
      if (service == null) {
        Response.Builder response = Response.newBuilder();
        response.setCode(Code.NOT_FOUND);
        System.out.println("service " + serviceName + " not found.");
        UnaryResponder responder = new UnaryResponder(exchange);
        response.setId("resp_");
        responder.respond(response.build());
        return;
      }
      method = service
        .getDescriptorForType()
        .findMethodByName(methodName);
      if (method == null) {
        Response.Builder response = Response.newBuilder();
        response.setCode(Code.NOT_FOUND);
        System.out.println("method " + methodName + " not found.");

        UnaryResponder responder = new UnaryResponder(exchange);
        //response.setId("resp_" +requestId);
        responder.respond(response.build());
        return;
      }
      shouldSave = shouldSave(methodName);
    }
    catch (Exception e) {
      // Error with request deserialization
      e.printStackTrace();
      Response.Builder response = Response.newBuilder();
      response.setCode(Code.INVALID_ARGUMENT);
      response.setMessage("error parsing request");

      UnaryResponder responder = new UnaryResponder(exchange);
      //response.setId("resp_"+requestId);
      responder.respond(response.build());

      return;
    }

    //HttpExchange exchange = new HttpExchange();
    Controller controller = new Controller(exchange, "req_");

    RpcCallback<Message> callback;
    if (!method.toProto().getServerStreaming()) {
      callback = controller.unaryHandler(requestId);
    } else {
      try {
        callback = controller.streamHandler(requestId);
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
        catch(ApiError e) {
          if (controller.isCanceled()) {
            return;
          }
          controller.startCancel();
          // Error with method execution
          Response.Builder response = Response.newBuilder();
          response.setCode(e.getCode());
          response.setMessage("DELTA ERROR: " + e.getMessage());
          response.setId("resp_");
          try {
            if (controller.getResponder() != null) {
              controller.getResponder().respond(response.build());
              controller.getResponder().close();
              return;
            }
            UnaryResponder responder = new UnaryResponder(exchange);
            response.setId("resp_");
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
      response.setId("resp_");
      try {
        if (controller.getResponder() != null) {
          controller.getResponder().respond(response.build());
          controller.getResponder().close();
          return;
        }
        UnaryResponder responder = new UnaryResponder(exchange);
        response.setId("resp_");
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
      response.setId("resp_");
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
      if (method == null) {
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
      }
      service.callMethod(method, controller, request, callback);

      if (shouldSave) {
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
