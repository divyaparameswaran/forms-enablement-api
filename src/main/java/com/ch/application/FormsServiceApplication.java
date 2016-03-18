package com.ch.application;

import com.ch.auth.FormsApiAuthenticator;
import com.ch.configuration.FormsServiceConfiguration;
import com.ch.health.AppHealthCheck;
import com.ch.model.FormsApiUser;
import com.ch.resources.ChipsStubResource;
import com.ch.resources.ConversionResource;
import com.ch.resources.FormResponseResource;
import com.ch.resources.FormSubmissionResource;
import com.ch.resources.HealthcheckResource;
import com.ch.resources.HomeResource;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.util.logging.Logger;

import javax.ws.rs.client.Client;

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
    bootstrap.addBundle(new MultiPartBundle());
  }

  @Override
  public void run(FormsServiceConfiguration configuration, Environment environment) {

    // Authentication Filter for resources
    BasicCredentialAuthFilter authFilter = new BasicCredentialAuthFilter.Builder<FormsApiUser>()
        .setAuthenticator(new FormsApiAuthenticator(configuration))
        .setRealm(getName())
        .buildAuthFilter();

    AuthDynamicFeature feature = new AuthDynamicFeature(authFilter);
    environment.jersey().register(feature);

    // Jersey Client
    final Client client = new JerseyClientBuilder(environment)
        .using(configuration.getJerseyClientConfiguration())
        .withProvider(MultiPartFeature.class)
        .build(getName());

    // Resources
    environment.jersey().register(new ConversionResource());
    environment.jersey().register(new ChipsStubResource());
    environment.jersey().register(new FormSubmissionResource(client, configuration.getCompaniesHouseConfiguration()));
    environment.jersey().register(new FormResponseResource());
    environment.jersey().register(new HomeResource());
    environment.jersey().register(new HealthcheckResource());

    // MultiPart
    environment.jersey().register(MultiPartFeature.class);

    // Health checks
    final AppHealthCheck healthCheck =
        new AppHealthCheck();
    environment.healthChecks().register("AppHealthCheck", healthCheck);

    //Logging filter for input and output
    environment.jersey().register(new LoggingFilter(
        Logger.getLogger(LoggingFilter.class.getName()),
        true)
    );
  }

}
