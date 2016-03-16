package com.ch.resources;

import io.dropwizard.auth.Auth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 09/03/2016.
 */
@Path("/response")
public class FormResponseResource {

  private static final Logger log = LogManager.getLogger(FormResponseResource.class);

  /**
   * Resource to test posting accept from CHIPS to Salesforce.
   *
   * @param info Information to be confirmed
   * @return What to post to salesforce to be confirmed
   */
  @Path("/accept")
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response accept(@Auth
                         @FormDataParam("info") String info) {
    log.info("Received Info: " + info);
    return Response.ok().build();
  }

  /**
   * Resource to test posting reject from CHIPS to Salesforce.
   *
   * @param info Information to be confirmed
   * @return What to post to salesforce to be confirmed
   */
  @Path("/reject")
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response reject(@Auth
                         @FormDataParam("info") String info) {
    log.info("Received Info: " + info);
    return Response.ok().build();
  }
}