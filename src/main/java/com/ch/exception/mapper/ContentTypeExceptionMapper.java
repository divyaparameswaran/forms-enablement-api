package com.ch.exception.mapper;

import com.ch.exception.ContentTypeException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class ContentTypeExceptionMapper implements ExceptionMapper<ContentTypeException> {
  @Override
  public Response toResponse(ContentTypeException ex) {
    return Response.status(Response.Status.NOT_ACCEPTABLE).entity(ex.getMessage()).build();
  }
}