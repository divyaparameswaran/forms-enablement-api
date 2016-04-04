package com.ch.resources;

import com.ch.application.FormsServiceApplication;
import com.codahale.metrics.Timer;
import io.dropwizard.auth.Auth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 09/03/2016.
 */
@Path("/response")
public class FormResponseResource {

  private static final Logger log = LogManager.getLogger(FormResponseResource.class);
  private static final Timer timer = FormsServiceApplication.registry.timer("FormResponseResource");

  /**
   * Resource to post response from CHIPS to Salesforce.
   *
   * @param json JSON to forward to Salesforce
   * @return What to return to CHIPS to be confirmed
   */
  @POST
  public Response submit(@Auth
                             String json) {
    final Timer.Context context = timer.time();
    try {
      log.info("Received JSON: " + json);
      // TODO: forward JSON to a salesforce endpoint
      return Response.ok().build();

    } finally {
      context.stop();
    }
  }
}