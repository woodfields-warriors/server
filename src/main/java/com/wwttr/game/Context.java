package com.wwttr.game;

import java.util.List;
import java.util.LinkedList;

public class Context {

  private boolean cancelled = false;
  private List<CancelledCallback> callbacks = new LinkedList<CancelledCallback>();

  public void cancel() {
    cancelled = true;
    for (CancelledCallback c : callbacks) {
      c.cancelled();
    }
  }
  public boolean isCanceled() {
    return cancelled;
  }
  public void addCallback(CancelledCallback callback) {

  }
}
