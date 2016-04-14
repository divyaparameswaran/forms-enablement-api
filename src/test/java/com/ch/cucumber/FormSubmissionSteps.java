package com.ch.cucumber;

import com.ch.configuration.CompaniesHouseConfiguration;
import com.ch.helpers.TestHelper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.dropwizard.client.JerseyClientBuilder;
import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.Assert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 10/04/2016.
 */
public class FormSubmissionSteps extends TestHelper {
  private Response responseOne;
  private Response responseTwo;

  @Given("^I submit a valid form to the forms API using the correct credentials$")
  public void i_submit_a_valid_form_to_the_forms_API_using_the_correct_credentials() throws Throwable {

    Client client = new JerseyClientBuilder(FormServiceTestSuiteIT.RULE.getEnvironment())
        .using(FormServiceTestSuiteIT.RULE.getConfiguration().getJerseyClientConfiguration())
        .build("submission client 1");

    CompaniesHouseConfiguration config = FormServiceTestSuiteIT.RULE.getConfiguration().getCompaniesHouseConfiguration();
    String encode = Base64.encodeAsString(config.getName() + ":" + config.getSecret());
    String url = String.format("http://localhost:%d/submission", FormServiceTestSuiteIT.RULE.getLocalPort());

    FormDataMultiPart multiPart = new FormDataMultiPart();

    String formdata = getStringFromFile(FORM_JSON_PATH);
    String packagemetadata = getStringFromFile(PACKAGE_JSON_PATH);

    multiPart.field("form1", formdata, MediaType.TEXT_PLAIN_TYPE);
    multiPart.field("packagemetadata", packagemetadata, MediaType.TEXT_PLAIN_TYPE);

    responseOne = client.target(url)
        .register(MultiPartFeature.class)
        .request()
        .header("Authorization", "Basic " + encode)
        .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
  }

  @Then("^then I should receive a response from CHIPS$")
  public void then_I_should_receive_a_response_from_CHIPS() throws Throwable {
    Assert.assertEquals("Correct HTTP status code.", 202, responseOne.getStatus());
  }

  @Given("^I submit a invalid form to the forms API using the correct credentials$")
  public void i_submit_a_invalid_form_to_the_forms_API_using_the_correct_credentials() throws Throwable {

    Client client = new JerseyClientBuilder(FormServiceTestSuiteIT.RULE.getEnvironment())
        .using(FormServiceTestSuiteIT.RULE.getConfiguration().getJerseyClientConfiguration())
        .build("response client 2");

    CompaniesHouseConfiguration config = FormServiceTestSuiteIT.RULE.getConfiguration().getCompaniesHouseConfiguration();
    String encode = Base64.encodeAsString(config.getName() + ":" + config.getSecret());
    String url = String.format("http://localhost:%d/submission", FormServiceTestSuiteIT.RULE.getLocalPort());

    FormDataMultiPart multiPart = new FormDataMultiPart();

    String formdata = getStringFromFile(INVALID_FORM_JSON_PATH);
    String packagemetadata = getStringFromFile(PACKAGE_JSON_PATH);

    multiPart.field("form1", formdata, MediaType.TEXT_PLAIN_TYPE);
    multiPart.field("packagemetadata", packagemetadata, MediaType.TEXT_PLAIN_TYPE);

    responseTwo = client.target(url)
        .register(MultiPartFeature.class)
        .request()
        .header("Authorization", "Basic " + encode)
        .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA_TYPE));
  }

  @Then("^then I should receive an error message from the API$")
  public void then_I_should_receive_an_error_message_from_the_API() throws Throwable {
    Assert.assertEquals("Correct HTTP status code.", 500, responseTwo.getStatus());
  }
}
