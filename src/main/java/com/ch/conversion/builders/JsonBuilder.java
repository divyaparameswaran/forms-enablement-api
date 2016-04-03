package com.ch.conversion.builders;


import com.ch.conversion.config.ITransformConfig;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class JsonBuilder {

  private final ITransformConfig config;
  private final String packageJson;
  private final List<String> formsJson;

  /**
   * Convert FormDataMultiPart.
   *
   * @param config json and xml
   * @param parts  the form parts
   */
  public JsonBuilder(ITransformConfig config, FormDataMultiPart parts) throws Exception {
    this.config = config;
    // TODO: look at moving handling the FormDataMultiPart to another class
    // parts
    FormDataBodyPart pack = null;
    List<FormDataBodyPart> forms = new ArrayList<>();
    // loop parts
    Map<String, List<FormDataBodyPart>> all = parts.getFields();
    for (Map.Entry<String, List<FormDataBodyPart>> entry : all.entrySet()) {
      // should only be one body part per entry
      FormDataBodyPart body = entry.getValue().get(0);
      if (body.getName().equals(config.getPackageMultiPartName())) {
        pack = body;
      } else {
        forms.add(body);
      }
    }
    // check we have the parts
    if (pack == null || forms.isEmpty()) {
      throw new Exception("Missing required valid FormDataBodyPart");
    }
    // get content
    this.packageJson = (String)pack.getEntity();
    this.formsJson = new ArrayList<>();
    for (FormDataBodyPart form : forms) {
      this.formsJson.add((String)form.getEntity());
    }
  }

  /**
   * Builder to create the json object for multiple forms.
   *
   * @param config      json and xml
   * @param packageJson package data
   * @param formsJson   list of forms json (untransformed)
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
