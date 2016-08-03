package com.ch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Aaron.Witter on 01/08/2016.
 */
public class PresenterAuthRequest {

  private String presenterId;
  private String presenterAuth;

  public PresenterAuthRequest() {// empty constructor for de serialisation
  }

  public PresenterAuthRequest(String presenterId, String presenterAuth) {
    this.presenterId = presenterId;
    this.presenterAuth = presenterAuth;
  }

  @JsonProperty
  public String getPresenterId() {
    return presenterId;
  }

  @JsonProperty
  public String getPresenterAuth() {
    return presenterAuth;
  }
}