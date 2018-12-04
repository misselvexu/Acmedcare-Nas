package com.acmedcare.nas.exts.api.properties;

import com.acmedcare.nas.exts.api.NasProperties;

/**
 * Auto Configure Properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-05.
 */
public abstract class AutoConfigureProperties extends NasProperties {

  /**
   * return custom properties key prefix
   *
   * @return name
   */
  protected abstract String getPropertiesPrefix();
}
