package com.wwttr.health;

import com.google.protobuf.RpcController;
import com.google.protobuf.RpcCallback;

public class HealthHandlers extends Api.HealthService {

  public void getHealth(RpcController controller, Api.GetHealthRequest request, RpcCallback<Api.Health> callback) {
    Api.Health.Builder builder = Api.Health.newBuilder();
    builder.setStatus(Api.Health.Status.READY);
    callback.run(builder.build());
  }
}
