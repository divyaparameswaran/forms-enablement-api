package com.ch.cucumber;

import com.ch.application.FormServiceConstants;
import com.ch.configuration.CompaniesHouseConfiguration;
import com.ch.configuration.FormsServiceConfiguration;
import com.ch.conversion.builders.JsonBuilder;
import com.ch.conversion.config.TransformConfig;
import com.ch.helpers.MongoHelper;
import com.ch.helpers.QueueHelper;
import com.ch.helpers.TestHelper;
import com.ch.model.FormStatus;
import com.ch.model.FormsPackage;
import com.ch.model.QueueRequest;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.internal.util.Base64;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * Created by Aaron.Witter on 19/07/2016.
 */
public class ActivationSteps extends TestHelper {
  private MongoHelper helper;
  private TransformConfig config = new TransformConfig();
  private DropwizardAppRule<FormsServiceConfiguration> rule = FormServiceTestSuiteIT.RULE;
  private QueueHelper queueHelper = new QueueHelper(config);
  private String packageOneString;
  private String packageTwoString;
  private String packageThreeString;



  @Before
  public void setUp() {
    config = new TransformConfig();
    MongoHelper.init(rule.getConfiguration());
    helper = MongoHelper.getInstance();
    helper.dropCollection("forms");
    helper.dropCollection("packages");
  }

  @Given("^The queue contains (\\d+) packages all pending$")
  public void the_queue_contains_packages_all_pending(int arg1) throws Throwable {

    // package one
    packageOneString = getStringFromFile(PACKAGE_JSON_PATH);
    // valid forms
    String valid = getStringFromFile(FORM_ALL_JSON_PATH);
    List<String> valid_forms = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      valid_forms.add(valid);
    }
    FormsPackage formsPackage = new JsonBuilder(config, packageOneString, valid_forms).getTransformedPackage();
    // insert package one into db
    helper.storeFormsPackage(formsPackage);

    // package two
    packageTwoString = getStringFromFile(SINGLE_PACKAGE_JSON_PATH);
    // valid forms
    String valid2 = getStringFromFile(FORM_ALL_JSON_PATH);
    List<String> valid_forms2 = new ArrayList<>();
    for (int i = 0; i < 1; i++) {
      valid_forms2.add(valid2);
    }
    FormsPackage formsPackage2 = new JsonBuilder(config, packageTwoString, valid_forms2).getTransformedPackage();
    // insert package two into db
    helper.storeFormsPackage(formsPackage2);

    // package three
    packageThreeString = getStringFromFile(FIVE_PACKAGE_JSON_PATH);
    // valid forms
    String valid3 = getStringFromFile(FORM_ALL_JSON_PATH);
    List<String> valid_forms3 = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      valid_forms3.add(valid3);
    }
    FormsPackage formsPackage3 = new JsonBuilder(config, packageThreeString, valid_forms3).getTransformedPackage();
    // insert package three into db
    helper.storeFormsPackage(formsPackage3);
  }

  @When("^I request (\\d+) pending packages$")
  public void i_request_pending_packages(int arg1) throws Throwable {
    Client client = new JerseyClientBuilder(rule.getEnvironment())
      .using(rule.getConfiguration().getJerseyClientConfiguration())
      .build("auth steps client 2");

    CompaniesHouseConfiguration config = rule.getConfiguration().getCompaniesHouseConfiguration();
    String encode = Base64.encodeAsString(config.getName() + ":" + config.getApiKey());
    String url = String.format("http://localhost:%d/activate", rule.getLocalPort());
    client.target(url)
      .request()
      .header("Authorization", "Basic " + encode)
      .post(Entity.entity(new QueueRequest(FormStatus.PENDING.toString().toUpperCase(), 2), MediaType.APPLICATION_JSON));
  }

  @Then("^Two packages status should be changed$")
  public void two_packages_status_should_be_changed() throws Throwable {
    List<JSONObject> packs = queueHelper.getCompletePackagesByStatus(FormStatus.SUCCESS.toString().toUpperCase(), 0);
    Assert.assertTrue(packs.size() == 2);

    JSONObject packageOne = packs.get(0);
    JSONObject packageTwo = packs.get(1);

    Assert.assertTrue(packageOne.getInt(FormServiceConstants.PACKAGE_IDENTIFIER_KEY) == (new JSONObject(packageOneString)
      .getInt(FormServiceConstants.PACKAGE_IDENTIFIER_KEY)));

    Assert.assertTrue(packageTwo.getInt(FormServiceConstants.PACKAGE_IDENTIFIER_KEY) == (new JSONObject(packageTwoString)
      .getInt(FormServiceConstants.PACKAGE_IDENTIFIER_KEY)));


  }
}
