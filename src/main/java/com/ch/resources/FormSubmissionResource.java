package com.ch.resources;

import com.ch.model.HelloWorld;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.dropwizard.auth.Auth;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Aaron.Witter on 07/03/2016.
 */
@Path("/upload")
@Produces(MediaType.APPLICATION_JSON)
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
}