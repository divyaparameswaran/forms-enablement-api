package com.ch.conversion;

import com.ch.model.FormsJson;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Created by elliott.jenkins on 15/03/2016.
 */
public class FormsJsonToXmlTest {

  @Test
  public void convertsValidJsonToXml() throws IOException {
    String path = "src/test/test_json/valid.json";
    String valid = getJSONFromFile(path);
    FormsJson form = new FormsJson(valid);
    String result = form.toXML();
    Assert.assertNotNull(result);
  }

  @Test(expected=JSONException.class)
  public void throwsJSONExceptionWithInvalidJson() throws IOException {
    String path = "src/test/test_json/invalid.json";
    String invalid = getJSONFromFile(path);
    FormsJson form = new FormsJson(invalid);
    form.toXML();
  }

  @Test(expected=JSONException.class)
  public void throwsJSONExceptionWhenInputNotJson() {
    String input = "abc";
    FormsJson form = new FormsJson(input);
    form.toXML();
  }

  private String getJSONFromFile(String path) throws IOException {
    File file = new File(path);
    return FileUtils.readFileToString(file);
  }
}
