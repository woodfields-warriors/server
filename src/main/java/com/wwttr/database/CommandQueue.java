package com.wwttr.database;

import java.util.stream.Stream;
import java.util.LinkedList;
import java.util.List;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CommandQueue<T> {

  List<BlockingQueue<T>> subscriptions = new LinkedList<BlockingQueue<T>>();
  List<T> elements = new LinkedList<T>();

  public Stream<T> subscribe() {
    synchronized (this) {
      BlockingQueue<T> q = new LinkedBlockingQueue<T>(elements);
      subscriptions.add(q);

    return Stream
      .generate(() -> {
        // Retry until not interrupted
        while (true) {
          try {
            return q.take();
          }
          catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      })
      .onClose(() -> {
        synchronized (this) {
          subscriptions.remove(q);
        }
      });
    }
  }

  public void publish(T t) {
    synchronized (this) {
      elements.add(t);
      for (BlockingQueue<T> q: subscriptions) {
        q.add(t);
      }
    }
  }
}
