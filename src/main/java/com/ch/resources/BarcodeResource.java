package com.ch.resources;

import com.ch.configuration.CompaniesHouseConfiguration;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 01/04/2016.
 */
@Path("/barcode")
public class BarcodeResource {
//  private static final Logger log = LogManager.getLogger(BarcodeResource.class);
//  private static final Timer timer = FormsServiceApplication.registry.timer("BarcodeResource");

  private final Client client;
  private final CompaniesHouseConfiguration configuration;

  public BarcodeResource(Client client, CompaniesHouseConfiguration configuration) {
    this.client = client;
    this.configuration = configuration;
  }

  /**
   * Retireves unique barcode from CHIPS.
   * @param dateReceived datereceived json object.
   * @return response from chips.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBarcode(String dateReceived) {
    // post to CHIPS
    final WebTarget target = client.target(configuration.getBarcodeServiceUrl());
    // return response from CHIPS
    Response response = target.request().post(Entity.json(dateReceived));

    // TODO: format of response to be confirmed by SF dev
    return response;
  }
}
