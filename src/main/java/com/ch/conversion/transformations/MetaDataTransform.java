package com.ch.conversion.transformations;


import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.helpers.XmlHelper;
import org.json.JSONObject;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class MetaDataTransform {

  private ITransformConfig config;
  private XmlHelper helper;

  private String xml;
  private JSONObject meta;

  public MetaDataTransform(ITransformConfig config, String xml, JSONObject meta) {
    this.config = config;
    this.xml = xml;
    this.meta = meta;

    helper = XmlHelper.getInstance();
  }

  public String getXml() {
    StringBuilder builder = new StringBuilder();
    builder
        .append("<?xml version='1.0'?>")
        .append(getOpenTag())
        .append(xml)
        .append(getCloseTag());
    return builder.toString();
  }

  private String getOpenTag() {
    StringBuilder builder = new StringBuilder();
    builder
        .append("<" + config.getRootElementNameOut())
        .append(helper.createAttribute(meta, config.getFormTypePropertyNameIn(), config.getFormTypeAttributeNameOut()))
        .append(helper.createAttribute(meta, config.getFormVersionPropertyNameIn(), config.getFormVersionAttributeNameOut()))
        .append(">");
    return builder.toString();
  }

  private String getCloseTag() {
    return "</" + config.getRootElementNameOut() + ">";
  }
}
