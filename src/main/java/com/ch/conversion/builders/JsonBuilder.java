package com.ch.conversion.builders;


import com.ch.conversion.config.ITransformConfig;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class JsonBuilder {

  private final ITransformConfig config;
  private final String packageJson;
  private final List<String> formsJson;

  /**
   * Builder to create the json object for multiple forms.
   *
   * @param config json and xml
   * @param packageJson package data
   * @param formsJson list of forms json (untransformed)
   */
  public JsonBuilder(ITransformConfig config, String packageJson, List<String> formsJson) {
    this.config = config;
    this.packageJson = packageJson;
    this.formsJson = formsJson;
  }

  /**
   * Get the json object for multiple forms.
   *
   * @return json
   * @throws Exception error
   */
  public String getJson() throws Exception {
    // 1. create root JSON object
    JSONObject root = new JSONObject();

    // 2. create JSON array
    JSONArray array = new JSONArray();

    // 3. loop forms and transform
    for (String formJson : formsJson) {
      FormJsonBuilder builder = new FormJsonBuilder(config, packageJson, formJson);
      JSONObject object = builder.getJson();
      array.put(object);
    }

    // 4. add array to root
    root.put(config.getFormsPropertyNameOut(), array);
    return root.toString();
  }
}
