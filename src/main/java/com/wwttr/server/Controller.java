package com.wwttr.server;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;

import com.sun.net.httpserver.HttpExchange;


class Controller implements RpcController {

  private HttpExchange exchange;
  private Throwable error;
  private boolean canceled = false;

  public Controller(HttpExchange exchange) {
    this.exchange = exchange;
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
}
