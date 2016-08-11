package com.ch.exception;

import javax.ws.rs.WebApplicationException;

/**
 * Created by Aaron.Witter on 20/07/2016.
 */
public class DuplicatePackageException extends WebApplicationException {

  private final String packageIdentifier;

  public DuplicatePackageException(String packageIdentifier) {
    this.packageIdentifier = packageIdentifier;
  }

  @Override
  public String getMessage() {
    return String.format("Unable to process your package %s as there is already a package with that identifier.",
      packageIdentifier);
  }
}
