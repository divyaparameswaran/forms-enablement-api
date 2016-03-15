package com.ch.resources;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/chips")
public class ChipsStubResource {

  private static final Logger log = LogManager.getLogger(ChipsStubResource.class);

  /**
   * Resource to test posting a multi-part form to chips.
   *
   * @param form            salesforce form xml
   * @param file            pdf
   * @param fileDisposition pdf meta
   * @return fake status from chips
   */
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response getResponse(@FormDataParam("form") String form,
                              @FormDataParam("file") InputStream file,
                              @FormDataParam("file") FormDataContentDisposition fileDisposition) {
    log.info("Received XML: " + form);
    log.info("Received File: " + fileDisposition.getFileName());
    return Response.ok().entity("<data contentType=\"text/plain\" contentLength=\"1\"><![CDATA[0]]></data>").build();
  }
}
