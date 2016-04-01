package com.ch.helpers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by elliott.jenkins on 01/04/2016.
 */
public class TestHelper {
  public static String getStringFromFile(String path) throws IOException {
    File file = new File(path);
    return FileUtils.readFileToString(file);
  }
}
