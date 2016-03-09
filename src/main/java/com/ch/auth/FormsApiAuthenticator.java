package com.ch.auth;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

/**
 * Created by Eliot Stock on 08/03/2016.
 */
public class FormsApiAuthenticator implements Authenticator<BasicCredentials, FormsApiUser> {

  /**
   * Note this warning from the Dropwizard docs: You should only throw an AuthenticationException if the authenticator
   * is unable to check the credentials (e.g., your database is down).
   */
  @Override
  public Optional<FormsApiUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
    // TODO: Get password from an environment variable.
    if ("TODO_use_env_variable_here".equals(credentials.getPassword())) {
      return Optional.of(new FormsApiUser(credentials.getUsername()));
    }

    return Optional.absent();
  }

}
