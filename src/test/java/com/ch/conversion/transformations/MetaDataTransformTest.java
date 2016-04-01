package com.ch.conversion.transformations;

import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TestTransformationConfig;
import org.junit.Before;

import java.io.IOException;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class MetaDataTransformTest {

  MetaDataTransform transform;

  @Before
  public void setUp() throws IOException {
    // config
    ITransformConfig config = new TestTransformationConfig();
  }
}
