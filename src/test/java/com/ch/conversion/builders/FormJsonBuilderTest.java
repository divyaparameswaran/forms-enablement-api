package com.ch.conversion.builders;


import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TestTransformationConfig;
import com.ch.helpers.TestHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class FormJsonBuilderTest {

  ITransformConfig config;
  @Before
  public void setUp() {
    config = new TestTransformationConfig();
  }

  @Test(expected=JSONException.class)
  public void throwsJSONExceptionWithInvalidJson() throws Exception {
    String path = "src/test/test_json/invalid.json";
    String invalid = TestHelper.getStringFromFile(path);
    FormJsonBuilder builder = new FormJsonBuilder(config, invalid, invalid);
    builder.getJson();
  }

  // TODO: is this the desired behaviour?
  @Test(expected=JSONException.class)
  public void throwsJSONExceptionWithValidJsonMissingRequiredData() throws Exception {
    String path = "src/test/test_json/valid.json";
    String valid = TestHelper.getStringFromFile(path);
    FormJsonBuilder builder = new FormJsonBuilder(config, valid, valid);
    builder.getJson();
  }

  // TODO: what to assert?
  @Test
  public void createJSONObjectForValidJson() throws Exception {
    FormJsonBuilder builder = getValidFormJsonBuilder();
    JSONObject json = builder.getJson();
    Assert.assertNotNull(json);
  }

  private FormJsonBuilder getValidFormJsonBuilder() throws Exception {
    // valid package data
    String package_path = "src/test/resources/package.json";
    String package_string = TestHelper.getStringFromFile(package_path);
    // valid form data
    String form_path = "src/test/resources/form_all.json";
    String form_string = TestHelper.getStringFromFile(form_path);
    // builder
    return new FormJsonBuilder(config, package_string, form_string);
  }
}
