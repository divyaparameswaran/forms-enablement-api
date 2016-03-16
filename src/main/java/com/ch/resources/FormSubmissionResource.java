package com.ch.resources;

import com.ch.configuration.CompaniesHouseConfiguration;
import com.ch.conversion.JsonToXmlConverter;
import io.dropwizard.auth.Auth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.json.JSONException;

import java.io.InputStream;

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

    // TODO: not sure this is needed
    // stop the input stream being closed
    // CloseShieldInputStream shieldInputStream = new CloseShieldInputStream(file);

    // convert json to xml
    // TODO: log in JsonToXmlConverter?
    log.info("JSON from Salesforce: " + form);
    log.info("File from Salesforce: " + fileDisposition.getFileName());
    String xml = JsonToXmlConverter.getInstance().toXML(form);
    log.info("Produced XML: " + xml);

    // create multipart - file and xml
    MultiPart multiPart = getMultipartForm(file, xml);

    // post to CHIPSgit stash
    
    // TODO: currently posting to ChipsStubResource, needs to point at real endpoint
    final WebTarget target = client.target(configuration.getApiUrl());
    // return response from CHIPS
    Response response = target.request(MediaType.MULTIPART_FORM_DATA_TYPE)
        .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
    log.info("Response from CHIPS: " + response.toString());
    return response;
  }

  /**
   * Creates multipart object - file and xml
   * @param file  uploaded file
   * @param xml  converted xml
   * @return Multipart object.
   */
  private MultiPart getMultipartForm(InputStream file, String xml){
    final MultiPart multiPart = new MultiPart()
        .bodyPart(new StreamDataBodyPart("file", file))
        .bodyPart(new FormDataBodyPart("form", xml, MediaType.APPLICATION_XML_TYPE));
    return multiPart;

  }
}