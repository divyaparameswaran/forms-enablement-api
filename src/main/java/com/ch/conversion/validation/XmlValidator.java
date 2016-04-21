package com.ch.conversion.validation;

import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.helpers.MultiPartHelper;
import com.ch.exception.InvalidXmlException;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

/**
 * Created by elliott.jenkins on 21/04/2016.
 */
public class XmlValidator {

  private ITransformConfig config;
  private String xml;

  public XmlValidator(ITransformConfig config, String xml) {
    this.config = config;
    this.xml = xml;
  }

  public void validate() {
    String formType = "DS01";
    String schema = "DS01.xsd";
    boolean valid = false;
    if(!valid){
      throw new InvalidXmlException(formType, schema);
    }
  }
}



