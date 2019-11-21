package com.acmedcare.nas.server;

import com.acmedcare.framework.kits.servlet.filter.compression.CompressingFilter;
import com.acmedcare.nas.server.proxy.ProxyConfig;
import com.acmedcare.nas.server.proxy.URITemplateProxyServlet;
import com.acmedcare.nas.server.proxy.filter.AuthHeaderInterceptor;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Nas Server Auto Configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-11-30.
 */
@Configuration
@EnableConfigurationProperties({NasProperties.class})
public class NasAutoConfiguration {

  @Configuration
  public static class ApplicationConfigurations {

    @Getter static ProxyConfig proxyConfig;

    static NasProperties nasConfig;

    public ApplicationConfigurations(NasProperties nasConfig) {
      ApplicationConfigurations.nasConfig = nasConfig;
      ApplicationConfigurations.proxyConfig = nasConfig.getProxy();
    }

    @Bean
    public ServletRegistrationBean nasServlet() {
      return new ServletRegistrationBean<>(
          new URITemplateProxyServlet(
              ApplicationConfigurations.nasConfig,
              // Interceptor List
              new AuthHeaderInterceptor()),
          // servlet mapping
          ApplicationConfigurations.proxyConfig.getContextPath() + "/*");
    }

    @Bean
    public FilterRegistrationBean<CompressingFilter> newServletFilter() {
      FilterRegistrationBean<CompressingFilter> registrationBean = new FilterRegistrationBean<>();

      CompressingFilter compressingFilter = new CompressingFilter();
      registrationBean.setFilter(compressingFilter);

      List<String> urlPatterns = Lists.newArrayList();
      urlPatterns.add(ApplicationConfigurations.proxyConfig.getContextPath() + "/*");

      registrationBean.setUrlPatterns(urlPatterns);
      registrationBean.addInitParameter("debug", "true");
      registrationBean.addInitParameter("compressionLevel", "9");
      registrationBean.addInitParameter(
          "includeContentTypes", ApplicationConfigurations.nasConfig.getIncludeContentTypes());

      return registrationBean;
    }
  }
}
