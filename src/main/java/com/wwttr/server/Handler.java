package com.wwttr.server;

import com.wwttr.api.Request;
import com.wwttr.api.Response;

import com.google.protobuf.BlockingService;
import com.google.protobuf.RpcController;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.ByteString;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

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
      if (service == null) {
        Response.Builder respBuilder = Response.newBuilder();
        respBuilder.setCode(Response.Code.NOT_FOUND);
        ByteString responseData = respBuilder.build().toByteString();

        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/proto");
        exchange.sendResponseHeaders(404, responseData.size());
        OutputStream out = exchange.getResponseBody();
        responseData.writeTo(out);
        out.flush();
        out.close();
        return;
      }
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
      System.out.println("deserialization error: " + e.toString());

      Response.Builder respBuilder = Response.newBuilder();
      respBuilder.setCode(Response.Code.INVALID_ARGUMENT);
      respBuilder.setMessage("error parsing request");
      ByteString responseData = respBuilder.build().toByteString();

      Headers headers = exchange.getResponseHeaders();
      headers.set("Content-Type", "application/proto");
      exchange.sendResponseHeaders(400, responseData.size());
      OutputStream out = exchange.getResponseBody();
      responseData.writeTo(out);
      out.flush();
      out.close();

      return;
    }

    Message response;
    try {
      Controller controller = new Controller(exchange);
      response = service.callBlockingMethod(method, controller, request);
    }
    catch (Exception e) {
      // Error with method execution
      System.out.println("method execution error: " + e.toString());
      System.out.println(e);

      Response.Builder respBuilder = Response.newBuilder();
      respBuilder.setCode(Response.Code.INTERNAL);
      respBuilder.setMessage("");
      ByteString responseData = respBuilder.build().toByteString();

      Headers headers = exchange.getResponseHeaders();
      headers.set("Content-Type", "application/proto");
      exchange.sendResponseHeaders(500, responseData.size());
      OutputStream out = exchange.getResponseBody();
      responseData.writeTo(out);
      out.flush();
      out.close();
      return;
    }

    try {
      Response.Builder respBuilder = Response.newBuilder();
      respBuilder.setCode(Response.Code.OK);
      respBuilder.setPayload(response.toByteString());
      ByteString responseData = respBuilder.build().toByteString();

      Headers headers = exchange.getResponseHeaders();
      headers.set("Content-Type", "application/proto");
      exchange.sendResponseHeaders(200, responseData.size());
      OutputStream out = exchange.getResponseBody();
      responseData.writeTo(out);
      out.flush();
      out.close();
    }
    catch (Exception e) {
      System.out.println("response serialization error: " + e.toString());
      return;
    }
    // finally {
    //   if (connection != null) {
    //    connection.disconnect();
    //   }
    // }
  }
}
