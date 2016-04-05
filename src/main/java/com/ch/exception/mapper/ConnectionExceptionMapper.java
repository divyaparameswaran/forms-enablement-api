package com.ch.exception.mapper;

import com.ch.exception.ConnectionException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class ConnectionExceptionMapper implements ExceptionMapper<ConnectionException> {
  @Override
  public Response toResponse(ConnectionException ex) {
    return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
  }
}
