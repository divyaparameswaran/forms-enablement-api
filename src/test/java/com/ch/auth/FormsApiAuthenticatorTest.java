package com.ch.auth;

import com.ch.application.FormsServiceApplication;
import com.ch.configuration.FormsServiceConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.internal.util.Base64;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Eliot Stock on 08/03/2016. See:
 * http://www.dropwizard.io/0.9.2/docs/manual/auth.html#testing-protected-resources
 * new client instances are used for each test to mitigate https://github.com/dropwizard/dropwizard/issues/1258
 */
public class FormsApiAuthenticatorTest {

  @ClassRule
  public static final DropwizardAppRule<FormsServiceConfiguration> RULE =
      new DropwizardAppRule<>(FormsServiceApplication.class, ResourceHelpers.resourceFilePath("test-configuration.yml"));

  @Test
  public void responseIs401WhenWeDontSendAnAuthHeader() {
    Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client1");

    final Response response = client.target(
        String.format("http://localhost:%d/auth", RULE.getLocalPort()))
        .request()
        .get();

    Assert.assertEquals("Wrong HTTP status code.", 401, response.getStatus());
  }

  @Test
  public void responseIs401WhenWeSendTheWrongAuthHeader() {
    Client client2 = new JerseyClientBuilder(RULE.getEnvironment()).build("test client2");

    final Response response = client2.target(
        String.format("http://localhost:%d/auth", RULE.getLocalPort()))
        .request()
        .header("Authorization", "Basic WRONG")
        .get();

    Assert.assertEquals("Wrong HTTP status code.", 401, response.getStatus());
  }


  @Test
  public void responseIs200WhenWeSendTheRightAuthHeaderForSalesforce() {
    Client client3 = new JerseyClientBuilder(RULE.getEnvironment()).build("test client3");

    String encode = Base64.encodeAsString(RULE.getConfiguration().getSalesforceConfiguration().getName()
        + ":" + RULE.getConfiguration().getSalesforceConfiguration().getSecret());

    final Response response = client3.target(
        String.format("http://localhost:%d/auth", RULE.getLocalPort()))
        .request()
        .header("Authorization", "Basic " + encode)
        .get();

    Assert.assertEquals("Correct HTTP status code.", 200, response.getStatus());
  }

  @Test
  public void responseIs200WhenWeSendTheRightAuthHeaderForCompaniesHouse() {
    Client client4 = new JerseyClientBuilder(RULE.getEnvironment()).build("test client4");

    String encode = Base64.encodeAsString(RULE.getConfiguration().getCompaniesHouseConfiguration().getName()
        + ":" + RULE.getConfiguration().getCompaniesHouseConfiguration().getSecret());

    final Response response = client4.target(
        String.format("http://localhost:%d/auth", RULE.getLocalPort()))
        .request()
        .header("Authorization", "Basic " + encode)
        .get();

    Assert.assertEquals("Correct HTTP status code.", 200, response.getStatus());
  }

}

