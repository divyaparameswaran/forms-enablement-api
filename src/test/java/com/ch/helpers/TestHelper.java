package com.ch.helpers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by elliott.jenkins on 01/04/2016.
 */
public class TestHelper {
  public static final String EXAMPLE_JSON_PATH = "src/test/resources/example.json";
  public static final String INVALID_JSON_PATH = "src/test/test_json/invalid.json";
  public static final String VALID_JSON_PATH = "src/test/test_json/valid.json";
  public static final String PACKAGE_JSON_PATH = "src/test/resources/package.json";
  public static final String FORM_JSON_PATH = "src/test/resources/form_all.json";
  public static final String INVALID_FORM_JSON_PATH = "src/test/resources/invalid_form_all.json";
  public static final String FORM_XML_PATH = "src/test/resources/form.xml";
  public static final String CONVERTED_FORM_XML_PATH = "src/test/resources/converted_form.xml";
  public static final String META_PATH = "src/test/resources/meta.json";
  public static final String EXAMPLE_XML_PATH = "src/test/resources/example.xml";
  public static final String LOWERCASE_JSON_PATH = "src/test/test_json/lower_case.json";


  public static String getStringFromFile(String path) throws IOException {
    File file = new File(path);
    return FileUtils.readFileToString(file);
  }
}
