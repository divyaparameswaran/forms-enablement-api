package com.ch.helpers;

import com.ch.application.FormServiceConstants;
import com.ch.conversion.config.ITransformConfig;
import com.ch.model.FormStatus;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Created by Aaron.Witter on 18/07/2016.
 */
public class QueueHelper {

  private ITransformConfig configuration;
  private MongoHelper helper = MongoHelper.getInstance();

  public QueueHelper(ITransformConfig configuration) {
    this.configuration = configuration;
  }

  public List<JSONObject> getCompletePackagesByStatus(String requestStatus, int count) {

    List<JSONObject> completeForms = new ArrayList<>();

    //get all packages matching criteria
    ArrayList<Document> packages = helper.getPackagesCollectionByStatus(requestStatus, count).into(new ArrayList<Document>());

    for (Document pack : packages) {

      //for each package search the database for forms matching the identifier and status
      ArrayList<Document> forms = helper.getFormsCollectionByPackageIdAndStatus((int) pack
        .get(FormServiceConstants.PACKAGE_IDENTIFIER_KEY), pack.getString(FormServiceConstants.PACKAGE_STATUS_KEY))
        .into(new ArrayList<Document>());

      //create a complete package from the package metadata and it's accompanying forms.
      completeForms.add(createPackage(new JSONObject(pack.toJson()), forms));
    }
    return completeForms;
  }

  public JSONObject getCompletePackageById(long packageId) {

    //get package matching criteria
    Document pack = helper.getPackageByPackageId(packageId);

    //search the database for forms matching the identifier
    ArrayList<Document> forms = helper.getFormsCollectionByPackageId((int) pack.get(FormServiceConstants.PACKAGE_IDENTIFIER_KEY))
      .into(new ArrayList<Document>());

    //create a complete package from the package metadata and it's accompanying forms.
    return createPackage(new JSONObject(pack.toJson()), forms);
  }

  public boolean updateCompletePackageById(long packageId, String requestStatus) {

    boolean updatePackage = updatePackageStatusById(packageId, requestStatus);

    boolean updateForms = updateFormsStatusById(packageId, requestStatus);

    if (updatePackage && updateForms) {
      return true;
    } else {
      return false;
    }
  }

  public boolean updatePackageStatusById(long packageId, String requestStatus) {
    return helper.updatePackageStatusByPackageId(packageId, requestStatus);
  }

  public boolean updateFormsStatusById(long packageId, String requestStatus) {
    return helper.updateFormsStatusByPackageId(packageId, requestStatus);
  }

  private JSONObject createPackage(JSONObject pack, ArrayList<Document> forms) {

    JSONArray formArray = new JSONArray();

    for (Document form : forms) {
      formArray.put(new JSONObject(form.toJson()));
    }
    return pack.put(configuration.getFormsPropertyNameOut(), formArray);
  }

  public void processResponse(Response response, long packageId) {

    int responseStatus = response.getStatus();

    if (responseStatus == 202 || responseStatus == 200) {
      updateCompletePackageById(packageId, FormStatus.SUCCESS.toString().toUpperCase());
    } else {
      updateCompletePackageById(packageId, FormStatus.FAILED.toString().toUpperCase());
    }
  }
}
