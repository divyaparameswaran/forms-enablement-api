package com.ch.model;


import org.json.JSONObject;

import java.util.List;

/**
 * Created by elliott.jenkins on 05/04/2016.
 */
public class FormsPackage {
    private final String packageMetaData;
    private final List<String> forms;

    public FormsPackage(String packageMetaData, List<String> forms) {
        this.packageMetaData = packageMetaData;
        this.forms = forms;
    }

    public String getPackageMetaData() {
        return packageMetaData;
    }

    public JSONObject getPackageMetaDataJson() {
        return new JSONObject(packageMetaData);
    }

    public List<String> getForms() {
        return forms;
    }
}
