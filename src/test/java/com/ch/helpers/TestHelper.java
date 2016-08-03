package com.ch.helpers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by elliott.jenkins on 01/04/2016.
 */
public class TestHelper {

    protected static final String EXAMPLE_JSON_PATH = "json/example.json";
    protected static final String INVALID_JSON_PATH = "json/invalid.json";
    protected static final String VALID_JSON_PATH = "json/valid.json";
    protected static final String PACKAGE_JSON_PATH = "json/package.json";
    protected static final long PACKAGE_JSON_ID = 12345;
    protected static final String SINGLE_PACKAGE_JSON_PATH = "json/single_package.json";
    protected static final long SINGLE_PACKAGE_JSON_ID = 54321;
    protected static final String FIVE_PACKAGE_JSON_PATH = "json/five_package.json";
    protected static final long FIVE_PACKAGE_JSON_ID = 12355;
    protected static final String THREE_PACKAGE_JSON_PATH = "json/three_package.json";
    protected static final String PACKAGE_NO_CREDENTIALS_PATH = "json/package_no_credentials.json";
    protected static final String PACKAGE_INVALID_CREDENTIALS_JSON_PATH = "json/package_invalid_credentials.json";
    protected static final String FORM_JSON_PATH = "json/form.json";
    protected static final String FORM_ALL_JSON_PATH = "json/form_all.json";
    protected static final String FORM_ALL_JSON_NO_ACC_NUMBER_PATH = "json/form_no_account_number.json";
    protected static final String FORM_ALL_JSON_NO_PAYMENT_PATH = "json/form_no_payment.json";
    protected static final String INVALID_FORM_JSON_PATH = "json/invalid_form_all.json";
    protected static final String META_PATH = "json/meta.json";
    protected static final String LOWERCASE_JSON_PATH = "json/lower_case.json";
    protected static final String DATE_JSON_PATH = "json/date.json";
    protected static final String RESPONSE_JSON_PATH = "json/response.json";

    protected static final String FORM_XML_PATH = "xml/form.xml";
    protected static final String CONVERTED_FORM_XML_PATH = "xml/converted_form.xml";
    protected static final String EXAMPLE_XML_PATH = "xml/example.xml";
    protected static final String VALID_DS01_XML_PATH = "xml/valid_DS01.xml";
    protected static final String INVALID_DS01_XML_PATH = "xml/invalid_DS01.xml";
    protected static final String INVALID_FORM_XML_PATH = "xml/invalid_form.xml";
    protected static final String BLANK_FORM_XML_PATH = "xml/blank_form.xml";
    protected static final String NO_SCHEMA_XML_PATH = "xml/no_schema_form.xml";
    public static final String FORM_SERVICE_TEST_YML = "FORM_SERVICE_TEST_YML";

    public static final long TEST_PACKAGE_ID = 12345;
    protected static final String SALESFORCE_ACCESS_TOKEN = "Bearer abcdefghijklmnopqrstuvwxyz";
    protected static final String TEST_PRESENTER_ACCOUNT = "98756122";



    public String getStringFromFile(String filename) throws IOException {
        String fileAsString;
        try {
            File file = new File("src/test/resources/" + filename);
            fileAsString = FileUtils.readFileToString(file);
        } catch (FileNotFoundException ex) {
            return IOUtils.toString(getClass().getClassLoader().getResourceAsStream(filename));
        }
        return fileAsString;
    }
}