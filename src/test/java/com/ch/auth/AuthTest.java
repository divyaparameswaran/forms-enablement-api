package com.ch.auth;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.ch.application.FormsServiceApplication;
import com.ch.configuration.CompaniesHouseConfiguration;
import com.ch.configuration.FormsServiceConfiguration;
import com.ch.configuration.SalesforceConfiguration;
import com.ch.cucumber.FormServiceTestSuiteIT;
import com.ch.model.FormsApiUser;
import com.ch.resources.FormSubmissionResource;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.apache.http.annotation.NotThreadSafe;
import org.glassfish.grizzly.compression.lzma.impl.Base;
import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Eliot Stock on 08/03/2016. See:
 * http://www.dropwizard.io/0.9.2/docs/manual/auth.html#testing-protected-resources
 */
public class AuthTest {

  @ClassRule
  public static final DropwizardAppRule<FormsServiceConfiguration> RULE =
      new DropwizardAppRule<>(FormsServiceApplication.class, ResourceHelpers.resourceFilePath("test-configuration.yml"));

  @Test
  public void responseIs401WhenWeDontSendAnAuthHeader() {
    Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client1");

    final Response response = client.target(
                String.format("http://localhost:%d/upload", RULE.getLocalPort()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                    .get();

    Assert.assertEquals("Wrong HTTP status code.", 401, response.getStatus());
  }

  @Test
  public void responseIs401WhenWeSendTheWrongAuthHeader() {
    Client client2 = new JerseyClientBuilder(RULE.getEnvironment()).build("test client2");

    final Response response = client2.target(
        String.format("http://localhost:%d/upload", RULE.getLocalPort()))
        .request(MediaType.APPLICATION_JSON_TYPE)
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
        String.format("http://localhost:%d/upload", RULE.getLocalPort()))
        .request(MediaType.APPLICATION_JSON_TYPE)
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
        String.format("http://localhost:%d/upload", RULE.getLocalPort()))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header("Authorization", "Basic " + encode)
        .get();

    Assert.assertEquals("Correct HTTP status code.", 200, response.getStatus());
  }

}

