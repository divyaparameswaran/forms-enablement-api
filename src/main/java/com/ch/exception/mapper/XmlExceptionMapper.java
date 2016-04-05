package com.ch.exception.mapper;

import com.ch.exception.XmlException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class XmlExceptionMapper implements ExceptionMapper<XmlException> {
  @Override
  public Response toResponse(XmlException ex) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
  }
}
