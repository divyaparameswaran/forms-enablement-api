package com.ch.resources;

import com.ch.application.FormsServiceApplication;
import com.ch.configuration.SalesforceConfiguration;
import com.codahale.metrics.Timer;
import io.dropwizard.auth.Auth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 09/03/2016.
 */
@Path("/response")
public class FormResponseResource {

  private static final Logger log = LogManager.getLogger(FormResponseResource.class);
  private static final Timer timer = FormsServiceApplication.registry.timer("FormResponseResource");

  private final Client client;
  private final SalesforceConfiguration configuration;

  public FormResponseResource(Client client, SalesforceConfiguration configuration) {
    this.client = client;
    this.configuration = configuration;
  }

  /**
   * Resource to post response from CHIPS to Salesforce.
   *
   * @param json JSON to forward to Salesforce
   * @return What to return to CHIPS to be confirmed
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response submit(@Auth
                         String json) {
    final Timer.Context context = timer.time();
    try {
      log.info("Received JSON: " + json);

      // post to Salesforce
      final WebTarget target = client.target(configuration.getApiUrl());
      // TODO: send as multipart?
      MultiPart multiPart = new MultiPart()
          .bodyPart(new FormDataBodyPart("response", json, MediaType.APPLICATION_JSON_TYPE));
      Response response = target.request(MediaType.MULTIPART_FORM_DATA_TYPE)
          .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
      log.info("Response from Salesforce: " + response.toString());

      // return response from Salesforce
      return response;

    } catch (Exception ex) {
      // TODO: how to handle errors?
      return Response.ok(ex.toString()).build();

    } finally {
      context.stop();
    }
  }
}