package com.acmedcare.nas.server;

import static com.acmedcare.nas.common.kits.SystemKits.LOCAL_IP;
import static com.acmedcare.nas.common.kits.SystemKits.NAS_LOCAL_IP_KEY;

import com.acmedcare.framework.kits.servlet.filter.compression.CompressingFilter;
import com.acmedcare.nas.server.proxy.filter.AuthHeaderInterceptor;
import com.acmedcare.nas.server.weed.proxy.ProxyConfig;
import com.acmedcare.nas.server.weed.proxy.URITemplateProxyServlet;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Nas Server Auto Configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-11-30.
 */
@Configuration
@EnableConfigurationProperties(ProxyConfig.class)
public class NasConfigurationRepository {

  @Configuration
  public static class ApplicationContext {

    @Getter private static ProxyConfig proxyConfig;

    @Getter private static NasConfig nasConfig;

    @Autowired
    public void setNasConfig(NasConfig nasConfig) {
      ApplicationContext.nasConfig = nasConfig;
    }

    @Autowired
    public void setProxyConfig(ProxyConfig proxyConfig) {
      ApplicationContext.proxyConfig = proxyConfig;
    }

    @Bean
    public ServletRegistrationBean nasServlet() {
      return new ServletRegistrationBean<URITemplateProxyServlet>(
          new URITemplateProxyServlet(
              ApplicationContext.getProxyConfig(),
              // Interceptor List
              new AuthHeaderInterceptor()),
          // servlet mapping
          ApplicationContext.getProxyConfig().getContextPath() + "/*");
    }

    @Bean
    @SuppressWarnings("unchecked")
    public FilterRegistrationBean<CompressingFilter> newServletFilter() {
      FilterRegistrationBean registrationBean = new FilterRegistrationBean();

      CompressingFilter compressingFilter = new CompressingFilter();
      registrationBean.setFilter(compressingFilter);

      List<String> urlPatterns = Lists.newArrayList();
      urlPatterns.add(ApplicationContext.getProxyConfig().getContextPath() + "/*");

      registrationBean.setUrlPatterns(urlPatterns);
      registrationBean.addInitParameter("debug", "true");
      registrationBean.addInitParameter("compressionLevel", "9");
      registrationBean.addInitParameter(
          "includeContentTypes", ApplicationContext.getNasConfig().getIncludeContentTypes());

      return registrationBean;
    }
  }

  /** Nas Config */
  @Getter
  @Setter
  @Configuration
  public static class NasConfig implements Serializable {

    private static final long serialVersionUID = -8718095574026379565L;

    @Value("${server.servlet.context-path:/nas}")
    private String contextPath;

    @Value("${nas.export.url.template}")
    private String exportUrlTemplate;

    @Value("${server.compression.mime-types}")
    private String includeContentTypes;

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
}
