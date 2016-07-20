package com.ch.exception.mapper;

import static com.ch.service.LoggingService.LoggingLevel.ERROR;
import static com.ch.service.LoggingService.tag;

import com.ch.exception.DatabaseException;
import com.ch.service.LoggingService;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by Aaron.Witter on 20/07/2016.
 */
public class DatabaseExceptionMapper  implements ExceptionMapper<DatabaseException> {

  /**
   * Returns an HTTP response containing the appropriate error message.
   *
   * @param exception - the DatabaseException that was thrown
   * @return an appropriate HTTP error response
   */
  public Response toResponse(DatabaseException exception) {
    LoggingService.log(tag, ERROR, exception.getMessage(), DatabaseException.class);
    String error = ExceptionHelper.getInstance().getJsonError(exception);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
      .entity(error)
      .build();
  }
}