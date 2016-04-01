package com.ch.conversion.helpers;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class XmlHelper {

  private static XmlHelper instance = new XmlHelper();

  private XmlHelper() {
  }

  public static XmlHelper getInstance() {
    return instance;
  }

  public void addElement(Document xml, NodeList nodes, JSONObject json, String propertyName, String elementName) {
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

  public String createAttribute(JSONObject json, String propertyName, Object attributeName) {
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
