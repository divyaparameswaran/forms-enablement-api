package com.ch.conversion.helpers;

import com.ch.application.FormServiceConstants;
import com.ch.conversion.config.ITransformConfig;
import com.ch.helpers.MongoHelper;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron.Witter on 18/07/2016.
 */
public class QueueHelper {
  public ITransformConfig configuration;

  public QueueHelper(ITransformConfig configuration){
    this.configuration = configuration;
  }

  private MongoHelper helper = MongoHelper.getInstance();

  public List<JSONObject> getCompletePackages(int count, String requestStatus){

    List<JSONObject> completeForms = new ArrayList<>();

    //get all packages matching criteria
    ArrayList<Document> packages = helper.getPackagesCollectionByStatus(requestStatus, count).into(new ArrayList<Document>());

    for(Document pack: packages){

      //for each package search the database for forms matching the identifier
      ArrayList<Document> forms = helper.getFormsCollectionByPackageId((int) pack.get(FormServiceConstants.PACKAGE_IDENTIFIER))
        .into(new ArrayList<Document>());

      //create a complete package from the packagemetadata and it's acoompanying forms.
      completeForms.add(createPackage(new JSONObject(pack.toJson()),forms));
    }
    return completeForms;
  }

  private JSONObject createPackage(JSONObject pack, ArrayList<Document> forms){

    JSONArray formArray = new JSONArray();

    for(Document form: forms){
      formArray.put(new JSONObject(form.toJson()));
    }
    return pack.put(configuration.getFormsPropertyNameOut(), formArray);
  }
}
