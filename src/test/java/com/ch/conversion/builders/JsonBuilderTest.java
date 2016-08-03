package com.ch.conversion.builders;

import com.ch.client.PresenterHelper;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TransformConfig;
import com.ch.exception.MissingRequiredDataException;
import com.ch.helpers.TestHelper;
import com.ch.model.PresenterAuthRequest;
import com.ch.model.PresenterAuthResponse;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class JsonBuilderTest extends TestHelper {

    ITransformConfig config;
    PresenterHelper helper;

    @Before
    public void setUp() {

        config = new TransformConfig();
        helper = mock(PresenterHelper.class);
        when(helper.getPresenterResponse(anyString(), anyString())).thenReturn(new PresenterAuthResponse("1234567"));

    }

    @Test(expected = JSONException.class)
    public void throwsJSONExceptionWithInvalidJson() throws Exception {
        String invalid = getStringFromFile(INVALID_JSON_PATH);
        List<String> invalid_forms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            invalid_forms.add(invalid);
        }
        JsonBuilder builder = new JsonBuilder(config, invalid, invalid_forms, helper);
        builder.getJson();
    }

    // TODO: is this the desired behaviour?
    @Test(expected = MissingRequiredDataException.class)
    public void throwsMissingRequiredDataExceptionWithValidJsonMissingRequiredData() throws Exception {
        String valid = getStringFromFile(VALID_JSON_PATH);
        List<String> valid_json_forms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            valid_json_forms.add(valid);
        }
        JsonBuilder builder = new JsonBuilder(config, valid, valid_json_forms, helper);
        builder.getJson();
    }

    // TODO: what to assert?
    @Test
    public void createStringForValidJson() throws Exception {
        JsonBuilder builder = getValidJsonBuilder();
        String json = builder.getJson();
        Assert.assertNotNull(json);
    }

    @Test(expected = Exception.class)
    public void throwsExceptionWithEmptyMultiPart() throws Exception {
        FormDataMultiPart multi = new FormDataMultiPart();
        JsonBuilder builder = new JsonBuilder(config, multi,helper);
        builder.getJson();
    }

    @Test
    public void createStringForValidMultiPart() throws Exception {
        FormDataMultiPart multi = getValidMultiPart();
        JsonBuilder builder = new JsonBuilder(config, multi, helper);
        String json = builder.getJson();
        Assert.assertNotNull(json);
    }

    @Test
    public void shouldReturnNullObject() throws IOException {
        FormDataMultiPart multi = getValidMultiPart();
        JsonBuilder builder = new JsonBuilder(config, multi, helper);
        JSONObject object = new JSONObject();
        Assert.assertFalse(builder.getAuthRequest(object) instanceof PresenterAuthRequest);
    }

    @Test
    public void shouldReturnPresenterAuthRequest() throws IOException {
        FormDataMultiPart multi = getValidMultiPart();
        JsonBuilder builder = new JsonBuilder(config, multi, helper);
        JSONObject object = new JSONObject();
        object.put("presenterId", "1234567");
        object.put("presenterAuth", "1234567");
        Assert.assertTrue(builder.getAuthRequest(object) instanceof PresenterAuthRequest);
    }

    @Test
    public void shouldAddAccountNumber() throws IOException {
        FormDataMultiPart multi = getValidMultiPart();
        JsonBuilder builder = new JsonBuilder(config, multi, helper);
        String formWithoutAccountNumber = getStringFromFile(FORM_ALL_JSON_NO_ACC_NUMBER_PATH);
        String accountNumber = "1234454";
        JSONObject form = new JSONObject(formWithoutAccountNumber);

        String formWithAccountString = builder.addAccountNumber(form, accountNumber);

        JSONObject formWithAccount = new JSONObject(formWithAccountString);

        Assert.assertTrue(accountNumber.equals(formWithAccount.getJSONObject(config.getFormPropertyNameIn())
            .getJSONObject(config.getFilingDetailsPropertyNameIn())
            .getJSONObject("payment")
            .get("accountNumber")));
    }

    @Test(expected = JSONException.class)
    public void shouldThrowJsonExceptionIsAbsenceOfProperty() throws IOException {
        FormDataMultiPart multi = getValidMultiPart();
        JsonBuilder builder = new JsonBuilder(config, multi, helper);
        String formWithoutPayment = getStringFromFile(FORM_ALL_JSON_NO_PAYMENT_PATH);
        String accountNumber = "1234454";
        JSONObject form = new JSONObject(formWithoutPayment);

        String formWithoutPaymentString = builder.addAccountNumber(form, accountNumber);

        JSONObject formWithoutPaymentObject = new JSONObject(formWithoutPaymentString);

        accountNumber.equals(formWithoutPaymentObject.getJSONObject(config.getFormPropertyNameIn())
            .getJSONObject(config.getFilingDetailsPropertyNameIn())
            .getJSONObject(config.getPaymentPropertyNameIn())
            .get(config.getPaymentMethodPropertyNameIn()));
    }

    private JsonBuilder getValidJsonBuilder() throws Exception {
        // valid package data
        String package_string = getStringFromFile(PACKAGE_JSON_PATH);
        // valid forms
        String valid = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            valid_forms.add(valid);
        }
        // builder
        return new JsonBuilder(config, package_string, valid_forms, helper);
    }

    private FormDataMultiPart getValidMultiPart() throws IOException {
        FormDataMultiPart multi = new FormDataMultiPart();
        // forms package data
        String pack = getStringFromFile(PACKAGE_JSON_PATH);
        multi.field(config.getPackageMultiPartName(), pack, MediaType.APPLICATION_JSON_TYPE);
        // form json
        String form = getStringFromFile(FORM_ALL_JSON_PATH);
        multi.field("form1", form, MediaType.APPLICATION_JSON_TYPE);
        return multi;
    }


}
