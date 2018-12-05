package com.acmedcare.nas.exts.api;

import com.acmedcare.nas.exts.api.properties.AutoConfigureProperties;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Nas Base Properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
public class NasProperties extends Properties implements Serializable {

  private static final long serialVersionUID = 584891296601423292L;

  /**
   * Decode Properties Bean
   *
   * @param aClass target class type
   * @param <T> class type
   * @return instance of assigned bean
   */
  public <T> T decodePropertiesBean(Class<? extends AutoConfigureProperties> aClass) {

    Field[] fields = aClass.getDeclaredFields();

    return null;
  }
}
