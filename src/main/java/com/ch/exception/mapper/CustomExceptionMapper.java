package com.ch.exception.mapper;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

  private static final Logger log = LogManager.getLogger(CustomExceptionMapper.class);

  @Override
  public Response toResponse(Exception ex) {
    log.debug(ex.getMessage(), ex);
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
