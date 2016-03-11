package com.ch.application;

import com.ch.auth.FormsApiAuthenticator;
import com.ch.configuration.FormsServiceConfiguration;
import com.ch.model.FormsApiUser;
import com.ch.resources.FormSubmissionResource;
import com.ch.resources.HomeResource;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.filter.LoggingFilter;

import java.util.logging.Logger;

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
    bootstrap.addBundle(new TemplateConfigBundle());
  }

  @Override
  public void run(FormsServiceConfiguration configuration, Environment environment) {
    BasicCredentialAuthFilter authFilter = new BasicCredentialAuthFilter.Builder<FormsApiUser>()
        .setAuthenticator(new FormsApiAuthenticator(configuration))
        .setRealm(getName())
        .buildAuthFilter();

    AuthDynamicFeature feature = new AuthDynamicFeature(authFilter);
    environment.jersey().register(feature);

    // Resources
    environment.jersey().register(new FormSubmissionResource());
    environment.jersey().register(new HomeResource());

    // Health checks

    //Logging filter for input and output
    environment.jersey().register(new LoggingFilter(
        Logger.getLogger(LoggingFilter.class.getName()),
        true)
    );

    //TODO healthchecks
  }

}
