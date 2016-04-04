package com.ch.conversion.transformations;


import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.helpers.XmlHelper;
import org.json.JSONObject;
import org.w3c.dom.Document;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class FilingDetailsTransform {

  private final ITransformConfig config;
  private final XmlHelper helper;

  private final Document xml;
  private final String xmlLocation;

  private final JSONObject pack;
  private final JSONObject meta;

  /**
   * Apply transforms to filing details.
   *
   * @param config json and xml
   * @param xml    xml to add to
   * @param pack   package data
   * @param meta   form meta data
   * @throws Exception error
   */
  public FilingDetailsTransform(ITransformConfig config, String xml, JSONObject pack, JSONObject meta) throws Exception {
    this.config = config;
    this.pack = pack;
    this.meta = meta;
    helper = XmlHelper.getInstance();
    // add elements to filing details element
    xmlLocation = config.getFilingDetailsPropertyNameIn();

    this.xml = helper.createDocumentFromString(xml);
  }

  /**
   * Add required elements to filing details.
   *
   * @return xml
   * @throws Exception error
   */
  public String getXml() throws Exception {
    // 1. submission number
    helper.addJsonValueAsElementToXml(xml, meta, xmlLocation,
        config.getSubmissionNumberPropertyNameIn(),
        config.getSubmissionNumberElementNameOut());

    // 2. package identifier
    helper.addJsonValueAsElementToXml(xml, pack, xmlLocation,
        config.getPackageIdentifierPropertyNameIn(),
        config.getPackageIdentifierElementNameOut());

    // 3. package count
    helper.addJsonValueAsElementToXml(xml, pack, xmlLocation,
        config.getPackageCountPropertyNameIn(),
        config.getPackageCountElementNameOut());

    return helper.getStringFromDocument(xml);
  }
}
