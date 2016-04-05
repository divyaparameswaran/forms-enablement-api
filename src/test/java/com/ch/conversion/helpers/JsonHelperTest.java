package com.ch.conversion.helpers;

import static com.ch.helpers.TestHelper.FORM_JSON_PATH;

import com.ch.exception.MissingRequiredDataException;
import com.ch.helpers.TestHelper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class JsonHelperTest {
  JsonHelper helper;

  @Before
  public void setUp() {
    helper = JsonHelper.getInstance();
  }

  @Test(expected = MissingRequiredDataException.class)
  public void throwMissingRequiredDataExceptionWhenJsonDoesNotContainProperty() throws Exception {
    String json = TestHelper.getStringFromFile(FORM_JSON_PATH);
    JSONObject object = new JSONObject(json);
    helper.getObjectFromJson(object, "not-valid");
  }

  @Test
  public void getJsonObjectByPropertyName() throws Exception {
    String json = TestHelper.getStringFromFile(FORM_JSON_PATH);
    JSONObject object = new JSONObject(json);
    JSONObject result = helper.getObjectFromJson(object, "formdata");
    Assert.assertNotNull(result);
  }
}
