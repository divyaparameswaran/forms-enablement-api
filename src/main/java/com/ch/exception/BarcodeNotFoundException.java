package com.ch.exception;

import javax.ws.rs.WebApplicationException;

/**
 * Created by Aaron.Witter on 01/04/2016.
 */
public class BarcodeNotFoundException extends WebApplicationException {
  private final Exception exception;

  public BarcodeNotFoundException(Exception exception) {
    this.exception = exception;
  }

  public Exception getException() {
    return exception;
  }
}
