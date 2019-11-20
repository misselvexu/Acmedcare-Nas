package com.acmedcare.nas.server.ftp;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.acmedcare.nas.server.ftp.FtpServerProperties.NAS_FTP_PROPERTIES_PREFIX;

/**
 * {@link FtpServerAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019/11/19.
 */
@Configuration
@EnableConfigurationProperties(FtpServerProperties.class)
@ConditionalOnProperty(prefix = NAS_FTP_PROPERTIES_PREFIX, name = "enabled", havingValue = "true")
public class FtpServerAutoConfiguration {



}
