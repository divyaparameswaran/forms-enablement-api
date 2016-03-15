package com.ch.conversion;

import com.ch.factory.JsonToXmlConverter;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Created by elliott.jenkins on 15/03/2016.
 */
public class JsonToXmlConverterTest {

    private JsonToXmlConverter converter;

    @Before
    public void setup() {
        converter = JsonToXmlConverter.getInstance();
    }

    @Test
    public void convertsValidJsonToXml() throws IOException {
        String path = "src/test/test_json/valid.json";
        String valid = getJSONFromFile(path);
        String result = converter.toXML(valid);
        Assert.assertNotNull(result);
    }

    @Test(expected=JSONException.class)
    public void throwsJSONExceptionWithInvalidJson() throws IOException {
        String path = "src/test/test_json/invalid.json";
        String invalid = getJSONFromFile(path);
        converter.toXML(invalid);
    }

    @Test(expected=JSONException.class)
    public void throwsJSONExceptionWhenInputNotJson() {
        String input = "abc";
        converter.toXML(input);
    }

    private String getJSONFromFile(String path) throws IOException {
        File file = new File(path);
        return FileUtils.readFileToString(file);
    }
}
