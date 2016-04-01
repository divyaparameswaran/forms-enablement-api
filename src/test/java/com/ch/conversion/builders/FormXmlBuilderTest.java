package com.ch.conversion.builders;

import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TestTransformationConfig;
import org.junit.Before;

import java.io.IOException;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class FormXmlBuilderTest {

  FormXmlBuilder builder;

  @Before
  public void setUp() throws IOException {
    // config
    ITransformConfig config = new TestTransformationConfig();
  }
}
