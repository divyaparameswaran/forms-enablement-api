package com.ch.resources;

import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.json.JSONObject;
import org.json.XML;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by elliott.jenkins on 11/03/2016.
 */
@Path("/multi-part")
public class MultiPartResource {

  /**
   * Resource to test posting a multi-part form to dropwizard.
   * @param form salesforce form json
   * @param file pdf
   * @param fileDisposition pdf meta
   * @return multi-part with xml and file name of file
   */
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response postForm(@Auth
      @FormDataParam("form") String form,
      @FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) {

    // json to xml
    JSONObject json = new JSONObject(form);
    String xml = XML.toString(json);

    // create multi-part
    // xml and file name
    MultiPart multiPartEntity = new MultiPart()
        .bodyPart(new BodyPart(MediaType.TEXT_PLAIN_TYPE).entity(fileDisposition.getFileName()))
        .bodyPart(new BodyPart(MediaType.APPLICATION_XML_TYPE).entity(xml));

    return Response.ok().entity(multiPartEntity).type(MediaType.MULTIPART_FORM_DATA_TYPE).build();
  }
}
