package com.ch.exception;

import javax.ws.rs.WebApplicationException;

/**
 * Created by Aaron.Witter on 20/07/2016.
 */
public class DatabaseException extends WebApplicationException {

  private final String message;

  public DatabaseException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return String.format("Unable to process your %s due to a database error. Please "
      + "contact your administrator.", message);
  }
}
