package com.acmedcare.nas.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * NasSyncBootstrap
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-01-21.
 */
@EnableAutoConfiguration
public class NasSyncBootstrap {

  public static void main(String[] args) {
    SpringApplication.run(NasSyncBootstrap.class, args);
  }
}
