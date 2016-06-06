package com.ch.client;

import com.ch.configuration.SalesforceConfiguration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public final class ClientHelper {

  private final Client client;

  public ClientHelper(Client client) {
    this.client = client;
  }

  /**
   * Send json to the desired url.
   *
   * @param url  destination
   * @param json json to send
   * @return response from url
   */
  public Response postJson(String url, String json) {
    final WebTarget target = client.target(url);
    return target.request().post(Entity.json(json));
  }

  /**
   * Patches json verdict to Salesforce.
   *
   * @param clientUrl   salesforce client url
   * @param accessToken access token
   * @param verdict     json verdict from chips
   * @return response
   */
  public Response postJson(String clientUrl, String accessToken, String verdict) {
    final WebTarget target = client.target(clientUrl);
    return target.request(MediaType.APPLICATION_XML_TYPE)
        .header("Authorization", "Bearer " + accessToken).method("PATCH", Entity.json(verdict));
  }

  /**
   * Gets token from Salesforce oauth client.
   *
   * @param configuration Salesforce configuration object
   * @return response from url
   */
  public Response getToken(SalesforceConfiguration configuration) {
    final WebTarget target = client.target(configuration.getAuthUrl())
        .queryParam("grant_type", configuration.getAuthGrantType())
        .queryParam("client_id", configuration.getAuthId())
        .queryParam("client_secret", configuration.getAuthSecret())
        .queryParam("username", configuration.getAuthUsername())
        .queryParam("password", configuration.getAuthPassword());
    return target.request().post(Entity.json(null));
  }
}


