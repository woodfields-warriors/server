package com.wwttr.server;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;

import com.google.protobuf.ByteString;

import java.net.HttpURLConnection;
import java.net.URL;

import com.wwttr.api.Request;
import com.wwttr.api.Response;
import com.wwttr.api.Code;
import com.wwttr.health.Api.Health;
import com.wwttr.health.HealthHandlers;

public class ServerTest {

  @Test public void testServer() throws IOException, InterruptedException {
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

    Response response = Response.parseFrom(con.getInputStream());

    assertEquals(Code.OK, response.getCode());
    assertEquals("", response.getMessage());
    Health health = Health.parseFrom(response.getPayload());

    assertEquals(Health.Status.READY, health.getStatus());

    server.stop();
  }
}
