package com.wwttr.server;

import com.wwttr.api.Request;
import com.wwttr.api.Response;
import com.wwttr.api.Code;
import com.wwttr.api.ApiError;

import com.google.protobuf.Service;
import com.google.protobuf.RpcController;
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

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    // System.out.println(exchange.getRequestURI().getRawPath() + " " + exchange.getRequestMethod());
    // for (String key : exchange.getRequestHeaders().keySet()) {
    //   System.out.println(key + ": " + exchange.getRequestHeaders().get(key));
    // }

    Message request;
    MethodDescriptor method;
    Service service;
    try {
      InputStream stream = exchange.getRequestBody();
      Request requestWrapper = Request.parseFrom(stream);

      service = services.get(requestWrapper.getService());
      if (service == null) {
        Response.Builder response = Response.newBuilder();
        response.setCode(Code.NOT_FOUND);
        System.out.println("service " + requestWrapper.getService() + " not found.");
        UnaryResponder responder = new UnaryResponder(exchange);
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
        responder.respond(response.build());
        return;
      }
      request = service
        .getRequestPrototype(method)
        .newBuilderForType()
        .mergeFrom(requestWrapper.getPayload())
        .build();
    }
    catch (Exception e) {
      // Error with request deserialization
      e.printStackTrace();
      Response.Builder response = Response.newBuilder();
      response.setCode(Code.INVALID_ARGUMENT);
      response.setMessage("error parsing request");

      UnaryResponder responder = new UnaryResponder(exchange);
      responder.respond(response.build());

      return;
    }

    Controller controller = new Controller(exchange);

    RpcCallback<Message> callback;
    if (!method.toProto().getServerStreaming()) {
      callback = Handler.unaryHandler(controller);
    } else {
      try {
        callback = Handler.streamHandler(controller);
      }
      catch (IOException e) {
        e.printStackTrace();
        return;
      }
    }

    try {
      service.callMethod(method, controller, request, callback);
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

      UnaryResponder responder = new UnaryResponder(exchange);
      try {
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
      response.setMessage("");

      UnaryResponder responder = new UnaryResponder(exchange);
      try {
        responder.respond(response.build());
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      return;
    }
  }

  static RpcCallback<Message> unaryHandler(Controller controller) {
    return (Message response) -> {
      synchronized (controller) {
        if (controller.isCanceled()) {
          return;
        }

        if (response == null) {
          throw new Error("server cannot respond with null");
        }

        Response.Builder responseWrapper = Response.newBuilder();
        responseWrapper.setCode(Code.OK);
        responseWrapper.setPayload(response.toByteString());

        UnaryResponder responder = new UnaryResponder(controller.getExchange());
        try {
          responder.respond(responseWrapper.build());
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
  }

  static RpcCallback<Message> streamHandler(Controller controller) throws IOException {
    StreamResponder responder = new StreamResponder(controller.getExchange());
    return (Message response) -> {
      synchronized (responder) {
        if (!responder.isOpen()) {
          return;
        }
        if (controller.isCanceled() || response == null) {
          try {
            responder.close();
          }
          catch (IOException e) {
            e.printStackTrace();
          }
          return;
        }

        Response.Builder responseWrapper = Response.newBuilder();
        responseWrapper.setCode(Code.OK);
        responseWrapper.setPayload(response.toByteString());

        try {
          responder.respond(responseWrapper.build());
        }
        catch (IOException e) {
          e.printStackTrace();
          controller.startCancel();
        }
      }
    };
  }
}

class UnaryResponder {

  private HttpExchange exchange;
  private boolean headerSent;

  public UnaryResponder(HttpExchange exchange) {
    this.exchange = exchange;
  }

  public void respond(Response response) throws IOException {

    exchange.getResponseHeaders().set("Content-Type", "application/proto");

    ByteString responseData = response.toByteString();
    int status = UnaryResponder.codeToHTTPStatus(response.getCode());
    exchange.sendResponseHeaders(status, responseData.size());

    OutputStream out = exchange.getResponseBody();
    responseData.writeTo(out);
    out.close();
  }

  static int codeToHTTPStatus(Code code) {
    switch (code) {
    case OK:
      return 200;
    case NOT_FOUND:
      return 404;
    case INVALID_ARGUMENT:
      return 400;
    case INTERNAL:
      return 500;
    case UNAVAILABLE:
      return 503;
    default:
      return 500;
    }
  }
}

class StreamResponder {

  private HttpExchange exchange;
  private boolean open;

  public StreamResponder(HttpExchange exchange) throws IOException {
    this.exchange = exchange;
    exchange.getResponseHeaders().set("Content-Type", "application/proto");
    exchange.sendResponseHeaders(200, 0);
    open = true;
  }

  public boolean isOpen() {
    return open;
  }

  public void respond(Response response) throws IOException {
    ByteString responseData = response.toByteString();

    ByteBuffer buf = ByteBuffer.allocate(4);
    buf.order(ByteOrder.LITTLE_ENDIAN);
    buf.putInt(responseData.size());
    System.out.println("sending length: " + buf.getInt(0));
    System.out.println("sending data: " + responseData);
    for (int i = 0; i < buf.capacity(); i++) {
      System.out.print(buf.get(i));
      System.out.print(" ");
    }
    System.out.println();
    for (int i = 0; i < buf.capacity(); i++) {
      System.out.print((char)buf.get(i));
    }
    System.out.println();
    OutputStream out = exchange.getResponseBody();
    out.write(buf.array());
    responseData.writeTo(out);
    out.flush();
  }

  public void close() throws IOException {
    open = false;
    exchange.getResponseBody().close();
  }
}
