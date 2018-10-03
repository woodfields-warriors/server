package com.wwttr.main;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;

import com.sun.net.httpserver.HttpExchange;


class Controller implements RpcController {

  private HttpExchange exchange;
  private Throwable error;

  public Controller(HttpExchange exchange) {
    this.exchange = exchange;
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
    return false;
  }

  public void notifyOnCancel(RpcCallback<Object> callback) {

  }

  public void reset() {

  }

  public void setFailed(String reason) {

  }

  public void startCancel() {

  }
}
