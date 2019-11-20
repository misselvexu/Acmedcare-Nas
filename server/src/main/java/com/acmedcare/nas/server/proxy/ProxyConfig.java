package com.acmedcare.nas.server.proxy;

import com.acmedcare.nas.server.NasType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.io.Serializable;
import java.util.List;

import static com.acmedcare.nas.common.kits.SystemKits.LOCAL_IP;
import static com.acmedcare.nas.common.kits.SystemKits.NAS_LOCAL_IP_KEY;
import static com.acmedcare.nas.server.proxy.ProxyConfig.NAS_PROXY_PREFIX;
import static lombok.AccessLevel.PRIVATE;

/**
 * Proxy Config
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version v1.0 - 28/08/2018.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = NAS_PROXY_PREFIX)
public class ProxyConfig implements Serializable, EnvironmentAware, InitializingBean {

  private static final long serialVersionUID = -3047132860550946253L;

  private static final Logger logger = LoggerFactory.getLogger(ProxyConfig.class);

  public static final String NAS_PROXY_PREFIX = "nas.proxy";

  private static final String LOCAL_DEFAULT_URL = "http://127.0.0.1:9333";

  private static final String UPLOAD_URI = "/submit";

  private static final String SERVLET_CONTEXT_PATH = "server.servlet.context-path";

  @Getter(value = PRIVATE)
  private Environment environment;

  @Getter(value = PRIVATE)
  @Setter(value = PRIVATE)
  private static String submitURI;

  // ====== Config Properties ========

  /** 是否开启 log */
  private String log;

  /**
   * 文件服务器类
   *
   * @see NasType
   */
  private NasType nasType = NasType.DFS;

  /**
   * Proxy Servlet Base URL Mapping
   *
   * <p>
   */
  private String contextPath = "/nas";

  /**
   * Real Server URL
   *
   * <pre>
   *   Supported Server:
   *      - Http Server
   *      - Ftp Server
   * </pre>
   */
  private List<String> targetUrls;

  /**
   * 文件输出路径模板
   *
   * <p>
   */
  private String exportUrlTemplate;

  // ====== Config Properties Methods ========

  public String getTargetUrl() {
    if (targetUrls != null && targetUrls.size() > 0) {
      // choose url (random default)
      String targetUrl = targetUrls.get(RandomUtils.nextInt(0, targetUrls.size()));
      if (StringUtils.isNoneBlank(targetUrl) && targetUrl.endsWith("/")) {
        targetUrl = targetUrl.substring(0, targetUrl.length() - 1);
      }
      return targetUrl;
    }
    return LOCAL_DEFAULT_URL;
  }

  public boolean isUploadRequestURI(String requestURI) {
    return submitURI.equals(requestURI);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    ProxyConfig.submitURI =
        environment
            .getProperty(SERVLET_CONTEXT_PATH, "/")
            .concat(this.contextPath)
            .concat(UPLOAD_URI);

    logger.info("[==Nas==] Proxy config's submit uri: {}", ProxyConfig.submitURI);
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  /**
   * Render Request Url
   *
   * @param fid file id
   * @return url
   */
  public String renderLink(String fid) {
    return String.format(this.exportUrlTemplate.replace(NAS_LOCAL_IP_KEY, LOCAL_IP), fid);
  }
}
