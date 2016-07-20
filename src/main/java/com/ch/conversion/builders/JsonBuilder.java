package com.ch.conversion.builders;

import com.ch.application.FormServiceConstants;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.helpers.MultiPartHelper;
import com.ch.exception.PackageContentsException;
import com.ch.model.FormsPackage;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by elliott.jenkins on 31/03/2016.
 */
public class JsonBuilder {

  private final ITransformConfig config;
  private final FormsPackage rawFormsPackage;

  /**
   * Convert FormDataMultiPart.
   *
   * @param config json and xml
   * @param parts  the form parts
   */
  public JsonBuilder(ITransformConfig config, FormDataMultiPart parts) {
    this.config = config;
    MultiPartHelper helper = MultiPartHelper.getInstance();
    this.rawFormsPackage = helper.getPackageFromMultiPart(config, parts);
  }

  /**
   * Builder to create the json object for multiple forms.
   *
   * @param config      json and xml
   * @param packageJson package data
   * @param formsJson   list of forms json (untransformed)
   */
  public JsonBuilder(ITransformConfig config, String packageJson, List<String> formsJson) {
    this.config = config;
    this.rawFormsPackage = new FormsPackage(packageJson, formsJson);
  }

  /**
   * Get the transformed forms package for multiple forms.
   *
   * @return forms package
   */
  public FormsPackage getTransformedPackage() {
    // 1. create list of transformed forms
    List<JSONObject> forms = new ArrayList<>();

    // 2. loop forms and transform
    for (String formJson : rawFormsPackage.getForms()) {
      FormJsonBuilder builder = new FormJsonBuilder(config, rawFormsPackage.getPackageMetaData(), formJson);
      JSONObject object = builder.getJson();
      forms.add(object);
    }

    //3. check the number of forms matches those prescribed in the package
    int packageFormCount = (Integer) rawFormsPackage.getPackageMetaDataJson().get(config.getPackageCountPropertyNameIn());

    if (forms.size() != packageFormCount) {
      throw new PackageContentsException(config.getPackageCountPropertyNameIn());
    }

    // 4. Get the submission number for addition to all parts of the entity
    String submissionNumber = getSubmissionNumber((Integer) rawFormsPackage.getPackageMetaDataJson()
      .get(config.getPackageIdentifierElementNameOut()));

    // 5. transform package meta data
    JSONObject packageMetaData = getTransformedPackageMetaData();

    //6. add submission number to package
    JSONObject transformedPackageMetaData = addSubmissionNumberToPackage(packageMetaData, submissionNumber);

    //7. add submission number to forms
    List<JSONObject> transformedForms = addSubmissionNumberToForms(forms, submissionNumber);

    // 8. return transformed package
    return new FormsPackage(transformedPackageMetaData.toString(), getFormsAsString(transformedForms));
  }

  private JSONObject getTransformedPackageMetaData() {
    JSONObject packageMetaData = new JSONObject(rawFormsPackage.getPackageMetaData());

    // 1. add datetime to package meta data
    packageMetaData.put(config.getPackageDatePropertyNameOut(), getDateTime());

    // 2. add default status
    Object status = getDefaultStatus();
    packageMetaData.put(config.getFormStatusPropertyNameOut(), status);

    return packageMetaData;
  }

  protected String getSubmissionNumber(long packageId) {
    DateFormat dateFormat = new SimpleDateFormat(FormServiceConstants.DATE_TIME_FORMAT_SUBMISSION, Locale.ENGLISH);
    String format = dateFormat.format(new Date());
    return packageId + "-" + format;
  }

  private JSONObject addSubmissionNumberToPackage(JSONObject object, String submissionNumber) {
    object.put(FormServiceConstants.PACKAGE_SUBMISSION_NUMBER_KEY, submissionNumber);
    return object;
  }

  private List<JSONObject> addSubmissionNumberToForms(List<JSONObject> forms, String submissionNumber) {
    List<JSONObject> formList = new ArrayList<>();
    for (JSONObject form : forms) {
      form.put(FormServiceConstants.PACKAGE_SUBMISSION_NUMBER_KEY, submissionNumber);
      formList.add(form);
    }
    return formList;
  }

  private List<String> getFormsAsString(List<JSONObject> forms) {
    List<String> formList = new ArrayList<>();
    for (JSONObject form : forms) {
      formList.add(form.toString());
    }
    return formList;
  }

  private String getDefaultStatus() {
    return FormServiceConstants.PACKAGE_STATUS_DEFAULT;
  }

  private String getDateTime() {
    DateFormat dateFormat = new SimpleDateFormat(FormServiceConstants.DATE_TIME_FORMAT_DB, Locale.ENGLISH);
    return dateFormat.format(new Date());
  }
}
