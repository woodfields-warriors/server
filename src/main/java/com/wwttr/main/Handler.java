package com.wwttr.main;

import com.wwttr.api.Request;
import com.wwttr.api.Response;

import com.google.protobuf.BlockingService;
import com.google.protobuf.RpcController;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Message;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


class Handler implements HttpHandler {

  private Map<String, BlockingService> services;

  public Handler(Map<String, BlockingService> services) {
    this.services = services;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    System.out.println("got request");

    Message request;
    MethodDescriptor method;
    BlockingService service;
    try {
      InputStream stream = exchange.getRequestBody();
      Request requestWrapper = Request.parseFrom(stream);

      service = services.get(requestWrapper.getService());
      method = service
        .getDescriptorForType()
        .findMethodByName(requestWrapper.getMethod());
      request = service
        .getRequestPrototype(method)
        .newBuilderForType()
        .mergeFrom(requestWrapper.getPayload())
        .build();
    }
    catch (Exception e) {
      // Error with request deserialization
      System.out.println("deserialization error");
      return;
    }

    Message response;
    try {
      Controller controller = new Controller(exchange);
      response = service.callBlockingMethod(method, controller, request);
    }
    catch (Exception e) {
      // Error with method execution
      System.out.println("method execution error");
      return;
    }

    try {
      Response.Builder respBuilder = Response.newBuilder();
      builder.setCode(Response.Code.OK);
      builder.setPayload(request.toByteString());
      ByteString responseData = builder.build().toByteString();

      Map<String, String> headers = exchange.getResponseHeaders();
      headers.put("Content-Type", "application/proto");
      exchange.sendResponseHeaders(200, responseData.length);
      OutputStream stream = exchange.getOutputStream();
    }
    catch (Exception e) {
      System.out.println("response serialization error");
    }
    // finally {
    //   if (connection != null) {
    //    connection.disconnect();
    //   }
    // }
  }
}
