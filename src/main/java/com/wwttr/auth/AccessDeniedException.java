package com.wwttr.auth;

class AccessDeniedException extends Exception {
  public AccessDeniedException(String message) {
    super(message);
  }
}
