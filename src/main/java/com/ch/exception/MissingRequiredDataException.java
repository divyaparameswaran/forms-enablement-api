package com.ch.exception;

import javax.ws.rs.WebApplicationException;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class MissingRequiredDataException extends WebApplicationException {

  private final String message;

  public MissingRequiredDataException(Exception exception, String message) {
    super(exception);
    this.message = message;
  }

  public MissingRequiredDataException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return String.format("Missing required data for transformation to occur. Missing: %s",
        message);
  }
}
