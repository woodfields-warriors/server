package com.wwttr.api;

public class ApiError extends Error {

  private Code code;

  public ApiError(Code code, String message) {
    super(message);
    this.code = code;
  }

  public Code getCode() {
    return code;
  }

  // public static final long serialVersionUID = 123988925;
}
