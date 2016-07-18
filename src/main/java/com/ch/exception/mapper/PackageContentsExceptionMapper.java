package com.ch.exception.mapper;

import static com.ch.service.LoggingService.LoggingLevel.ERROR;
import static com.ch.service.LoggingService.tag;

import com.ch.exception.PackageContentsException;
import com.ch.service.LoggingService;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by Aaron.Witter on 15/07/2016.
 */
public class PackageContentsExceptionMapper implements ExceptionMapper<PackageContentsException> {

  /**
   * Returns an HTTP response containing the appropriate error message.
   *
   * @param exception - the PackageContentsException that was thrown
   * @return an appropriate HTTP error response
   */
  public Response toResponse(PackageContentsException exception) {
    LoggingService.log(tag, ERROR, exception.getMessage(), PackageContentsException.class);
    String error = ExceptionHelper.getInstance().getJsonError(exception);
    return Response.status(Response.Status.BAD_REQUEST)
      .entity(error)
      .build();
  }
}

