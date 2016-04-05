package com.ch.conversion.helpers;

import com.ch.exception.MissingRequiredDataException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public final class JsonHelper {
  private static JsonHelper instance = new JsonHelper();

  private JsonHelper() {
  }

  public static JsonHelper getInstance() {
    return instance;
  }

  /**
   * Get a value from a Json Object.
   *
   * @param json json object
   * @param propertyName property to get
   * @return value of property
   * @throws MissingRequiredDataException property not in json object
   */
  public Object getValueFromJson(JSONObject json, String propertyName) throws MissingRequiredDataException {
    try {
      return json.get(propertyName);

    } catch (Exception ex) {
      throw new MissingRequiredDataException(ex, propertyName);
    }
  }

  /**
   * Get an object from a json object.
   *
   * @param json json object
   * @param propertyName property name of the object
   * @return json object
   * @throws MissingRequiredDataException property not in json object
   */
  public JSONObject getObjectFromJson(JSONObject json, String propertyName) throws MissingRequiredDataException {
    try {
      return json.getJSONObject(propertyName);

    } catch (Exception ex) {
      throw new MissingRequiredDataException(ex, propertyName);
    }
  }

  /**
   * Get an array from a json object.
   *
   * @param json json object
   * @param propertyName property name of object
   * @return json array
   * @throws MissingRequiredDataException property not in json object
   */
  public JSONArray getArrayFromJson(JSONObject json, String propertyName) throws MissingRequiredDataException {
    try {
      return json.getJSONArray(propertyName);

    } catch (Exception ex) {
      throw new MissingRequiredDataException(ex, propertyName);
    }
  }
}
