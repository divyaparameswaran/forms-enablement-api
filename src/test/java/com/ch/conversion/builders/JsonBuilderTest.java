package com.ch.conversion.builders;

import com.ch.application.FormServiceConstants;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TransformConfig;
import com.ch.exception.MissingRequiredDataException;
import com.ch.helpers.TestHelper;
import com.ch.model.FormsPackage;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.MediaType;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class JsonBuilderTest extends TestHelper {

    ITransformConfig config;

    @Before
    public void setUp() {
        config = new TransformConfig();
    }

    @Test(expected = JSONException.class)
    public void throwsJSONExceptionWithInvalidJson() throws Exception {
        String invalid = getStringFromFile(INVALID_JSON_PATH);
        List<String> invalid_forms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            invalid_forms.add(invalid);
        }
        JsonBuilder builder = new JsonBuilder(config, invalid, invalid_forms);
        builder.getTransformedPackage();
    }

    @Test(expected = MissingRequiredDataException.class)
    public void throwsMissingRequiredDataExceptionWithValidJsonMissingRequiredData() throws Exception {
        String valid = getStringFromFile(VALID_JSON_PATH);
        List<String> valid_json_forms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            valid_json_forms.add(valid);
        }
        JsonBuilder builder = new JsonBuilder(config, valid, valid_json_forms);
        builder.getTransformedPackage();
    }

    @Test
    public void createStringForValidJson() throws Exception {
        JsonBuilder builder = getValidJsonBuilder();
        FormsPackage transformedPackage = builder.getTransformedPackage();
        Assert.assertNotNull(transformedPackage);
    }

    @Test(expected = Exception.class)
    public void throwsExceptionWithEmptyMultiPart() throws Exception {
        FormDataMultiPart multi = new FormDataMultiPart();
        JsonBuilder builder = new JsonBuilder(config, multi);
        builder.getTransformedPackage();
    }

    @Test
    public void createStringForValidMultiPart() throws Exception {
        FormDataMultiPart multi = getValidMultiPart();
        JsonBuilder builder = new JsonBuilder(config, multi);
        FormsPackage transformedPackage = builder.getTransformedPackage();
        Assert.assertNotNull(transformedPackage);
    }

    private JsonBuilder getValidJsonBuilder() throws Exception {
        // valid package data
        String package_string = getStringFromFile(PACKAGE_JSON_PATH);
        // valid forms
        String valid = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            valid_forms.add(valid);
        }
        // builder
        return new JsonBuilder(config, package_string, valid_forms);
    }

    @Test
    public void shouldAddValidSubmissionNumberToAllElements() throws Exception {
        //Given a valid package is submitted
        // valid package data
        String package_string = getStringFromFile(PACKAGE_JSON_PATH);
        // valid forms
        String valid = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            valid_forms.add(valid);
        }

        // when this package is transformed
        FormsPackage pack =  new JsonBuilder(config, package_string, valid_forms).getTransformedPackage();

        //all elements should contain the same submissionNumber

        String packageSubmissionNumber  = pack.getPackageMetaDataJson()
          .getString(FormServiceConstants.PACKAGE_SUBMISSION_NUMBER_KEY);
        String formOneSubmissionNumber = pack.getFormsJSon().get(0).getString(FormServiceConstants.PACKAGE_SUBMISSION_NUMBER_KEY);
        String formTwoSubmissionNumber = pack.getFormsJSon().get(1).getString(FormServiceConstants.PACKAGE_SUBMISSION_NUMBER_KEY);


        Assert.assertTrue(!packageSubmissionNumber.equals(null));
        Assert.assertTrue(!formOneSubmissionNumber.equals(null));
        Assert.assertTrue(!formTwoSubmissionNumber.equals(null));
        Assert.assertTrue(formTwoSubmissionNumber.equals(packageSubmissionNumber));
    }

    @Test
    public void shouldAddSubmissionNumbersToForms() throws Exception {
        JsonBuilder builder = getValidJsonBuilder();
        long packageId = 12345;
        String submissionNumber = builder.getSubmissionNumber(packageId);

        DateFormat dateFormat = new SimpleDateFormat(FormServiceConstants.DATE_TIME_FORMAT_SUBMISSION, Locale.ENGLISH);
        String format = dateFormat.format(new Date());

        Assert.assertTrue(submissionNumber.equals(packageId + "-" + format));
    }


    private FormDataMultiPart getValidMultiPart() throws IOException {
        FormDataMultiPart multi = new FormDataMultiPart();
        // forms package data
        String pack = getStringFromFile(SINGLE_PACKAGE_JSON_PATH);
        multi.field(config.getPackageMultiPartName(), pack, MediaType.APPLICATION_JSON_TYPE);
        // form json
        String form = getStringFromFile(FORM_ALL_JSON_PATH);
        multi.field("form1", form, MediaType.APPLICATION_JSON_TYPE);
        return multi;
    }
}
