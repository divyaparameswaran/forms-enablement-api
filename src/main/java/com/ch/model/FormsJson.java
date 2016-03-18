package com.ch.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.Iterator;
import java.util.Locale;

/**
 * Created by elliott.jenkins on 18/03/2016.
 */
public class FormsJson {
  private final JSONObject jsonObject;

  public FormsJson(String jsonString) throws JSONException {
    this.jsonObject = new JSONObject(jsonString);
  }

  public String getConvertedString() {
    convert();
    return jsonObject.toString();
  }

  public String toXML() {
    convert();
    return XML.toString(jsonObject);
  }

  private void convert() {
    // transform string properties to uppercase
    parentUpperCase(jsonObject);
  }

  private void parentUpperCase(JSONObject parent) {
    Iterator<String> keys = parent.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      Object child = parent.get(key);
      childUpperCase(key, parent, child);
    }
  }

  private void childUpperCase(String key, JSONObject parent, Object child) {
    if (child instanceof JSONObject) {
      JSONObject childJsonObject = ((JSONObject) child);
      parentUpperCase(childJsonObject);

    } else if (child instanceof JSONArray) {
      JSONArray array = ((JSONArray) child);
      for (Object item : array) {
        childUpperCase(key, parent, item);
      }

    } else if (child instanceof String) {
      String value = ((String) child);
      String upper = value.toUpperCase(Locale.ENGLISH);
      parent.put(key, upper);
    }
  }
}