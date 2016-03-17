package com.ch.resources;

import com.ch.conversion.JsonToXmlConverter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by elliott.jenkins on 17/03/2016.
 */
@Path("/conversion")
public class ConversionResource {

  private static final Logger log = LogManager.getLogger(FormSubmissionResource.class);

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response convert(@FormDataParam("form") String form) {
    log.info("JSON received: " + form);
    String xml = JsonToXmlConverter.getInstance().toXML(form);
    log.info("Produced XML: " + xml);
    return Response.ok().entity(xml).build();
  }
}