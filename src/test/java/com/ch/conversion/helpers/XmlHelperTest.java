package com.ch.conversion.helpers;

import com.ch.helpers.TestHelper;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class XmlHelperTest {

  XmlHelper helper;

  @Before
  public void setUp() {
    helper = XmlHelper.getInstance();
  }

  @Test
  public void createDocumentFromString() throws Exception {
    String xml_path = "src/test/resources/example.xml";
    String xml_string = TestHelper.getStringFromFile(xml_path);
    Document xml = helper.createDocumentFromString(xml_string);
    Assert.assertNotNull(xml);
  }

  @Test
  public void addJsonPropertyToXml() throws Exception {
    // xml
    String xml_path = "src/test/resources/example.xml";
    Document xml = createDocumentFromPath(xml_path);

    // json
    String json_path = "src/test/resources/example.json";
    String json_string = TestHelper.getStringFromFile(json_path);
    JSONObject json = new JSONObject(json_string);

    // where to add element
    String xml_location = "parent";
    // which property to add
    String property_to_add = "new_child";
    // what it should be called
    String element_to_add = "new_element";
    // add
    helper.addJsonValueAsElementToXml(xml, json, xml_location, property_to_add, element_to_add);

    // document should contain element with json property value
    String modified_xml = helper.getStringFromDocument(xml);
    Assert.assertThat(modified_xml, CoreMatchers.containsString("<new_element>I should be added</new_element>"));
  }

  @Test
  public void createAttributeFromJson() throws Exception {
    String json_path = "src/test/resources/example.json";
    String json_string = TestHelper.getStringFromFile(json_path);
    JSONObject json = new JSONObject(json_string);
    String json_property = "new_child";
    String attribute_name = "new_attribute";

    String attribute = helper.createAttribute(json, json_property, attribute_name);
    Assert.assertEquals(" new_attribute='I should be added'", attribute);
  }

  @Test
  public void getStringFromDocument() throws Exception {
    String xml_path = "src/test/resources/example.xml";
    Document xml = createDocumentFromPath(xml_path);
    String output = helper.getStringFromDocument(xml);
    Assert.assertNotNull(output);
  }

  private Document createDocumentFromPath(String path) throws Exception {
    String xml_string = TestHelper.getStringFromFile(path);
    return helper.createDocumentFromString(xml_string);
  }
}
