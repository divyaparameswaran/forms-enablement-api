package com.ch.resources;

import com.ch.model.HelloWorld;
import io.dropwizard.auth.Auth;

import javax.annotation.security.PermitAll;
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

  @GET
  @PermitAll
  public String getMessage(@Auth String message) {
    return "hello world";
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_XML)
  public HelloWorld getXml(@Auth HelloWorld xml) {
    return xml;
  }
}