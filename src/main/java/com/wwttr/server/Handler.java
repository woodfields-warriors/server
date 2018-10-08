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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


class Handler implements HttpHandler {

  private Map<String, Service> services;

  public Handler(Map<String, Service> services) {
    this.services = services;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {

    Message request;
    MethodDescriptor method;
    Service service;
    try {
      InputStream stream = exchange.getRequestBody();
      Request requestWrapper = Request.parseFrom(stream);

      service = services.get(requestWrapper.getService());
      if (service == null) {
        Response.Builder respBuilder = Response.newBuilder();
        respBuilder.setCode(Code.NOT_FOUND);
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
      Response.Builder respBuilder = Response.newBuilder();
      respBuilder.setCode(Code.INVALID_ARGUMENT);
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

    Controller controller = new Controller(exchange);

    try {
      service.callMethod(method, controller, request, (Message response) -> {
        if (controller.isCanceled()) {
          return;
        }

        if (response == null) {
          // Oops!
          throw new Error("handler for " + service.getDescriptorForType().getName() + "." + method.getName() + " returned null");
        }

        try {
          Response.Builder respBuilder = Response.newBuilder();
          respBuilder.setCode(Code.OK);
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
        catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
    catch (ApiError e) {
      if (controller.isCanceled()) {
        return;
      }
      controller.startCancel();
      // Error with method execution
      Response.Builder respBuilder = Response.newBuilder();
      respBuilder.setCode(e.getCode());
      respBuilder.setMessage(e.getMessage());
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
    catch (Throwable t) {
      if (controller.isCanceled()) {
        return;
      }
      controller.startCancel();
      // Error with method execution
      System.out.println(service.getDescriptorForType().getName() + "." + method.getName() + ": " + t.toString());

      Response.Builder respBuilder = Response.newBuilder();
      respBuilder.setCode(Code.INTERNAL);
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
  }
}
