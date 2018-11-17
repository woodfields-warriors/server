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

      try {
        if (controller.getResponder() != null) {
          controller.getResponder().respond(response.build());
          controller.getResponder().close();
          return;
        }
        UnaryResponder responder = new UnaryResponder(exchange);
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
