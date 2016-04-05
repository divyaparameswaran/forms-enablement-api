package com.ch.exception.mapper;

import com.ch.exception.MissingRequiredDataException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class MissingRequiredDataExceptionMapper implements ExceptionMapper<MissingRequiredDataException> {
  @Override
  public Response toResponse(MissingRequiredDataException ex) {
    return Response.status(Response.Status.PARTIAL_CONTENT).entity(ex.getMessage()).build();
  }
}
