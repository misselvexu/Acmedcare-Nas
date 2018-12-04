package com.acmedcare.nas.client.exts.qiniu;

import com.acmedcare.nas.api.Extension;
import com.acmedcare.nas.exts.api.properties.AbstractNasPropertiesLoader;

/**
 * QiNiu Properties Loader
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-05.
 */
@Extension("qiniu")
public class QiNiuNasPropertiesLoader extends AbstractNasPropertiesLoader<QiNiuProperties> {

  /**
   * get property file name
   *
   * @return file name
   */
  @Override
  public String propertyFileName() {
    return "qiniu.properties";
  }
}
