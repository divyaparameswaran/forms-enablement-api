package com.ch.exception.mapper;

import com.ch.exception.BarcodeNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by Aaron.Witter on 01/04/2016.
 */
public class BarcodeNotFoundExceptionMapper implements ExceptionMapper<BarcodeNotFoundException> {
  @Override
  public Response toResponse(BarcodeNotFoundException exception) {
    return Response.status(Response.Status.PARTIAL_CONTENT).entity(exception).build();
  }
}
