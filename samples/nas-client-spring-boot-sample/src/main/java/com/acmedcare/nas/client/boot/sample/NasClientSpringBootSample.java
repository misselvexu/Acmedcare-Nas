package com.acmedcare.nas.client.boot.sample;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * {@link NasClientSpringBootSample}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-04-26.
 */
@SpringBootApplication
public class NasClientSpringBootSample {

  public static void main(String[] args) {
    new SpringApplicationBuilder(NasClientSpringBootSample.class)
        .web(WebApplicationType.NONE)
        .run(args);
  }
}
