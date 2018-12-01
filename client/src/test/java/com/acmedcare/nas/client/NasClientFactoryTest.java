package com.acmedcare.nas.client;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

/**
 * com.acmedcare.nas.client
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
public class NasClientFactoryTest {

  @Test
  public void createNewNasClient() {
    try {
      NasClientFactory.createNewNasClient(null);
    } catch (Exception e) {
      Assert.assertEquals(
          "Nas Client init failed with invalid properties [serverAddrs , https] .", e.getMessage());
    }
  }

  @Test
  public void createNewNasClient2() {
    try {
      NasProperties nasProperties = new NasProperties();
      NasClientFactory.createNewNasClient(nasProperties);
    } catch (Exception e) {
      Assert.assertEquals(
          "Nas Client init failed with invalid properties [serverAddrs , https] .", e.getMessage());
    }
  }

  @Test
  public void createNewNasClient3() {
    NasProperties nasProperties = new NasProperties();
    nasProperties.setServerAddrs(Lists.newArrayList("192.168.1.1:8080"));
    NasClient nasClient = NasClientFactory.createNewNasClient(nasProperties);
    Assert.assertNotNull(nasClient);
  }
}
