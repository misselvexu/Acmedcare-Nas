package com.acmedcare.nas.ftp.server;

import com.acmedcare.nas.ftp.server.impl.DefaultConnectionConfig;

/**
 * Factory for creating connection configurations
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ConnectionConfigFactory {

  private int maxLogins = 10;

  private boolean anonymousLoginEnabled = true;

  private int maxAnonymousLogins = 10;

  private int maxLoginFailures = 3;

  private int loginFailureDelay = 500;

  private int maxThreads = 0;

  /**
   * Create a connection configuration instances based on the configuration on this factory
   *
   * @return The {@link ConnectionConfig} instance
   */
  public ConnectionConfig createConnectionConfig() {
    return new DefaultConnectionConfig(
        anonymousLoginEnabled,
        loginFailureDelay,
        maxLogins,
        maxAnonymousLogins,
        maxLoginFailures,
        maxThreads);
  }

  /**
   * The delay in number of milliseconds between login failures. Important to make brute force
   * attacks harder.
   *
   * @return The delay time in milliseconds
   */
  public int getLoginFailureDelay() {
    return loginFailureDelay;
  }

  /**
   * Set the delay in number of milliseconds between login failures. Important to make brute force
   * attacks harder.
   *
   * @param loginFailureDelay The delay time in milliseconds
   */
  public void setLoginFailureDelay(final int loginFailureDelay) {
    this.loginFailureDelay = loginFailureDelay;
  }

  /**
   * The maximum number of anonymous logins the server would allow at any given time
   *
   * @return The maximum number of anonymous logins
   */
  public int getMaxAnonymousLogins() {
    return maxAnonymousLogins;
  }

  /**
   * Sets the maximum number of anonymous logins the server would allow at any given time
   *
   * @param maxAnonymousLogins The maximum number of anonymous logins
   */
  public void setMaxAnonymousLogins(final int maxAnonymousLogins) {
    this.maxAnonymousLogins = maxAnonymousLogins;
  }

  /**
   * The maximum number of time an user can fail to login before getting disconnected
   *
   * @return The maximum number of failure login attempts
   */
  public int getMaxLoginFailures() {
    return maxLoginFailures;
  }

  /**
   * Set the maximum number of time an user can fail to login before getting disconnected
   *
   * @param maxLoginFailures The maximum number of failure login attempts
   */
  public void setMaxLoginFailures(final int maxLoginFailures) {
    this.maxLoginFailures = maxLoginFailures;
  }

  /**
   * The maximum number of concurrently logged in users
   *
   * @return The maximum number of users
   */
  public int getMaxLogins() {
    return maxLogins;
  }

  /**
   * Set she maximum number of concurrently logged in users
   *
   * @param maxLogins The maximum number of users
   */
  public void setMaxLogins(final int maxLogins) {
    this.maxLogins = maxLogins;
  }

  /**
   * Is anonymous logins allowed at the server?
   *
   * @return true if anonymous logins are enabled
   */
  public boolean isAnonymousLoginEnabled() {
    return anonymousLoginEnabled;
  }

  /**
   * Set if anonymous logins are allowed at the server
   *
   * @param anonymousLoginEnabled true if anonymous logins should be enabled
   */
  public void setAnonymousLoginEnabled(final boolean anonymousLoginEnabled) {
    this.anonymousLoginEnabled = anonymousLoginEnabled;
  }

  /**
   * Returns the maximum number of threads the server is allowed to create for processing client
   * requests.
   *
   * @return the maximum number of threads the server is allowed to create for processing client
   *     requests.
   */
  public int getMaxThreads() {
    return maxThreads;
  }

  /**
   * Sets the maximum number of threads the server is allowed to create for processing client
   * requests.
   *
   * @param maxThreads the maximum number of threads the server is allowed to create for processing
   *     client requests.
   */
  public void setMaxThreads(int maxThreads) {
    this.maxThreads = maxThreads;
  }
}
