package com.acmedcare.nas.client.exts.qiniu;

import com.acmedcare.nas.api.Extension;
import com.acmedcare.nas.exts.api.NasProperties;
import com.acmedcare.nas.exts.api.exception.NasContextException;
import com.acmedcare.nas.exts.api.properties.NasPropertiesLoader;

/**
 * QiNiu Properties Loader
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-05.
 */
@Extension("qiniu")
public class QiNiuNasPropertiesLoader implements NasPropertiesLoader<QiNiuProperties> {

  /**
   * get property file name
   *
   * @return file name
   */
  @Override
  public String propertyFileName() {
    return "qiniu.properties";
  }

  /**
   * Load properties from system config
   *
   * @return instance of {@link NasProperties}
   * @throws NasContextException exception
   */
  @Override
  public QiNiuProperties loadProperties() throws NasContextException {
    return null;
  }
}
