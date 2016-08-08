package com.ch.conversion.builders;

import com.ch.application.FormServiceConstants;
import com.ch.client.PresenterHelper;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.helpers.MultiPartHelper;
import com.ch.exception.PackageContentsException;
import com.ch.exception.PresenterAuthenticationException;
import com.ch.model.FormsPackage;
import com.ch.model.PresenterAuthRequest;
import com.ch.model.PresenterAuthResponse;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONException;
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
  private final FormsPackage formsPackage;
  private final PresenterHelper presenterHelper;

  /**
   * Convert FormDataMultiPart.
   *
   * @param config json and xml
   * @param parts  the form parts
   */
  public JsonBuilder(ITransformConfig config, FormDataMultiPart parts, PresenterHelper presenterHelper) {
    this.config = config;
    this.presenterHelper = presenterHelper;
    MultiPartHelper helper = MultiPartHelper.getInstance();
    this.formsPackage = helper.getPackageFromMultiPart(config, parts);
  }

  /**
   * Builder to create the json object for multiple forms.
   *
   * @param config      json and xml
   * @param packageJson package data
   * @param formsJson   list of forms json (untransformed)
   */
  public JsonBuilder(ITransformConfig config, String packageJson, List<String> formsJson, PresenterHelper presenterHelper) {
    this.config = config;
    this.formsPackage = new FormsPackage(packageJson, formsJson);
    this.presenterHelper = presenterHelper;
  }

  /**
   * Get the transformed forms package for multiple forms.
   *
   * @return forms package
   */
  public FormsPackage getTransformedPackage() {
    // 1. create list of transformed forms
    List<JSONObject> forms = new ArrayList<>();

    // 2. Get the submission number for addition to all parts of the entity
    String submissionNumber = getSubmissionNumber((String) formsPackage.getPackageMetaDataJson()
      .get(config.getPackageIdentifierElementNameOut()));

    // 3. transform package meta data
    JSONObject packageMetaData = getTransformedPackageMetaData();

    // 4. check the package meta data for auth credentials
    PresenterAuthRequest presenterAuthRequest = getAuthRequest(packageMetaData);

    // 5. if there are credentials make call to presenter auth api
    if (presenterAuthRequest == null) {

      // 6. if there are no presenter credentials loop and transform without the account number
      for (String formJson : formsPackage.getForms()) {
        forms.add(getBuilderJson(formJson, null));
      }
    } else {

      //7. Get the presenters account number
      PresenterAuthResponse presenterAuthResponse = presenterHelper.getPresenterResponse(
        presenterAuthRequest.getPresenterId(),
        presenterAuthRequest.getPresenterAuth());

      //8. If no account number is returned from api, end submission with exception
      if (presenterAuthResponse.getPresenterAccountNumber() == null) {
        throw new PresenterAuthenticationException(presenterAuthRequest.getPresenterId(), presenterAuthRequest.getPresenterAuth());
      }

      // 9. loop forms and transform
      for (String formJson : formsPackage.getForms()) {
        forms.add(getBuilderJson(formJson, presenterAuthResponse.getPresenterAccountNumber()));
      }
    }

    //10. check the number of forms matches those prescribed in the package, if not throw an exception
    int packageFormCount = (Integer) formsPackage.getPackageMetaDataJson().get(config.getPackageCountPropertyNameIn());

    if (forms.size() != packageFormCount) {
      throw new PackageContentsException(config.getPackageCountPropertyNameIn());
    }

    //11. add submission number to package
    JSONObject transformedPackageMetaData = addSubmissionNumberToPackage(packageMetaData, submissionNumber);

    //12. add submission number to forms
    List<JSONObject> transformedForms = addSubmissionNumberToForms(forms, submissionNumber);

    // 13. return transformed package
    return new FormsPackage(transformedPackageMetaData.toString(), getFormsAsString(transformedForms));
  }

  private JSONObject getTransformedPackageMetaData() {
    JSONObject packageMetaData = new JSONObject(formsPackage.getPackageMetaData());

    // 1. add datetime to package meta data
    packageMetaData.put(config.getPackageDatePropertyNameOut(), getDateTime());

    // 2. add default status
    Object status = getDefaultStatus();
    packageMetaData.put(config.getFormStatusPropertyNameOut(), status);

    return packageMetaData;
  }

  protected String getSubmissionNumber(String packageId) {
    DateFormat dateFormat = new SimpleDateFormat(FormServiceConstants.DATE_TIME_FORMAT_SUBMISSION, Locale.ENGLISH);
    String format = dateFormat.format(new Date());
    return packageId + "-" + format;
  }

  protected PresenterAuthRequest getAuthRequest(JSONObject packageMetaData) {
    String presenterId, presenterAuth;

    try {
      presenterId = packageMetaData.getString("presenterId");
      presenterAuth = packageMetaData.getString("presenterAuth");
    } catch (JSONException e) {
      return null;
    }
    return new PresenterAuthRequest(presenterId, presenterAuth);
  }

  protected JSONObject getBuilderJson(String json, String presenterAccountNumber) {
    FormJsonBuilder builder = new FormJsonBuilder(config, formsPackage.getPackageMetaData(), json, presenterAccountNumber);
    return builder.getJson();
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
