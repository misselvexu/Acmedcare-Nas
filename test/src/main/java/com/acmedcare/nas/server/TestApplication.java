package com.acmedcare.nas.server;

import com.acmedcare.framework.newim.master.endpoint.client.MasterEndpointClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * com.acmedcare.nas.server
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
@SpringBootApplication
public class TestApplication {

  public static void main(String[] args) {
    //
    SpringApplication.run(TestApplication.class, args);
  }

  @Configuration
  public static class EndpointAutoConfiguration {

    @Autowired private MasterEndpointClient masterEndpointClient;
  }
}
