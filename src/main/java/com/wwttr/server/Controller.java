package com.wwttr.server;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.Message;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import com.wwttr.api.Response;
import com.wwttr.api.Code;

public class Controller implements RpcController {

  private HttpExchange exchange;
  private Throwable error;
  private boolean canceled = false;
  private StreamResponder responder;
  private String id;

  public Controller(HttpExchange exchange, String id) {
    this.exchange = exchange;
    this.id = id;
  }

  public Controller(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public HttpExchange getExchange() {
    return exchange;
  }

  public String errorText() {
    if (error == null) {
      return null;
    }
    return error.getMessage();
  }

  public boolean failed() {
    return error != null;
  }

  public boolean isCanceled() {
    return canceled;
  }

  public void notifyOnCancel(RpcCallback<Object> callback) {

  }

  public void reset() {

  }

  public void setFailed(String reason) {

  }

  public void startCancel() {
    canceled = true;
  }

  public StreamResponder getResponder() {
    return responder;
  }

  RpcCallback<Message> unaryHandler(String reqId) {
    return (Message response) -> {
      synchronized (this) {
        if (isCanceled()) {
          return;
        }

        if (response == null) {
          throw new Error("server cannot respond with null");
        }

        Response.Builder responseWrapper = Response.newBuilder();
        responseWrapper.setCode(Code.OK);
        responseWrapper.setPayload(response.toByteString());
        responseWrapper.setId(reqId);
        UnaryResponder responder = new UnaryResponder(getExchange());
        try {
          responder.respond(responseWrapper.build());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
  }

  RpcCallback<Message> streamHandler(String reqId) throws IOException {
    return (Message response) -> {
      synchronized (this) {
        try {

          if (responder == null) {
            responder = new StreamResponder(this);
          }
          if (!responder.isOpen()) {
            return;
          }
          if (isCanceled() || response == null) {
            responder.close();
            return;
          }

          Response.Builder responseWrapper = Response.newBuilder();
          responseWrapper.setCode(Code.OK);
          responseWrapper.setPayload(response.toByteString());
          responseWrapper.setId(reqId + "-" + new java.util.Random().nextInt(Integer.MAX_VALUE));
          responder.respond(responseWrapper.build());

        } catch (IOException e) {
          e.printStackTrace();
          startCancel();
        }
      }
    };
  }
}
