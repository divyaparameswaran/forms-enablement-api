package com.ch.conversion.transformations;

import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TestTransformationConfig;
import com.ch.conversion.config.TransformConfig;
import com.ch.helpers.TestHelper;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class MetaDataTransformTest {

  ITransformConfig config;

  @Before
  public void setUp() {
    config = new TransformConfig();
  }

  @Test
  public void addMetaDataToRootXmlElement() throws Exception {
    // xml
    String xml_path = "src/test/resources/form.xml";
    String xml = TestHelper.getStringFromFile(xml_path);

    // meta json
    String meta_path = "src/test/resources/meta.json";
    String meta_string = TestHelper.getStringFromFile(meta_path);
    JSONObject meta_json = new JSONObject(meta_string);

    MetaDataTransform transform = new MetaDataTransform(config, xml, meta_json);
    String output = transform.getXml();

    Assert.assertThat(output, CoreMatchers.containsString("<?xml version='1.0'?><form type='SH08' version=''>"));
  }
}
