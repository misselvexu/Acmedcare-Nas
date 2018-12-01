package com.acmedcare.nas.client;

import java.util.List;

/**
 * Nas Properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
public class NasProperties {

  /**
   * Nas FS Server Address List
   *
   * <p>
   */
  private List<String> serverAddrs;

  /** is https */
  private boolean https;

  public List<String> getServerAddrs() {
    return serverAddrs;
  }

  public void setServerAddrs(List<String> serverAddrs) {
    this.serverAddrs = serverAddrs;
  }

  public boolean isHttps() {
    return https;
  }

  public void setHttps(boolean https) {
    this.https = https;
  }
}
