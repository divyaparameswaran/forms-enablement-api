package com.ch.resources;

import com.ch.model.DateReceived;
import io.dropwizard.auth.Auth;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 10/04/2016.
 */
@Path("test")
public class TestResource {

  @POST
  @Path("/submission")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getResponse(String forms) {
    return Response.status(202).entity(" {\r\n    \"barcode\": \"X52S4WYM\",\r\n      \"presenterDocumentReference\": \"\",\r\n  " +
        "    \"status\": 0\r\n  }").build();
  }

  @POST
  @Path("/barcode")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public JSONObject getBarcode(@Auth DateReceived dateAdded) {

    JSONObject obj = new JSONObject();
    obj.put("barcode", "KYTEST" + dateAdded.getDatereceived() + "XYZ");
    return obj;
  }


}
