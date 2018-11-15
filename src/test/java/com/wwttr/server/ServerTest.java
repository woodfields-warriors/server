package com.wwttr.server;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.protobuf.ByteString;

import java.net.HttpURLConnection;
import java.net.URL;

import com.wwttr.api.Request;
import com.wwttr.api.Response;
import com.wwttr.api.Code;
import com.wwttr.health.Api.Health;
import com.wwttr.health.HealthHandlers;

import java.util.Arrays;

public class ServerTest {

  @Test public void testUnary() throws IOException, InterruptedException {
    Server server = new Server();
    server.register(new HealthHandlers());
    server.start(8080);

    Request.Builder requestBuilder = Request.newBuilder();
    requestBuilder.setService("health.HealthService");
    requestBuilder.setMethod("GetHealth");
    ByteString requestData = requestBuilder.build().toByteString();

    URL obj = new URL("http://localhost:8080/");
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/protobuf");
    con.setDoOutput(true);
    OutputStream out = con.getOutputStream();
    requestData.writeTo(out);
    out.flush();
    out.close();

    // Thread.sleep(4000);
    //
    // assertTrue(false);

    assertEquals(200, con.getResponseCode());
    InputStream in = con.getInputStream();

    Response response = Response.parseFrom(in);

    assertEquals(Code.OK, response.getCode());
    assertEquals("", response.getMessage());
    Health health = Health.parseFrom(response.getPayload());

    assertEquals(Health.Status.READY, health.getStatus());

    server.stop();
  }

  @Test public void testStream() throws IOException, InterruptedException {
    Server server = new Server();
    server.register(new HealthHandlers());
    server.start(8081);

    Request.Builder requestBuilder = Request.newBuilder();
    requestBuilder.setService("health.HealthService");
    requestBuilder.setMethod("StreamHealth");
    ByteString requestData = requestBuilder.build().toByteString();

    URL obj = new URL("http://localhost:8081/");
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/protobuf");
    con.setDoOutput(true);
    OutputStream out = con.getOutputStream();
    requestData.writeTo(out);
    out.flush();
    out.close();

    // Thread.sleep(4000);
    //
    // assertTrue(false);

    assertEquals(200, con.getResponseCode());
    InputStream in = con.getInputStream();


    for (int responseCount = 0; responseCount < 3; responseCount++) {
      ByteBuffer lenBuf = ByteBuffer.allocate(4);
      lenBuf.order(ByteOrder.LITTLE_ENDIAN);

      for (int i = 0; i < lenBuf.capacity(); i++) {
        lenBuf.put((byte)in.read());
      }
      System.out.println("got length: " + lenBuf.getInt(0));
      byte[] buf = new byte[lenBuf.getInt(0)];
      in.read(buf);
      System.out.println(Arrays.toString(buf));
      Response response = Response.parseFrom(buf);

      assertEquals(Code.OK, response.getCode());
      assertEquals("", response.getMessage());
      Health health = Health.parseFrom(response.getPayload());

      assertEquals(Health.Status.READY, health.getStatus());
    }
    server.stop();
  }
}
