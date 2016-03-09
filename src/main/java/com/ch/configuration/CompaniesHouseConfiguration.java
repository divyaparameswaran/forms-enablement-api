package com.ch.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Aaron.Witter on 09/03/2016.
 */
public class CompaniesHouseConfiguration {
  @JsonProperty
  @NotEmpty
  private String secret;

  @JsonProperty
  @NotEmpty
  private String apiGetUrl;

  @JsonProperty
  @NotEmpty
  private String name;

  public String getSecret() {
    return secret;
  }

  public String getApiGetUrl() {
    return apiGetUrl;
  }

  public String getName() {
    return name;
  }
}