package com.ch.conversion.transformations;


import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.helpers.XmlHelper;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class FilingDetailsTransform {

  private ITransformConfig config;
  private XmlHelper helper;

  private Document xml;
  private NodeList nodes;

  private JSONObject pack;
  private JSONObject meta;

  public FilingDetailsTransform(ITransformConfig config, String xml, JSONObject pack, JSONObject meta) throws Exception {
    this.config = config;
    this.pack = pack;
    this.meta = meta;

    // create dom from xml string
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    this.xml = db.parse(new InputSource(new StringReader(xml)));

    // ensure filingDetails element is present
    nodes = this.xml.getElementsByTagName(config.getFilingDetailsPropertyNameIn());

    // Xml Helper
    helper = XmlHelper.getInstance();
  }

  public String getXml() throws Exception {
    // 1. submission number
    helper.addElement(xml, nodes, meta, config.getSubmissionNumberPropertyNameIn(), config.getSubmissionNumberElementNameOut());
    // 2. package identifier
    helper.addElement(xml, nodes, pack, config.getPackageIdentifierPropertyNameIn(), config.getPackageIdentifierElementNameOut());
    // 3. package count
    helper.addElement(xml, nodes, pack, config.getPackageCountPropertyNameIn(), config.getPackageCountElementNameOut());
    return helper.getStringFromDocument(xml);
  }
}
