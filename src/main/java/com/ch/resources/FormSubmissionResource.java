package com.ch.resources;

import com.ch.conversion.JsonToXmlConverter;
import com.ch.model.HelloWorld;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.dropwizard.auth.Auth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.json.JSONException;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 07/03/2016.
 */
@Path("/submission")
public class FormSubmissionResource {

  private static final Logger log = LogManager.getLogger(FormSubmissionResource.class);

  @GET
  public String getMessage(@Auth String message) {
    return "hello world";
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_XML)
  public HelloWorld getXml(@Auth HelloWorld xml1) throws JsonProcessingException {
    log.info("JSON from Salesforce " + xml1);
    return xml1;
  }

  /**
   * Resource to test posting a multi-part form to dropwizard.
   *
   * @param form            salesforce form json
   * @param file            pdf
   * @param fileDisposition pdf meta
   * @return multi-part with xml and file name of file
   */
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response submit(@Auth
                         @FormDataParam("form") String form,
                         @FormDataParam("file") InputStream file,
                         @FormDataParam("file") FormDataContentDisposition fileDisposition) throws JSONException {

    log.info("JSON from Salesforce: " + form);
    log.info("File from Salesforce: " + fileDisposition.getFileName());
    String xml = JsonToXmlConverter.getInstance().toXML(form);
    log.info("Produced XML: " + xml);

    MultiPart multiPartEntity = new MultiPart()
        .bodyPart(new StreamDataBodyPart("file", file, fileDisposition.getFileName()))
        .bodyPart(new BodyPart("form", MediaType.APPLICATION_XML_TYPE).entity(xml));

    return Response.ok().entity(multiPartEntity).type(MediaType.MULTIPART_FORM_DATA_TYPE).build();
  }
}