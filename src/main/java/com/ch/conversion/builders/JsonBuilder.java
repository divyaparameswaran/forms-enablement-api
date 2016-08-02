package com.ch.conversion.builders;


import com.ch.client.PresenterHelper;
import com.ch.conversion.config.ITransformConfig;
import com.ch.conversion.helpers.MultiPartHelper;
import com.ch.model.FormsPackage;
import com.ch.model.PresenterAuthRequest;
import com.ch.model.PresenterAuthResponse;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

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

    // 2. create initial JSON array
    JSONArray array = new JSONArray();

    // 3. Check for auth credentials
    JSONObject transformedPackageMetaData = formsPackage.getPackageMetaDataJson();

    PresenterAuthRequest presenterAuthRequest = getAuthRequest(transformedPackageMetaData);

    if (presenterAuthRequest != null) {

      //TODO what if nothing is received from presenter
      //get Account number
      // Get auth response from entity
      PresenterAuthResponse presenterAuthResponse = presenterHelper.getPresenterResponse(
          presenterAuthRequest.getPresenterId(),
          presenterAuthRequest.getPresenterAuth());

      //If has credentials but no account numbers
      if (presenterAuthResponse.getPresenterAccountNumber() == null) {
        //TODO create custom exception
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
      }

      // 3. loop forms and transform
      for (String formJson : formsPackage.getForms()) {

        JSONObject formJsonObject = toJsonOBject(formJson);

        String accountJson = addAccountNumber(formJsonObject, presenterAuthResponse.getPresenterAccountNumber());

        FormJsonBuilder builder = new FormJsonBuilder(config, formsPackage.getPackageMetaData(), accountJson);

        JSONObject finalObject = builder.getJson();

        array.put(finalObject);

      }
      root.put(config.getFormsPropertyNameOut(), array);
      return root.toString();
    }


    // 3. loop forms and transform
    for (String formJson : formsPackage.getForms()) {
      FormJsonBuilder builder = new FormJsonBuilder(config, formsPackage.getPackageMetaData(), formJson);
      JSONObject object = builder.getJson();
      array.put(object);

    }
    // 4. add array to root
    root.put(config.getFormsPropertyNameOut(), array);
    return root.toString();
  }


  protected PresenterAuthRequest getAuthRequest(JSONObject packageMetaData) {
    String presenterId = null;
    String presenterAuth = null;

    try {
      presenterId = packageMetaData.getString("presenterId");
      presenterAuth = packageMetaData.getString("presenterAuth");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return new PresenterAuthRequest(presenterId, presenterAuth);
  }

  private JSONObject toJsonOBject(String form) {
    return new JSONObject(form);
  }

  protected String addAccountNumber(JSONObject form, String accountNumber) {

    try {
      if ("account".equals(form.getJSONObject(config.getFormPropertyNameIn())
          .getJSONObject(config.getFilingDetailsPropertyNameIn())
          .getJSONObject("payment")
          .get("paymentMethod"))) {

        form.getJSONObject(config.getFormPropertyNameIn())
            .getJSONObject(config.getFilingDetailsPropertyNameIn())
            .getJSONObject("payment").put("accountNumber", accountNumber);

        return form.toString();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return form.toString();
  }
}



