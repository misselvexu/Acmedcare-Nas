package com.acmedcare.nas.client;

import com.acmedcare.nas.api.exception.NasException;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Nas Client Factory
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
public class NasClientFactory {

  /**
   * Create New {@link NasClient} instance
   *
   * @param properties config properties
   * @return a instance of {@link NasClient}
   * @throws NasException exception
   * @see NasProperties
   */
  public static NasClient createNewNasClient(NasProperties properties) throws NasException {
    try {
      if (properties == null
          || properties.getServerAddrs() == null
          || properties.getServerAddrs().isEmpty()) {
        throw new NasException(
            "Nas Client init failed with invalid properties [serverAddrs , https] .");
      }
      Class<?> aClass = Class.forName("com.acmedcare.nas.client.NasClient");
      Constructor constructor = aClass.getConstructor(List.class, boolean.class);
      Object instance = constructor.newInstance(properties.getServerAddrs(), properties.isHttps());
      return (NasClient) instance;
    } catch (NasException e) {
      throw e;
    } catch (Exception e) {
      throw new NasException("Create nas client instance failed", e);
    }
  }
}
