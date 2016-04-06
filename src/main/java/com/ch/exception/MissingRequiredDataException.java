package com.ch.exception;

import javax.ws.rs.WebApplicationException;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class MissingRequiredDataException extends WebApplicationException {

  private final String property;
  private final String parent;

  public MissingRequiredDataException(Exception exception, String property, String parent) {
    super(exception);
    this.property = property;
    this.parent = parent;
  }

  public MissingRequiredDataException(String property, String parent) {
    this.property = property;
    this.parent = parent;
  }

  @Override
  public String getMessage() {
    return String.format("Missing required data for transformation to occur. Missing: %s in %s",
        property, parent);
  }
}
