/*
 * Copyright 1999-2018 Acmedcare+ Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acmedcare.nas.server;

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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Nas Server Main Class
 *
 * @author Elve.Xu [iskp.me<at>gmail.com]
 * @version v1.0 - 27/09/2018.
 */
@SpringBootApplication(scanBasePackages = "com.acmedcare.nas.server")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ServletComponentScan
public class NasServer {

  /**
   * Main Method For Application Startup
   *
   * @param args args
   */
  public static void main(String[] args) {
    SpringApplication.run(NasServer.class, args);
  }

  @Configuration
  public static class ApplicationContext {

    @Getter private static ProxyConfig proxyConfig;

    @Getter private static NasConfig nasConfig;

    @Autowired
    public void setNasConfig(NasConfig nasConfig) {
      ApplicationContext.nasConfig = nasConfig;
    }

    @Bean
    public ServletRegistrationBean nasServlet() {
      return new ServletRegistrationBean<URITemplateProxyServlet>(
          new URITemplateProxyServlet(
              ApplicationContext.getProxyConfig(),
              // Interceptor List
              new AuthHeaderInterceptor()),
          // servlet mapping
          "/nas/*");
    }

    @Bean
    @SuppressWarnings("unchecked")
    public FilterRegistrationBean<CompressingFilter> newServletFilter() {
      FilterRegistrationBean registrationBean = new FilterRegistrationBean();

      CompressingFilter compressingFilter = new CompressingFilter();
      registrationBean.setFilter(compressingFilter);

      List<String> urlPatterns = Lists.newArrayList();
      urlPatterns.add("/nas/*");

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
      return String.format(this.exportUrlTemplate, fid);
    }
  }
}
