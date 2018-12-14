package com.wwttr.server;

import com.wwttr.api.Response;
import com.wwttr.api.Code;
import com.wwttr.api.ApiError;

import com.google.protobuf.ByteString;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.util.Timer;
import java.util.TimerTask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class UnaryResponder {

  private HttpExchange exchange;

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
    case PING:
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

  private Controller controller;
  private boolean open;
  private Timer timer;
  // 15 second delay.
  private long pingDelay = 15000;

  public StreamResponder(Controller controller) throws IOException {
    this.controller = controller;
    controller.getExchange().getResponseHeaders().set("Content-Type", "application/proto");
    controller.getExchange().sendResponseHeaders(200, 0);
    open = true;
    setTimer();
  }

  public boolean isOpen() {
    return open;
  }

  public void respond(Response response) throws IOException {
    if (controller.isCanceled()) {
      return;
    }

    ByteString responseData = response.toByteString();

    ByteBuffer buf = ByteBuffer.allocate(4);
    buf.order(ByteOrder.LITTLE_ENDIAN);
    buf.putInt(responseData.size());

    OutputStream out = controller.getExchange().getResponseBody();

    synchronized (this) {
      out.write(buf.array());
      responseData.writeTo(out);
    }
    out.flush();
    setTimer();
  }

  public void close() throws IOException {
    open = false;
    timer.cancel();
    timer.purge();
    if (!controller.isCanceled()) {
      controller.startCancel();
    }
    controller.getExchange().getResponseBody().close();
  }

  private void setTimer() {
    synchronized (this) {
      if (timer != null) {
        timer.cancel();
        timer.purge();
      }

      timer = new Timer();
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          try {
            Response.Builder builder = Response.newBuilder();
            builder.setCode(Code.PING);
            builder.setId("PING");
            respond(builder.build());
          } catch (IOException e) {
            e.printStackTrace();
            try {
              close();
            } catch (IOException e2) {
              // e2.printStackTrace();
            }
          }
        }
      }, pingDelay, pingDelay);
    }
  }
}
