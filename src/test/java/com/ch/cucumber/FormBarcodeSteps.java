package com.ch.cucumber;

import com.ch.configuration.CompaniesHouseConfiguration;
import com.ch.helpers.TestHelper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.dropwizard.client.JerseyClientBuilder;
import org.glassfish.jersey.internal.util.Base64;
import org.junit.Assert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * Created by elliott.jenkins on 14/04/2016.
 */
// TODO: look at refactoring
public class FormBarcodeSteps extends TestHelper {
  private Response responseOne;
  private Response responseTwo;

  @Given("^I submit a valid date to the forms API using the correct credentials$")
  public void i_submit_a_valid_date_to_the_forms_API_using_the_correct_credentials() throws Throwable {

    Client client = new JerseyClientBuilder(FormServiceTestSuiteIT.RULE.getEnvironment())
        .using(FormServiceTestSuiteIT.RULE.getConfiguration().getJerseyClientConfiguration())
        .build("barcode client 1");

    CompaniesHouseConfiguration config = FormServiceTestSuiteIT.RULE.getConfiguration().getCompaniesHouseConfiguration();
    String encode = Base64.encodeAsString(config.getName() + ":" + config.getSecret());
    String url = String.format("http://localhost:%d/barcode", FormServiceTestSuiteIT.RULE.getLocalPort());

    String date = getStringFromFile(DATE_JSON_PATH);
    responseOne = client.target(url)
        .request()
        .header("Authorization", "Basic " + encode)
        .post(Entity.json(date));
  }

  @Then("^I should receive a response from CHIPS barcode service$")
  public void i_should_receive_a_response_from_CHIPS_barcode_service() throws Throwable {
    Assert.assertEquals("Correct HTTP status code.", 202, responseOne.getStatus());
  }

  @Given("^I submit an invalid media type to the barcode forms API using the correct credentials$")
  public void i_submit_an_invalid_media_type_to_the_barcode_forms_API_using_the_correct_credentials() throws Throwable {

    Client client = new JerseyClientBuilder(FormServiceTestSuiteIT.RULE.getEnvironment())
        .using(FormServiceTestSuiteIT.RULE.getConfiguration().getJerseyClientConfiguration())
        .build("barcode client 2");

    CompaniesHouseConfiguration config = FormServiceTestSuiteIT.RULE.getConfiguration().getCompaniesHouseConfiguration();
    String encode = Base64.encodeAsString(config.getName() + ":" + config.getSecret());
    String url = String.format("http://localhost:%d/barcode", FormServiceTestSuiteIT.RULE.getLocalPort());

    String date = getStringFromFile(DATE_JSON_PATH);
    // wrong media type
    responseTwo = client.target(url)
        .request()
        .header("Authorization", "Basic " + encode)
        .post(Entity.text(date));
  }

  @Then("^I should receive an invalid media type error from the barcode api$")
  public void i_should_receive_an_invalid_media_type_error_from_the_barcode_api() throws Throwable {
    Assert.assertEquals("Correct HTTP status code.", 415, responseTwo.getStatus());
  }
}

