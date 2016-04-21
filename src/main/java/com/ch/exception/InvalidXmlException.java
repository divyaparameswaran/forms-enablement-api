package com.ch.exception;

import javax.ws.rs.WebApplicationException;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class InvalidXmlException extends WebApplicationException {

  private final String formType;
  private final String schema;

  public InvalidXmlException(String formType, String schema) {
    this.formType = formType;
    this.schema = schema;
  }

  @Override
  public String getMessage() {
    return String.format("XML for form type %s, could not be validated against schema %s",
        formType, schema);
  }
}
