package com.acmedcare.nas.server;

import com.acmedcare.nas.server.ftp.FtpServerProperties;
import com.acmedcare.nas.server.proxy.ProxyConfig;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.acmedcare.nas.server.NasProperties.NAS_CONFIG_PREFIX;
import static lombok.AccessLevel.PRIVATE;

/**
 * {@link NasProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019/11/20.
 */
@Getter
@Setter
@ToString(exclude = {"environment"})
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = NAS_CONFIG_PREFIX)
public class NasProperties implements Serializable, InitializingBean, EnvironmentAware {

  private static final long serialVersionUID = -8718095574026379565L;

  private static final Logger log = LoggerFactory.getLogger(NasProperties.class);

  public static final String NAS_CONFIG_PREFIX = "nas";

  /**
   * 应用上下文请求路径， 默认获取:`${server.servlet.context-path}`的值
   *
   * <p>
   */
  @Value("${server.servlet.context-path:/nas}")
  private String contextPath;

  /**
   * 文件压缩内容类型指定(<code>Content-Type</code>) ,默认获取: ${server.compression.mime-types}值
   *
   * <p>
   */
  @Value("${server.compression.mime-types:}")
  private String includeContentTypes;

  /**
   * Ftp Config Properties
   *
   * <p>
   */
  @NestedConfigurationProperty private FtpServerProperties ftp;

  /**
   * Proxy Config Properties
   *
   * <p>
   */
  @NestedConfigurationProperty private ProxyConfig proxy;

  /**
   * Access Control Lists Enabled Flag
   *
   * <p>default: false
   */
  private boolean aclEnabled = false;

  /**
   * Acl Config Properties
   *
   * <p>Condition: <code>nas.acl-enabled=true</code>
   */
  @NestedConfigurationProperty private AclProperties acl;

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
    if (proxy != null) {
      proxy.afterPropertiesSet(this.environment);
      // check
      if (ftp != null && proxy.getNasType().equals(NasType.FTP) && ftp.isEnabled()) {
        ftp.afterPropertiesSet();
      }
    } else {
      throw new IllegalArgumentException("missing nas proxy config properties , @nas.proxy.xxx ");
    }

    log.info("[==Nas Properties==] loaded , data : {}" , toString());
  }

  @Getter(value = PRIVATE)
  private Environment environment;

  @Override
  public void setEnvironment(@Nonnull Environment environment) {
    this.environment = environment;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  public static class AclProperties implements Serializable {

    public static final String NAS_ACL_CONFIG_PREFIX = "nas.acl";
  }
}
