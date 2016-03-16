package com.ch.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * Created by Aaron.Witter on 07/03/2016.
 */
public class FormsServiceConfiguration extends Configuration {
  @JsonProperty
  private SalesforceConfiguration salesforceConfiguration;

  @JsonProperty
  private  CompaniesHouseConfiguration companiesHouseConfiguration;

  public SalesforceConfiguration getSalesforceConfiguration() {
    return salesforceConfiguration;
  }

  public CompaniesHouseConfiguration getCompaniesHouseConfiguration() {
    return companiesHouseConfiguration;
  }
}