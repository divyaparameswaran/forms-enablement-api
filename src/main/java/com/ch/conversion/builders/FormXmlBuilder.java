package com.ch.conversion.builders;


import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.transformations.FilingDetailsTransform;
import com.ch.conversion.transformations.MetaDataTransform;
import com.ch.conversion.transformations.UpperCaseTransform;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.json.XML;


/**
 * Created by elliott.jenkins on 30/03/2016.
 */
public class FormXmlBuilder {

  private ITransformConfig config;
  private JSONObject pack;
  private JSONObject meta;
  private JSONObject form;

  public FormXmlBuilder(ITransformConfig config, JSONObject pack, JSONObject meta, JSONObject form) {
    this.config = config;
    this.pack = pack;
    this.meta = meta;
    this.form = form;
  }

  public String getXML() throws Exception {
    // 1. convert form data strings to upper case in the json
    toUpperCase();

    // 2. convert the form json straight to xml
    String xml = toXml();

    // 3. add root element and meta data attributes to the xml
    String metaXml = addMetaData(xml);

    // 4. add extra filing details
    String filingDetailsXml = addFilingDetails(metaXml);

    // 5. base64 encode
    return encode(filingDetailsXml);
  }

  private void toUpperCase() {
    UpperCaseTransform.parentUpperCase(form);
  }

  private String toXml() {
    return XML.toString(form);
  }

  private String addMetaData(String xml) {
    MetaDataTransform transform = new MetaDataTransform(config, xml, meta);
    return transform.getXml();
  }

  private String addFilingDetails(String xml) throws Exception {
    FilingDetailsTransform transform = new FilingDetailsTransform(config, xml, pack, meta);
    return transform.getXml();
  }

  private String encode(String xml) {
    byte[] encoded = Base64.encodeBase64(xml.getBytes());
    return new String(encoded);
  }
}
