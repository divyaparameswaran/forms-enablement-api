package com.ch.resources;

import static com.ch.service.LoggingService.LoggingLevel.INFO;
import static com.ch.service.LoggingService.tag;

import com.ch.application.FormsServiceApplication;
import com.ch.client.ClientHelper;
import com.ch.client.PresenterHelper;
import com.ch.configuration.CompaniesHouseConfiguration;
import com.ch.model.PresenterAuthResponse;
import com.ch.service.LoggingService;
import com.codahale.metrics.Timer;
import io.dropwizard.auth.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 01/04/2016.
 */
@Path("/presenterauth")
@Produces(MediaType.APPLICATION_JSON)
public class PresenterAuthResource {
  private static final Timer timer = FormsServiceApplication.registry.timer("PresenterAuthResource");

  private final PresenterHelper presenterHelper;

  public PresenterAuthResource(PresenterHelper presenterHelper) {
    this.presenterHelper = presenterHelper;
  }

  /**
   * Checks whether valid account exists for forms presenter.
   *
   * @param presenterId   id number of the forms presenter.
   * @param presenterAuth auth code of the forms presenter.
   * @return response.
   */
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPresenterAuth(@Auth @QueryParam("id") String presenterId, @QueryParam("auth") String presenterAuth) {

    final Timer.Context context = timer.time();

    try {
      LoggingService.log(tag, INFO, "Presenter Auth request from Salesforce: " + presenterId,
          PresenterAuthResource.class);

      // Get auth response from entity
      PresenterAuthResponse presenterAuthResponse = presenterHelper.getPresenterResponse(presenterId, presenterAuth);

      if (presenterAuthResponse.getPresenterAccountNumber() != null) {

        LoggingService.log(tag, INFO, "Response from Presenter Auth Service " + presenterAuthResponse, PresenterAuthResource.class);

        return Response.ok().build();
      }

      return Response.status(401).build();

    } finally {
      context.stop();
    }
  }
}