package com.ch.conversion.builders;


import com.ch.conversion.config.ITransformConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class FormJsonBuilder {

  private ITransformConfig config;
  private JSONObject pack;
  private JSONObject meta;
  private JSONObject form;
  private JSONArray attachments;

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
