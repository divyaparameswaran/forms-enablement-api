package com.ch.conversion.validation;


import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.helpers.XmlHelper;
import com.ch.exception.XsdValidationException;
import org.w3c.dom.Document;

import java.io.File;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * Created by elliott.jenkins on 21/04/2016.
 */
public class XmlValidator {

  private final ITransformConfig config;
  private final String xml;

  public XmlValidator(ITransformConfig config, String xml) {
    this.config = config;
    this.xml = xml;
  }

  /**
   * Validate a forms xml against its schema.
   * Throws XsdValidationException if not valid.
   */
  public void validate() {
    // get schema for form
    String formType = getFormType();
    String schemaFileName = getSchemaFileName(formType);
    String schemaPath = getSchemaPath(schemaFileName);

    try {
      // build the schema
      SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      File schemaFile = new File(schemaPath);
      Schema schema = factory.newSchema(schemaFile);
      Validator validator = schema.newValidator();
      // create a source from xml string
      Source source = new StreamSource(new StringReader(xml));

      // check input
      validator.validate(source);

    } catch (Exception ex) {
      // exception thrown when invalid
      // TODO: do we want to catch all exceptions or handle specific ones
      throw new XsdValidationException(ex, formType, schemaFileName);
    }
  }

  private String getFormType() {
    // create xml document
    XmlHelper helper = XmlHelper.getInstance();
    Document document = helper.createDocumentFromString(xml);
    // get type attribute from form element
    // e.g. <form type="DS01">
    return helper.getAttributeValueFromDocument(document, config.getRootElementNameOut(), config.getFormTypeAttributeNameOut());
  }

  private String getSchemaFileName(String formType) {
    return String.format("%s.xsd", formType);
  }

  private String getSchemaPath(String schemaFileName) {
    return config.getSchemasLocation() + schemaFileName;
  }
}



