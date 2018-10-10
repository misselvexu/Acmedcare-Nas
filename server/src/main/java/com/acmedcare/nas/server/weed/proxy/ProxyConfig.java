package com.acmedcare.nas.server.weed.proxy;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Proxy Config
 *
 * @author Elve.Xu [iskp.me<at>gmail.com]
 * @version v1.0 - 28/08/2018.
 */
@Getter
@Setter
@PropertySource(value = "classpath:proxy.properties", ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "proxy")
@Configuration
public class ProxyConfig implements Serializable {

  private static final long serialVersionUID = -3047132860550946253L;

  private static final String LOCAL_DEFAULT_URL = "http://127.0.0.1:9333";

  /** 是否开启 log */
  private String log;

  /** 转发地址 */
  private List<String> targetUrls;

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
}
