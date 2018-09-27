package com.acmedcare.nas.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

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
}
