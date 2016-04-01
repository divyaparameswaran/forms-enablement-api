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
public class FilingDetailsTransformTest {

  ITransformConfig config;

  @Before
  public void setUp(){
    config = new TransformConfig();
  }

  @Test
  public void addFilingDetailsToXml() throws Exception {
    // xml
    String xml_path = "src/test/resources/form.xml";
    String xml = TestHelper.getStringFromFile(xml_path);

    // package json
    String package_path = "src/test/resources/package.json";
    String package_string = TestHelper.getStringFromFile(package_path);
    JSONObject package_json = new JSONObject(package_string);

    // meta json
    String meta_path = "src/test/resources/meta.json";
    String meta_string = TestHelper.getStringFromFile(meta_path);
    JSONObject meta_json = new JSONObject(meta_string);

    FilingDetailsTransform transform = new FilingDetailsTransform(config, xml, package_json, meta_json);
    String output = transform.getXml();

    Assert.assertThat(output, CoreMatchers.containsString("<submissionNumber>038-496949</submissionNumber>"));
    Assert.assertThat(output, CoreMatchers.containsString("<packageIdentifier>some identifier</packageIdentifier>"));
    Assert.assertThat(output, CoreMatchers.containsString("<packageCount>3</packageCount>"));
  }
}
