package com.ch.exception;

import javax.ws.rs.WebApplicationException;

/**
 * Created by Aaron.Witter on 02/08/2016.
 */
public class PresenterAuthenticationException extends WebApplicationException {

  private final String id;
  private final String auth;

  public PresenterAuthenticationException(String id, String auth) {
    this.id = id;
    this.auth = auth;
  }

  @Override
  public String getMessage() {
    return String.format("Error authentication the presenter using the given credentials: id: %s, auth: %s",
        id, auth);
  }
}