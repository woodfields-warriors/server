package com.wwttr.api;

public class ApiException extends Exception {

  private Code code;

  public ApiException(Code code, String message) {
    super(message);
    this.code = code;
  }

  public Code getCode() {
    return code;
  }
}
