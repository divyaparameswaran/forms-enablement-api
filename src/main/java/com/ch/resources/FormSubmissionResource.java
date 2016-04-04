package com.ch.resources;

import com.ch.application.FormsServiceApplication;
import com.ch.configuration.CompaniesHouseConfiguration;
import com.ch.conversion.builders.JsonBuilder;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TransformConfig;
import com.codahale.metrics.Timer;
import io.dropwizard.auth.Auth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.json.JSONException;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 07/03/2016.
 */
@Path("/submission")
public class FormSubmissionResource {

  private static final Logger log = LogManager.getLogger(FormSubmissionResource.class);
  private static final Timer timer = FormsServiceApplication.registry.timer("FormSubmissionResource");

  private final Client client;
  private final CompaniesHouseConfiguration configuration;

  public FormSubmissionResource(Client client, CompaniesHouseConfiguration configuration) {
    this.client = client;
    this.configuration = configuration;
  }

  @GET
  public String getMessage(@Auth String message) {
    return "hello world";
  }

  /**
   * Resource to post forms from Salesforce to CHIPS.
   *
   * @return json with response from CHIPS
   */
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response submit(@Auth
                         FormDataMultiPart multi) {
    final Timer.Context context = timer.time();
    try {
      // convert input to json
      ITransformConfig config = new TransformConfig();
      JsonBuilder builder = new JsonBuilder(config, multi);
      String forms = builder.getJson();
      log.info("Transformation output: " + forms);

      // post to CHIPS
      final WebTarget target = client.target(configuration.getApiUrl());
      Response response = target.request().post(Entity.json(forms));
      log.info("Response from CHIPS: " + response.toString());

      // return response from CHIPS
      return response;

    } catch (Exception ex) {
      // TODO: handle when an error occurred
      return Response.serverError().entity(Entity.text(ex.toString())).build();

    } finally {
      context.stop();
    }
  }
}