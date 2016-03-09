package com.ch.resources;

import com.ch.model.HelloWorld;

import javax.ws.rs.Consumes;
import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Aaron.Witter on 07/03/2016.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

  @GET
  @PermitAll
  public String getMessage() {
    return "Give me JSON, and I'll spit out XML.";
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_XML)
  public HelloWorld getXml(HelloWorld xml){
    return xml;
  }
}