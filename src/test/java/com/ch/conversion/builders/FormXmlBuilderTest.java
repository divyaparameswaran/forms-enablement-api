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
public class FormXmlBuilderTest {

  ITransformConfig config;

  @Before
  public void setUp() {
    config = new TestTransformationConfig();
  }

  @Test(expected = JSONException.class)
  public void throwsJSONExceptionWithInvalidJson() throws Exception {
    String path = "src/test/test_json/invalid.json";
    String invalid = TestHelper.getStringFromFile(path);
    JSONObject json = new JSONObject(invalid);
    FormXmlBuilder builder = new FormXmlBuilder(config, json, json, json);
    builder.getXML();
  }

  // TODO: is this the desired behaviour?
  @Test(expected = NullPointerException.class)
  public void throwsNullPointerWithValidJsonMissingRequiredData() throws Exception {
    String path = "src/test/test_json/valid.json";
    String valid = TestHelper.getStringFromFile(path);
    JSONObject json = new JSONObject(valid);
    FormXmlBuilder builder = new FormXmlBuilder(config, json, json, json);
    builder.getXML();
  }

  // TODO: what to assert?
  @Test
  public void createEncodedXmlForValidJson() throws Exception {
    FormXmlBuilder builder = getValidFormXmlBuilder();
    String xml = builder.getXML();
    Assert.assertNotNull(xml);
  }

  private FormXmlBuilder getValidFormXmlBuilder() throws Exception {
    // valid package data
    String package_path = "src/test/resources/package.json";
    String package_string = TestHelper.getStringFromFile(package_path);
    JSONObject package_json = new JSONObject(package_string);
    // valid meta data
    String meta_path = "src/test/resources/meta.json";
    String meta_string = TestHelper.getStringFromFile(meta_path);
    JSONObject meta_json = new JSONObject(meta_string);
    // valid form data
    String form_path = "src/test/resources/form.json";
    String form_string = TestHelper.getStringFromFile(form_path);
    JSONObject form_json = new JSONObject(form_string);
    // builder
    return new FormXmlBuilder(config, package_json, meta_json, form_json);
  }
}
