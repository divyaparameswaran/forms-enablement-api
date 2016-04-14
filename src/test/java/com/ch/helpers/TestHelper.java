package com.ch.helpers;

import com.ch.cucumber.FormServiceTestSuiteIT;
import io.dropwizard.client.JerseyClientBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.ws.rs.client.Client;

/**
 * Created by elliott.jenkins on 01/04/2016.
 */
public class TestHelper {

  public static final String EXAMPLE_JSON_PATH = "example.json";
  public static final String INVALID_JSON_PATH = "invalid.json";
  public static final String VALID_JSON_PATH = "valid.json";
  public static final String PACKAGE_JSON_PATH = "package.json";
  public static final String FORM_JSON_PATH = "form_all.json";
  public static final String INVALID_FORM_JSON_PATH = "invalid_form_all.json";
  public static final String FORM_XML_PATH = "form.xml";
  public static final String CONVERTED_FORM_XML_PATH = "converted_form.xml";
  public static final String META_PATH = "meta.json";
  public static final String EXAMPLE_XML_PATH = "example.xml";
  public static final String LOWERCASE_JSON_PATH = "lower_case.json";
  public static final String DATE_JSON_PATH = "date.json";
  public static final String RESPONSE_JSON_PATH = "response.json";

  public static final Client client = new JerseyClientBuilder(FormServiceTestSuiteIT.RULE.getEnvironment())
      .using(FormServiceTestSuiteIT.RULE.getConfiguration().getJerseyClientConfiguration())
      .build("test client");

  public String getStringFromFile(String filename) throws IOException {
    String fileAsString;
    try {
      File file = new File("src/test/resources/" + filename);
      fileAsString = FileUtils.readFileToString(file);
    } catch (FileNotFoundException ex) {
      return IOUtils.toString(getClass().getClassLoader().getResourceAsStream(filename));
    }
    return fileAsString;
  }
}