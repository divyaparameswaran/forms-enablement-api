package com.ch.resources;

import com.ch.model.FormsJson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by elliott.jenkins on 17/03/2016.
 */
@Path("/conversion")
public class ConversionResource {

  private static final Logger log = LogManager.getLogger(FormSubmissionResource.class);

  /**
   * Resource to test posting JSON and receive XML.
   * @param json json to convert
   * @return xml
   */
  @POST
  public Response convert(String json) {
    log.info("JSON received: " + json);
    FormsJson form = new FormsJson(json);
    String converted = form.getConvertedString();
    log.info("Converted JSON: " + converted);
    String xml = form.toXML();
    log.info("Produced XML: " + xml);
    return Response.ok().entity(xml).build();
  }
}