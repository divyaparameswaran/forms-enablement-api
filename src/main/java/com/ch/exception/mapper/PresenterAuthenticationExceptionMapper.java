package com.ch.exception.mapper;

import static com.ch.service.LoggingService.LoggingLevel.ERROR;
import static com.ch.service.LoggingService.tag;

import com.ch.exception.PresenterAuthenticationException;
import com.ch.service.LoggingService;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by Aaron.Witter on 02/08/2016.
 */
public class PresenterAuthenticationExceptionMapper implements ExceptionMapper<PresenterAuthenticationException> {


  /**
   * Returns an HTTP response containing the appropriate error message.
   *
   * @param exception - the PresenterAuthenticationException that was thrown
   * @return an appropriate HTTP error response
   */
  @Override
  public Response toResponse(PresenterAuthenticationException exception) {
    LoggingService.log(tag, ERROR, exception.getMessage(), PresenterAuthenticationException.class);
    String error = ExceptionHelper.getInstance().getJsonError(exception);
    return Response.status(Response.Status.UNAUTHORIZED)
        .entity(error)
        .build();
  }
}
