package com.wwttr.health;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;

public class HealthHandlers extends Api.HealthService {

  public void getHealth(RpcController controller, Api.GetHealthRequest request, RpcCallback<Api.Health> callback) {
    Api.Health.Builder builder = Api.Health.newBuilder();
    builder.setStatus(Api.Health.Status.READY);
    callback.run(builder.build());
  }

  public void streamHealth(RpcController controller, Api.GetHealthRequest request, RpcCallback<Api.Health> callback) {
    while (!controller.isCanceled()) {
      Api.Health.Builder builder = Api.Health.newBuilder();
      builder.setStatus(Api.Health.Status.READY);
      callback.run(builder.build());
      try {
        Thread.sleep(1000);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
