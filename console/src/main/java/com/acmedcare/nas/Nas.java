package com.acmedcare.nas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Nas Main Class
 *
 * @author Elve.Xu [iskp.me<at>gmail.com]
 * @version v1.0 - 27/09/2018.
 */
@SpringBootApplication(scanBasePackages = "com.acmedcare.nas")
@ServletComponentScan
public class Nas {

  /**
   * Main Method For Application Startup
   *
   * @param args args
   */
  public static void main(String[] args) {
    SpringApplication.run(Nas.class, args);
  }
}
