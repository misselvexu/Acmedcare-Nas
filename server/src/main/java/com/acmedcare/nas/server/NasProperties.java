package com.acmedcare.nas.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

import static com.acmedcare.nas.server.NasProperties.NAS_CONFIG_PREFIX;

/**
 * {@link NasProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019/11/20.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = NAS_CONFIG_PREFIX)
public class NasProperties implements Serializable {

  private static final long serialVersionUID = -8718095574026379565L;

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
  @Value("${server.compression.mime-types}")
  private String includeContentTypes;

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

  @Getter
  @Setter
  @NoArgsConstructor
  public static class AclProperties implements Serializable {

    public static final String NAS_ACL_CONFIG_PREFIX = "nas.acl";


  }
}
