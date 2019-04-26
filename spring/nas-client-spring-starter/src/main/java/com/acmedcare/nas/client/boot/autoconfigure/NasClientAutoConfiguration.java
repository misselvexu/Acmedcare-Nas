package com.acmedcare.nas.client.boot.autoconfigure;

import com.acmedcare.nas.client.NasClient;
import com.acmedcare.nas.client.NasClientFactory;
import com.acmedcare.nas.client.boot.config.NasClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static com.acmedcare.nas.client.boot.config.NasClientProperties.NAS_CLIENT_PROPERTIES_PREFIX;

/**
 * {@link NasClientAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-04-26.
 */
@Configuration
@EnableConfigurationProperties(NasClientProperties.class)
public class NasClientAutoConfiguration {

  @ConditionalOnProperty(
      prefix = NAS_CLIENT_PROPERTIES_PREFIX,
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  @Bean
  @Primary
  @ConditionalOnMissingBean
  public NasClient nasClient(NasClientProperties properties) {
    properties.validate();
    return NasClientFactory.createNewNasClient(properties);
  }
}
