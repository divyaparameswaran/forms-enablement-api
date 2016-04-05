package com.ch.exception.mapper;

import org.json.JSONObject;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception ex) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(getJsonError(ex.getMessage()))
        .build();
  }

  private String getJsonError(String message) {
    JSONObject error = new JSONObject();
    error.put("error", message);
    return error.toString();
  }
}
