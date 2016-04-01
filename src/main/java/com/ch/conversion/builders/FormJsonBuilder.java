package com.ch.conversion.builders;


import com.ch.conversion.config.ITransformConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class FormJsonBuilder {

  private final ITransformConfig config;
  private final JSONObject pack;
  private final JSONObject meta;
  private final JSONObject form;
  private final JSONArray attachments;

  /**
   * Builder to convert one form into the required json object.
   *
   * @param config      json and xml
   * @param packageJson package data json
   * @param formJson    form data json
   */
  public FormJsonBuilder(ITransformConfig config, String packageJson, String formJson) {
    this.config = config;
    // package json object
    this.pack = new JSONObject(packageJson);
    // form json object
    JSONObject form = new JSONObject(formJson);
    this.meta = form.getJSONObject(config.getMetaPropertyNameIn());
    this.form = form.getJSONObject(config.getFormPropertyNameIn());
    this.attachments = form.getJSONArray(config.getAttachmentsPropertyNameIn());
  }

  /**
   * Get the json object for a single form.
   *
   * @return transformed json
   * @throws Exception error
   */
  public JSONObject getJson() throws Exception {
    // 1. create empty json object
    JSONObject output = new JSONObject();

    // 2. add barcode
    Object barcode = getFormBarcode();
    output.put(config.getBarcodePropertyNameOut(), barcode);

    // 3. add attachments
    output.put(config.getAttachmentsPropertyNameOut(), attachments);

    // 4. transform package and form json into base64 xml
    String xml = getFormXML();
    output.put(config.getXmlPropertyNameOut(), xml);
    return output;
  }

  private Object getFormBarcode() {
    try {
      JSONObject details = form.getJSONObject(config.getFilingDetailsPropertyNameIn());
      return details.get(config.getBarcodePropertyNameIn());
    } catch (JSONException ex) {
      // TODO: how to handle when barcode can't be found
      return "N/A";
    }
  }

  private String getFormXML() throws Exception {
    FormXmlBuilder builder = new FormXmlBuilder(config, pack, meta, form);
    return builder.getXML();
  }
}
