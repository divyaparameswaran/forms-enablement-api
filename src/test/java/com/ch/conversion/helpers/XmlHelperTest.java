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
public class XmlHelperTest extends TestHelper {

  XmlHelper helper;

  @Before
  public void setUp() {
    helper = XmlHelper.getInstance();
  }

  @Test
  public void createDocumentFromString() throws Exception {
    String xml_string = TestHelper.getStringFromFile(EXAMPLE_XML_PATH);
    Document xml = helper.createDocumentFromString(xml_string);
    Assert.assertNotNull(xml);
  }

  @Test
  public void addJsonPropertyToXml() throws Exception {
    // xml
    Document xml = createDocumentFromPath(EXAMPLE_XML_PATH);

    // json
    String json_string = TestHelper.getStringFromFile(EXAMPLE_JSON_PATH);
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
  public void addElementToXml() throws Exception {
    // xml
    Document xml = createDocumentFromPath(EXAMPLE_XML_PATH);
    // element
    String elementName = "test";
    String elementValue = "value";
    // location
    String location = "root";
    helper.addElementToXml(xml, location, elementName, elementValue);

    String modified = helper.getStringFromDocument(xml);
    Assert.assertThat(modified, CoreMatchers.containsString("<test>value</test>"));
  }

  @Test
  public void createAttributeFromJson() throws Exception {
    String json_string = TestHelper.getStringFromFile(EXAMPLE_JSON_PATH);
    JSONObject json = new JSONObject(json_string);
    String json_property = "new_child";
    String attribute_name = "new_attribute";

    String attribute = helper.createAttributeFromJson(json, json_property, attribute_name);
    Assert.assertEquals(" new_attribute='I should be added'", attribute);
  }

  @Test
  public void getStringFromDocument() throws Exception {
    Document xml = createDocumentFromPath(EXAMPLE_XML_PATH);
    String output = helper.getStringFromDocument(xml);
    Assert.assertNotNull(output);
  }

  private Document createDocumentFromPath(String path) throws Exception {
    String xml_string = TestHelper.getStringFromFile(path);
    return helper.createDocumentFromString(xml_string);
  }
}
