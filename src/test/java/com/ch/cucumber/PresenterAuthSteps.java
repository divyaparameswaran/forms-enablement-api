package com.ch.cucumber;

import com.ch.configuration.CompaniesHouseConfiguration;
import com.ch.configuration.FormsServiceConfiguration;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TransformConfig;
import com.ch.helpers.TestHelper;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.json.JSONObject;
import org.junit.Assert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 20/07/2016.
 */
public class PresenterAuthSteps extends TestHelper{

  private Response responseOne;
  private Response responseTwo;
  private Response responseThree;
  private Response responseFour;
  private String formEntity;
  private DropwizardAppRule<FormsServiceConfiguration> rule = FormServiceTestSuiteIT.RULE;
  private ITransformConfig transformConfig = new TransformConfig();
  public Client client = new JerseyClientBuilder(rule.getEnvironment())
      .using(rule.getConfiguration().getJerseyClientConfiguration())
      .build("presenter client 1");


  @Given("^I submit a valid credentials to the presenter auth endpoint$")
  public void i_submit_a_valid_credentials_to_the_presenter_auth_endpoint() throws Throwable {
//    Client client = new JerseyClientBuilder(rule.getEnvironment())
//        .using(rule.getConfiguration().getJerseyClientConfiguration())
//        .build("presenter client 1");

    CompaniesHouseConfiguration config = rule.getConfiguration().getCompaniesHouseConfiguration();
    String encode = Base64.encodeAsString(config.getName() + ":" + config.getApiKey());
    String url = String.format("http://localhost:%d/presenterauth?id=12423&auth=PSW52889885", rule.getLocalPort());

    responseOne = client.target(url)
        .request()
        .header("Authorization", "Basic " + encode)
        .get();
  }

  @Then("^I should receive a successful response from the presenter auth endpoint$")
  public void i_should_receive_a_successful_response_from_the_presenter_auth_endpoint() throws Throwable {
    Assert.assertTrue(responseOne.getStatus() == 200);
  }

  @Given("^I submit invalid credentials to the presenter auth endpoint$")
  public void i_submit_invalid_credentials_to_the_presenter_auth_endpoint() throws Throwable {
    CompaniesHouseConfiguration config = rule.getConfiguration().getCompaniesHouseConfiguration();
    String encode = Base64.encodeAsString(config.getName() + ":" + config.getApiKey());
    String url = String.format("http://localhost:%d/presenterauth?id=124823&auth=PSW52889885", rule.getLocalPort());

    responseTwo = client.target(url)
        .request()
        .header("Authorization", "Basic " + encode)
        .get();
  }

  @Then("^I should receive an unsuccessful response from the presenter auth endpoint$")
  public void i_should_receive_a_unsuccessful_response_from_the_presenter_auth_endpoint() throws Throwable {
    Assert.assertTrue(responseTwo.getStatus() == 401);
  }

  @Given("^I submit a package with valid presenter credentials$")
  public void i_submit_a_package_with_valid_presenter_credentials() throws Throwable {
    CompaniesHouseConfiguration config = rule.getConfiguration().getCompaniesHouseConfiguration();
    String encode = Base64.encodeAsString(config.getName() + ":" + config.getApiKey());
    String url = String.format("http://localhost:%d/submission", rule.getLocalPort());

    FormDataMultiPart multi = new FormDataMultiPart();
    // forms package data
    String pack = getStringFromFile(PACKAGE_JSON_PATH);
    multi.field(transformConfig.getPackageMultiPartName(), pack, MediaType.APPLICATION_JSON_TYPE);
    // form json
    String form = getStringFromFile(FORM_ALL_JSON_NO_ACC_NUMBER_PATH);
    multi.field("form1", form, MediaType.APPLICATION_JSON_TYPE);

    responseThree = client.target(url)
        .register(MultiPartFeature.class)
        .request()
        .header("Authorization", "Basic " + encode)
        .post(Entity.entity(multi, MediaType.MULTIPART_FORM_DATA_TYPE));

    formEntity = responseThree.readEntity(String.class);

  }

  @Then("^The forms with account payment should be given an account number$")
  public void the_forms_with_account_payment_should_be_given_an_account_number() throws Throwable {
    Assert.assertTrue(responseThree.getStatus() == 202);

    JSONObject formJson = new JSONObject(formEntity);

    String xml = formJson.getJSONArray(transformConfig.getFormsPropertyNameOut()).getJSONObject(0).get(transformConfig
        .getXmlPropertyNameOut()).toString();

    String decodedXml = decode(xml);

    Assert.assertTrue(decodedXml.contains("<accountNumber>123456789</accountNumber>"));
  }

  @Given("^I submit a package with invalid presenter credentials$")
  public void i_submit_a_package_with_invalid_presenter_credentials() throws Throwable {
    CompaniesHouseConfiguration config = rule.getConfiguration().getCompaniesHouseConfiguration();
    String encode = Base64.encodeAsString(config.getName() + ":" + config.getApiKey());
    String url = String.format("http://localhost:%d/submission", rule.getLocalPort());

    FormDataMultiPart multi = new FormDataMultiPart();
    // forms package data
    String pack = getStringFromFile(PACKAGE_INVALID_CREDENTIALS_JSON_PATH);
    multi.field(transformConfig.getPackageMultiPartName(), pack, MediaType.APPLICATION_JSON_TYPE);
    // form json
    String form = getStringFromFile(FORM_ALL_JSON_NO_ACC_NUMBER_PATH);
    multi.field("form1", form, MediaType.APPLICATION_JSON_TYPE);

    responseFour = client.target(url)
        .register(MultiPartFeature.class)
        .request()
        .header("Authorization", "Basic " + encode)
        .post(Entity.entity(multi, MediaType.MULTIPART_FORM_DATA_TYPE));
  }

  @Then("^An exception should be thrown and no submision should take place$")
  public void an_exception_should_be_thrown_and_no_submision_should_take_place() throws Throwable {
    Assert.assertTrue(responseFour.getStatus() == 400);
  }

  private String decode(String xml) {
    byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(xml.getBytes());
    return new String(decoded);
  }
}
