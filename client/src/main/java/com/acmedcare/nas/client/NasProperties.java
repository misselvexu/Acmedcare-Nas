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

  /** app id */
  private String appId;

  /** app key */
  private String appKey;

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

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }
}
