package com.ch.conversion.validation;

import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TestTransformationConfig;
import com.ch.exception.XsdValidationException;
import com.ch.helpers.TestHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by elliott.jenkins on 21/04/2016.
 */
public class XmlValidatorTest extends TestHelper {
  ITransformConfig config;

  @Before
  public void setUp() {
    config = new TestTransformationConfig();
  }

  @Test
  public void validXmlAgainstXsd() throws Exception {
    String valid = getStringFromFile(VALID_DS01_XML_PATH);
    XmlValidator validator = new XmlValidator(config, valid);
    validator.validate();
  }

  @Test(expected = XsdValidationException.class)
  public void invalidXmlAgainstXsd() throws Exception {
    String invalid = getStringFromFile(INVALID_DS01_XML_PATH);
    XmlValidator validator = new XmlValidator(config, invalid);
    validator.validate();
  }
}
