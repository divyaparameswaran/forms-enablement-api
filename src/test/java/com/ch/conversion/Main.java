package com.ch.conversion;

import com.ch.conversion.builders.JsonBuilder;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TestTransformationConfig;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elliott.jenkins on 01/04/2016.
 */
public class Main {
  public static void main(String[] args) throws Exception {
    //  HttpEntity entity = createMultiPart();
    //  String request = EntityUtils.toString(entity);
    //  System.out.println(request);

    testMultipleForms();
  }

  private static String getJSONFromFile(String path) throws IOException {
    File file = new File(path);
    return FileUtils.readFileToString(file);
  }

  private static void testMultipleForms() throws Exception {
    // get sample json from file
    String packageJsonPath = "src/test/resources/package.json";
    String packageJson = getJSONFromFile(packageJsonPath);
    System.out.println("- package json -");
    System.out.println(packageJson);

    // multiple forms
    List<String> formsJson = new ArrayList<String>();
    for (int i = 0; i < 5; i++) {
      String formJsonPath = "src/test/resources/form.json";
      String formJson = getJSONFromFile(formJsonPath);
      System.out.println("- form json " + i + " -");
      System.out.println(formJson);
      formsJson.add(formJson);
    }

    // output
    ITransformConfig config = new TestTransformationConfig();
    JsonBuilder builder = new JsonBuilder(config, packageJson, formsJson);
    String output = builder.getJson();
    System.out.println("- chips json -");
    System.out.println(output);
  }
}
