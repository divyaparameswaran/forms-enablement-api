package com.ch.helpers;

import com.ch.application.FormServiceConstants;
import com.ch.application.FormsServiceApplication;
import com.ch.configuration.FormsServiceConfiguration;
import com.ch.conversion.builders.JsonBuilder;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.config.TransformConfig;
import com.ch.cucumber.FormServiceTestSuiteIT;
import com.ch.model.FormsPackage;
import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.bson.Document;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron.Witter on 18/07/2016.
 */
public class MongoHelperTest extends TestHelper{

    private MongoHelper helper;
    ITransformConfig config;

    @ClassRule
    public static final DropwizardAppRule<FormsServiceConfiguration> RULE =
        new DropwizardAppRule<>(FormsServiceApplication.class, ResourceHelpers.resourceFilePath
            ("test-configuration.yml"));

    @Before
    public void setUp() {
        config = new TransformConfig();
        MongoHelper.init(RULE.getConfiguration());
        helper =  MongoHelper.getInstance();
        helper.dropCollection("forms");
        helper.dropCollection("packages");
    }

    @Test
    public void shouldReturnResultsOldestFirst() throws IOException {

        // valid package data
        String package_string = getStringFromFile(PACKAGE_JSON_PATH);
        // valid forms
        String valid = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            valid_forms.add(valid);
        }
        // builder
        FormsPackage formsPackage = new JsonBuilder(config, package_string, valid_forms).getTransformedPackage();

        helper.storeFormsPackage(formsPackage);

        // valid package data
        String package_string2 = getStringFromFile(SINGLE_PACKAGE_JSON_PATH);
        // valid forms
        String valid2 = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms2 = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            valid_forms2.add(valid2);
        }
        // builder
        FormsPackage formsPackage2 = new JsonBuilder(config, package_string2, valid_forms2).getTransformedPackage();

        helper.storeFormsPackage(formsPackage2);

        ArrayList<Document> documents = helper.getPackagesCollectionByStatus("PENDING", 2).into(new ArrayList<Document>());

        Assert.assertTrue(documents.get(0).get("packageIdentifier").equals(12345));
        Assert.assertTrue(documents.get(0).get("count").equals(2));
    }

    @Test
    public void shouldReturnAllPackages() throws IOException {

        // valid package data
        String package_string = getStringFromFile(PACKAGE_JSON_PATH);
        // valid forms
        String valid = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            valid_forms.add(valid);
        }
        // builder
        FormsPackage formsPackage = new JsonBuilder(config, package_string, valid_forms).getTransformedPackage();

        helper.storeFormsPackage(formsPackage);

        // valid package data
        String package_string2 = getStringFromFile(SINGLE_PACKAGE_JSON_PATH);
        // valid forms
        String valid2 = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms2 = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            valid_forms2.add(valid2);
        }
        // builder
        FormsPackage formsPackage2 = new JsonBuilder(config, package_string2, valid_forms2).getTransformedPackage();

        helper.storeFormsPackage(formsPackage2);

        ArrayList<Document> documents = helper.getPackagesCollectionByStatus("PENDING", 0).into(new ArrayList<Document>());

        Assert.assertTrue(documents.size() == 2);
    }

    @Test
    public void shouldReturnOnePackage() throws IOException {

        // valid package data
        String package_string = getStringFromFile(PACKAGE_JSON_PATH);
        // valid forms
        String valid = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            valid_forms.add(valid);
        }
        // builder
        FormsPackage formsPackage = new JsonBuilder(config, package_string, valid_forms).getTransformedPackage();

        helper.storeFormsPackage(formsPackage);

        // valid package data
        String package_string2 = getStringFromFile(SINGLE_PACKAGE_JSON_PATH);
        // valid forms
        String valid2 = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms2 = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            valid_forms2.add(valid2);
        }
        // builder
        FormsPackage formsPackage2 = new JsonBuilder(config, package_string2, valid_forms2).getTransformedPackage();

        helper.storeFormsPackage(formsPackage2);

        ArrayList<Document> documents = helper.getPackagesCollectionByStatus("PENDING", 1).into(new ArrayList<Document>());

        Assert.assertTrue(documents.size() == 1);
    }

    @Test
    public void shouldUpdatePackageStatus() throws IOException {

        // valid package data
        String package_string = getStringFromFile(PACKAGE_JSON_PATH);
        // valid forms
        String valid = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            valid_forms.add(valid);
        }
        // builder
        FormsPackage formsPackage = new JsonBuilder(config, package_string, valid_forms).getTransformedPackage();

        helper.storeFormsPackage(formsPackage);

        helper.updatePackageStatusByPackageId(12345, "SUCCESS");

        Document pack = helper.getPackageByPackageId(12345);

        Assert.assertTrue(pack.getString(FormServiceConstants.PACKAGE_STATUS_KEY).equals("SUCCESS"));
    }

    @Test
    public void shouldUpdateAllFormsStatus() throws IOException {

        // valid package data
        String package_string = getStringFromFile(FIVE_PACKAGE_JSON_PATH);
        // valid forms
        String valid = getStringFromFile(FORM_ALL_JSON_PATH);
        List<String> valid_forms = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            valid_forms.add(valid);
        }
        // builder
        FormsPackage formsPackage = new JsonBuilder(config, package_string, valid_forms).getTransformedPackage();

        helper.storeFormsPackage(formsPackage);

        helper.updateFormsStatusByPackageId(12345, "FAILED");

        ArrayList<Document> forms = helper.getFormsCollectionByPackageId(12345).into(new ArrayList<>());

        Assert.assertTrue(forms.get(0).getString(FormServiceConstants.PACKAGE_STATUS_KEY).equals("FAILED"));
        Assert.assertTrue(forms.get(1).getString(FormServiceConstants.PACKAGE_STATUS_KEY).equals("FAILED"));
        Assert.assertTrue(forms.get(2).getString(FormServiceConstants.PACKAGE_STATUS_KEY).equals("FAILED"));
        Assert.assertTrue(forms.get(3).getString(FormServiceConstants.PACKAGE_STATUS_KEY).equals("FAILED"));
        Assert.assertTrue(forms.get(4).getString(FormServiceConstants.PACKAGE_STATUS_KEY).equals("FAILED"));

    }


}
