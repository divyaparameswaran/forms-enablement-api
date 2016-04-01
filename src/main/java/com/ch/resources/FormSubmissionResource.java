package com.ch.resources;

import com.ch.application.FormsServiceApplication;
import com.ch.configuration.CompaniesHouseConfiguration;
import com.codahale.metrics.Timer;
import io.dropwizard.auth.Auth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.json.JSONException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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
   * Resource to test posting a multi-part form to dropwizard.
   *
   * @param form salesforce form json
   * @param file pdf
   * @return multi-part with xml and file name of file
   */
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response submit(@Auth
                         @FormDataParam("form") String form,
                         @FormDataParam("file") String file) throws JSONException {
    final Timer.Context context = timer.time();
    try {
      // TODO: loop parts
      String json = "abc";

      // create multipart - file and xml
      MultiPart multiPart = getMultipartForm(json);

      // post to CHIPS
      // TODO: currently posting to Chips Stub, needs to point at real endpoint
      final WebTarget target = client.target(configuration.getApiUrl());
      // return response from CHIPS
      Response response = target.request(MediaType.MULTIPART_FORM_DATA_TYPE)
          .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
      log.info("Response from CHIPS: " + response.toString());
      return response;

    } finally {
      context.stop();
    }
  }

  /**
   * Creates multipart object - json
   *
   * @param json forms json
   * @return Multipart object.
   */
  private MultiPart getMultipartForm(String json) {
    final MultiPart multiPart = new MultiPart()
        .bodyPart(new FormDataBodyPart("forms", json, MediaType.APPLICATION_JSON_TYPE));
    return multiPart;
  }
}