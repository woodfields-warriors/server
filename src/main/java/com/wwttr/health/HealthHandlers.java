package com.wwttr.health;

import com.google.protobuf.RpcController;

public class HealthHandlers implements Api.HealthService.BlockingInterface {

  public Api.Health getHealth(RpcController controller, Api.GetHealthRequest request) {
    Api.Health.Builder builder = Api.Health.newBuilder();
    builder.setStatus(Api.Health.Status.READY);
    return builder.build();
  }
}
