package com.acmedcare.nas.exts.api.properties;

import com.acmedcare.nas.api.Extensible;
import com.acmedcare.nas.exts.api.NasProperties;
import com.acmedcare.nas.exts.api.exception.NasContextException;

/**
 * Properties Loader
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-05.
 */
@Extensible
public interface NasPropertiesLoader<T> {

  /**
   * get property file name
   *
   * @return file name
   */
  String propertyFileName();

  /**
   * Load properties from system config
   *
   * @param <T> instance class type
   * @return instance of {@link NasProperties}
   * @throws NasContextException exception
   */
  T loadProperties() throws NasContextException;
}
