package com.ch.conversion.builders;


import com.ch.client.PresenterHelper;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.helpers.MultiPartHelper;
import com.ch.exception.PresenterAuthenticationException;
import com.ch.model.FormsPackage;
import com.ch.model.PresenterAuthRequest;
import com.ch.model.PresenterAuthResponse;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;



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
   * Get the json object for multiple forms.
   *
   * @return json
   */
  public String getJson() {
    // 1. create root JSON object
    JSONObject root = new JSONObject();

    // 2. create JSON array
    JSONArray array = new JSONArray();

    // 3. Check for auth credentials
    JSONObject transformedPackageMetaData = formsPackage.getPackageMetaDataJson();

    PresenterAuthRequest presenterAuthRequest = getAuthRequest(transformedPackageMetaData);

    //if there are credentials make call to presenter auth api
    if (presenterAuthRequest != null) {

      //get Account number
      PresenterAuthResponse presenterAuthResponse = presenterHelper.getPresenterResponse(
          presenterAuthRequest.getPresenterId(),
          presenterAuthRequest.getPresenterAuth());

      //If no account number is returned from api, end submission
      if (presenterAuthResponse.getPresenterAccountNumber() == null) {
        throw new PresenterAuthenticationException(presenterAuthRequest.getPresenterId(),presenterAuthRequest.getPresenterAuth());
      }

      // 4. loop forms and transform with account numbers
      for (String formJson : formsPackage.getForms()) {
        JSONObject finalObject = getFormJson(formJson, presenterAuthResponse.getPresenterAccountNumber());
        array.put(finalObject);
      }
      root.put(config.getFormsPropertyNameOut(), array);
      return root.toString();
    }


    // 5. loop forms and transform without account numbers
    for (String formJson : formsPackage.getForms()) {
      JSONObject object = getFormJson(formJson, null);
      array.put(object);
    }
    // 4. add array to root
    root.put(config.getFormsPropertyNameOut(), array);
    return root.toString();
  }


  protected PresenterAuthRequest getAuthRequest(JSONObject packageMetaData) {
    String presenterId, presenterAuth;

    try {
      presenterId = packageMetaData.getString("presenterId");
      presenterAuth = packageMetaData.getString("presenterAuth");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return new PresenterAuthRequest(presenterId, presenterAuth);
  }

  /**
   *  Adds account number to json object if payment number is account.
   * @param form form as json object.
   * @param accountNumber account number as string.
   * @return Form as string.
   */
  protected String addAccountNumber(JSONObject form, String accountNumber) {

    try {
      if ("account".equals(form.getJSONObject(config.getFormPropertyNameIn())
          .getJSONObject(config.getFilingDetailsPropertyNameIn())
          .getJSONObject(config.getPaymentPropertyNameIn())
          .get(config.getPaymentMethodPropertyNameIn()))) {

        form.getJSONObject(config.getFormPropertyNameIn())
            .getJSONObject(config.getFilingDetailsPropertyNameIn())
            .getJSONObject(config.getPaymentPropertyNameIn()).put(config.getAccountNumberPropertyNameIn(), accountNumber);

        return form.toString();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return form.toString();
  }

  /**
   *  Gets form json via form json builder, calls add accountNumber to the json if required.
   * @param formJson form as string.
   * @param accountNumber account number as string.
   * @return JSON object of transformed form.
   */
  private JSONObject getFormJson(String formJson, String accountNumber) {

    String newJson = formJson;

    if (accountNumber != null) {

      JSONObject formJsonObject = toJsonObject(formJson);

      newJson = addAccountNumber(formJsonObject, accountNumber);
    }

    FormJsonBuilder builder = new FormJsonBuilder(config, formsPackage.getPackageMetaData(), newJson);

    return builder.getJson();
  }

  /**
   * Generate json object from string.
   * @param form form as string.
   * @return json object.
   */
  private JSONObject toJsonObject(String form) {
    return new JSONObject(form);
  }

}



