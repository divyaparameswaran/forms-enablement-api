package com.ch.factory;

import org.json.JSONObject;
import org.json.XML;

/**
 * Created by elliott.jenkins on 15/03/2016.
 */
public final class JsonToXmlConverter {

  private static JsonToXmlConverter instance = new JsonToXmlConverter();

  private JsonToXmlConverter() {
  }

  public static JsonToXmlConverter getInstance() {
    return instance;
  }

  public String toXML(String json) {
    JSONObject jsonObj = new JSONObject(json);
    return XML.toString(jsonObj);
  }
}
