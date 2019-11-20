package com.acmedcare.nas.server.ftp;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * {@link FtpServerProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019/11/19.
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = FtpServerProperties.NAS_FTP_PROPERTIES_PREFIX)
public class FtpServerProperties implements Serializable, InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(FtpServerProperties.class);

  static final String NAS_FTP_PROPERTIES_PREFIX = "nas.ftp";

  private static final String FTP_TEMP_PATH = "/ftp-files";

  private boolean enabled = false;

  // ===== Ftp Servlet Bootstrap Properties =====

  /**
   * FTP 服务模式， 默认值: 单机模式(STANDALONE)
   *
   * <p>
   */
  private Model model = Model.STANDALONE;

  /**
   * 本地存储路径
   *
   * <p>
   */
  private String localStoragePath;

  /**
   * Invoked by the containing {@code BeanFactory} after it has set all bean properties and
   * satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
   *
   * <p>This method allows the bean instance to perform validation of its overall configuration and
   * final initialization when all bean properties have been set.
   *
   * @throws Exception in the event of misconfiguration (such as failure to set an essential
   *     property) or if initialization fails for any other reason
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (StringUtils.isBlank(localStoragePath)) {
      this.localStoragePath = System.getProperty("user.dir").concat(FTP_TEMP_PATH);
    }

    log.info("[==FTP==] bean properties set-ed , data: {}", toString());
  }

  public enum Model {

    /**
     * Standalone model , only one server
     *
     * <p>
     */
    STANDALONE,

    /**
     * Cluster model , distribute ftp servers
     *
     * <p>
     */
    CLUSTER
  }
}
