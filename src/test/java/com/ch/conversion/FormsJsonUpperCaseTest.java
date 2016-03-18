package com.ch.conversion;

import com.ch.model.FormsJson;
import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


/**
 * Created by elliott.jenkins on 18/03/2016.
 */
public class FormsJsonUpperCaseTest {

  @Test
  public void convertsToUpperCase() throws IOException {
    String input_path = "src/test/test_json/lower_case.json";
    String input = getJSONFromFile(input_path);
    FormsJson json = new FormsJson(input);
    String result = json.getConvertedString();
    Assert.assertThat(result, CoreMatchers.containsString("HELLO"));
    Assert.assertThat(result, CoreMatchers.containsString("HELLO NESTED"));
    Assert.assertThat(result, CoreMatchers.containsString("HELLO ARRAY STRING"));
    Assert.assertThat(result, CoreMatchers.containsString("HELLO ARRAY NESTED 1"));
    Assert.assertThat(result, CoreMatchers.containsString("HELLO ARRAY NESTED 2"));
  }

  private String getJSONFromFile(String path) throws IOException {
    File file = new File(path);
    return FileUtils.readFileToString(file);
  }
}
