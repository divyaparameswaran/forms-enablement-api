package com.ch.application;

import com.ch.auth.FormsApiAuthenticator;
import com.ch.auth.FormsApiUser;
import com.ch.configuration.FormsServiceConfiguration;
import com.ch.health.TemplateHealthCheck;
import com.ch.resources.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by Aaron.Witter on 07/03/2016.
 */
@SuppressWarnings("PMD")
public class FormsServiceApplication extends Application<FormsServiceConfiguration> {

  public static final String NAME = "Forms API Service";

  public static void main(String[] args) throws Exception {
    new FormsServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void initialize(Bootstrap<FormsServiceConfiguration> bootstrap) {
    // nothing to do yet
  }

  @Override
  public void run(FormsServiceConfiguration configuration, Environment environment) {
    // Basic HTTP authentication. Note that we don't have an authoriser here, just an authenticator.
    BasicCredentialAuthFilter authFilter = new BasicCredentialAuthFilter.Builder<FormsApiUser>()
            .setAuthenticator(new FormsApiAuthenticator())
            .setRealm(getName())
            .buildAuthFilter();

    AuthDynamicFeature feature = new AuthDynamicFeature(authFilter);
    environment.jersey().register(feature);

    // Resources
    environment.jersey().register(new HelloWorldResource());

    // Health checks
    environment.healthChecks().register("template", new TemplateHealthCheck(configuration.getTemplate()));
  }

}
