package com.ch.resources;

import javax.ws.rs.GET;
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
  public String getMessage() {
    return "Give me JSON, and I'll spit out XML. In the future.";
  }
}