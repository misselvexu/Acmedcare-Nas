package com.acmedcare.nas.sync.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SyncProperties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-01-21.
 */
@Component
@ConfigurationProperties(prefix = "sync", value = "sync.properties")
public class SyncProperties {

  // TODO sync properties

}
