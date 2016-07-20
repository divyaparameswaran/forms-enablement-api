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
import java.util.concurrent.TimeUnit;

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

        Assert.assertTrue(documents.get(0).getInteger("packageIdentifier") == PACKAGE_JSON_ID);
        Assert.assertTrue(documents.get(0).getInteger("count") == 2);
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

        helper.updatePackageStatusByPackageId(PACKAGE_JSON_ID, "SUCCESS");

        Document pack = helper.getPackageByPackageId(PACKAGE_JSON_ID);

        Assert.assertTrue(pack.getString(config.getFormStatusPropertyNameOut()).equals("SUCCESS"));
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

        helper.updateFormsStatusByPackageId(FIVE_PACKAGE_JSON_ID, "FAILED");

        ArrayList<Document> forms = helper.getFormsCollectionByPackageId(FIVE_PACKAGE_JSON_ID).into(new ArrayList<Document>());

        Assert.assertTrue(forms.get(0).getString(config.getFormStatusPropertyNameOut()).equals("FAILED"));
        Assert.assertTrue(forms.get(1).getString(config.getFormStatusPropertyNameOut()).equals("FAILED"));
        Assert.assertTrue(forms.get(2).getString(config.getFormStatusPropertyNameOut()).equals("FAILED"));
        Assert.assertTrue(forms.get(3).getString(config.getFormStatusPropertyNameOut()).equals("FAILED"));
        Assert.assertTrue(forms.get(4).getString(config.getFormStatusPropertyNameOut()).equals("FAILED"));
    }


}
