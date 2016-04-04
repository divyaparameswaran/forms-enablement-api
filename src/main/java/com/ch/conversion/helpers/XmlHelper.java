package com.ch.conversion.helpers;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public final class XmlHelper {

  private static XmlHelper instance = new XmlHelper();

  private XmlHelper() {
  }

  public static XmlHelper getInstance() {
    return instance;
  }

  /**
   * Create an xml document from a string.
   *
   * @param xml xml string
   * @return xml document
   * @throws Exception error parsing string to document
   */
  public Document createDocumentFromString(String xml) throws Exception {
    // create dom from xml string
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    return db.parse(new InputSource(new StringReader(xml)));
  }

  /**
   * Add a json property as an element to an xml document.
   *
   * @param xml          xml document
   * @param xmlLocation  where to add element
   * @param json         json object to get property from
   * @param propertyName json property to add
   * @param elementName  name of the xml element to add
   */
  public void addJsonValueAsElementToXml(Document xml, JSONObject json, String xmlLocation, String propertyName, String
      elementName) {
    // get where to add element
    NodeList nodes = xml.getElementsByTagName(xmlLocation);
    try {
      // get value from json
      Object value = json.get(propertyName);
      // insert element
      insert(xml, nodes, elementName, value);
    } catch (JSONException ex) {
      // TODO: how to handle when propertyName can't be found
      // create empty element
      insert(xml, nodes, elementName, null);
    }
  }

  /**
   * Add an element with a value to xml document.
   *
   * @param xml          document
   * @param xmlLocation  where to add element in xml
   * @param elementName  name of element to add
   * @param elementValue value of the element
   */
  public void addElementToXml(Document xml, String xmlLocation, String elementName, String elementValue) {
    // get where to add element
    NodeList nodes = xml.getElementsByTagName(xmlLocation);
    insert(xml, nodes, elementName, elementValue);
  }

  /**
   * Insert xml element into document.
   *
   * @param xml          xml document
   * @param nodes        where to insert
   * @param elementName  xml element name
   * @param elementValue xml element value
   */
  public void insert(Document xml, NodeList nodes, String elementName, Object elementValue) {
    // create xml element e.g. <name>value</name>
    Element element = xml.createElement(elementName);
    if (elementValue != null) {
      Text text = xml.createTextNode(elementValue.toString());
      element.appendChild(text);
    }
    // append to nodes
    Node node = nodes.item(0);
    node.appendChild(element);
  }

  /**
   * Create the string for an xml attribute for a json property.
   *
   * @param json          json
   * @param propertyName  json property
   * @param attributeName xml attribute name
   * @return xml attribute string
   */
  public String createAttribute(JSONObject json, String propertyName, String attributeName) {
    // e.g. attributeName='value'
    // if value is null: attributeName=''
    StringBuilder builder = new StringBuilder();
    builder
        .append(" ")
        .append(attributeName)
        .append("=")
        .append("'");

    try {
      // get property from json object
      Object value = json.get(propertyName);
      if (value != null && !value.toString().equals("null")) {
        builder.append(value);
      }
      builder.append("'");
      return builder.toString();

    } catch (JSONException ex) {
      // TODO: how to handle when value isn't present
      // return empty attribute
      builder.append("'");
      return builder.toString();
    }
  }

  /**
   * Convert an xml document into a string.
   *
   * @param doc xml document
   * @return string representation
   * @throws Exception error
   */
  public String getStringFromDocument(Document doc) throws Exception {
    DOMSource domSource = new DOMSource(doc);
    StringWriter writer = new StringWriter();
    StreamResult result = new StreamResult(writer);
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.transform(domSource, result);
    return writer.toString();
  }
}
