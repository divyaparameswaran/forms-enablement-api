package com.ch.helpers;

import com.ch.application.FormsServiceApplication;
import com.ch.configuration.FormsServiceConfiguration;
import com.ch.conversion.builders.JsonBuilder;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TransformConfig;
import com.ch.conversion.helpers.QueueHelper;
import com.ch.model.FormsPackage;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron.Witter on 18/07/2016.
 */
public class QueueHelperTest extends TestHelper {

  @ClassRule
  public static final DropwizardAppRule<FormsServiceConfiguration> RULE =
    new DropwizardAppRule<>(FormsServiceApplication.class, ResourceHelpers.resourceFilePath
      ("test-configuration.yml"));
  ITransformConfig config;
  private MongoHelper helper;

  @Before
  public void setUp() {
    config = new TransformConfig();
    MongoHelper.init(RULE.getConfiguration());
    helper = MongoHelper.getInstance();
    helper.dropCollection("forms");
    helper.dropCollection("packages");
  }

  @Test
  public void shouldReturnCompletePackageWithFiveForms() throws IOException {

    // valid package data
    String package_string = getStringFromFile(FIVE_PACKAGE_JSON_PATH);
    // valid forms
    String valid = getStringFromFile(FORM_ALL_JSON_PATH);
    List<String> valid_forms = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      valid_forms.add(valid);
    }
    // builder
    FormsPackage formsPackage = new JsonBuilder(config, package_string, valid_forms).getTransformedPackage();

    helper.storeFormsPackage(formsPackage);

    TransformConfig config = new TransformConfig();

    QueueHelper helper = new QueueHelper(config);

    List<JSONObject> forms = helper.getCompletePackagesByStatus(1, "PENDING");

    Assert.assertTrue(forms.size() == 1);
    Assert.assertTrue(forms.get(0).getInt("count") == 5);
    Assert.assertTrue(forms.get(0).getJSONArray(config.getFormsPropertyNameOut()).length() == 5);
  }

  @Test
  public void shouldReturnCompletePackageWithThreeForms() throws IOException {

    // valid package data
    String package_string = getStringFromFile(THREE_PACKAGE_JSON_PATH);
    // valid forms
    String valid = getStringFromFile(FORM_ALL_JSON_PATH);
    List<String> valid_forms = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      valid_forms.add(valid);
    }
    // builder
    FormsPackage formsPackage = new JsonBuilder(config, package_string, valid_forms).getTransformedPackage();

    helper.storeFormsPackage(formsPackage);

    TransformConfig config = new TransformConfig();

    QueueHelper helper = new QueueHelper(config);

    List<JSONObject> forms = helper.getCompletePackagesById(12435);

    Assert.assertTrue(forms.size() == 1);
    Assert.assertTrue(forms.get(0).getInt("count") == 3);
    Assert.assertTrue(forms.get(0).getJSONArray(config.getFormsPropertyNameOut()).length() == 3);
  }

}
