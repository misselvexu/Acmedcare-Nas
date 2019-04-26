package com.acmedcare.nas.client.boot.config;

import com.acmedcare.nas.client.NasProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import static com.acmedcare.nas.client.boot.config.NasClientProperties.NAS_CLIENT_PROPERTIES_PREFIX;

/**
 * {@link NasClientProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-04-26.
 */
@ConfigurationProperties(prefix = NAS_CLIENT_PROPERTIES_PREFIX)
public class NasClientProperties extends NasProperties {

  /**
   * Nas Client Properties Prefix
   */
  public static final String NAS_CLIENT_PROPERTIES_PREFIX = "nas.client";

  /**
   * validate config properties
   */
  public void validate() {
    if (getServerAddrs() == null || getServerAddrs().isEmpty()) {
      throw new IllegalArgumentException(
          "nas client's properties [nas.client.server-addrs] must be set.");
    }

    if (!StringUtils.hasText(getAppId()) || !StringUtils.hasText(getAppKey())) {
      throw new IllegalArgumentException(
          "nas client's properties [nas.client.app-id] or [nas.client.app-key] must be set.");
    }
  }
}
