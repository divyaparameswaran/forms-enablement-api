package com.ch.exception.mapper;

import com.ch.exception.MissingRequiredDataException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * This is an implementation of ExceptionMapper which is mapped to an MissingRequiredDataException.
 * It will handle the exception and return an appropriate response.
 * Created by elliott.jenkins on 19/05/2016.
 */
public class MissingRequiredDataExceptionMapper implements ExceptionMapper<MissingRequiredDataException> {

  /**
   * Returns an HTTP response containing the appropriate error message.
   *
   * @param exception - the MissingRequiredDataException that was thrown
   * @return an appropriate HTTP error response
   */
  public Response toResponse(MissingRequiredDataException exception) {
    return Response.status(Response.Status.BAD_REQUEST)
        .header("Error", exception.getMessage())
        .build();
  }
}
