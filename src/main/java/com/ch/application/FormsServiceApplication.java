package com.ch.application;

import com.ch.configuration.FormsServiceConfiguration;
import com.ch.health.TemplateHealthCheck;
import com.ch.resources.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


/**
 * Created by Aaron.Witter on 07/03/2016.
 */
@SuppressWarnings("PMD")
public class FormsServiceApplication extends Application<FormsServiceConfiguration> {
  public static void main(String[] args) throws Exception {
    new FormsServiceApplication().run(args);
  }

  @Override
  public String getName() {
    return "hello-world";
  }

  @Override
  public void initialize(Bootstrap<FormsServiceConfiguration> bootstrap) {
    // nothing to do yet
  }

  @Override
  public void run(FormsServiceConfiguration configuration,
                  Environment environment) {

    environment.jersey().register(new HelloWorldResource());

    environment.healthChecks().register("template", new TemplateHealthCheck(configuration.getTemplate()));

  }
}
