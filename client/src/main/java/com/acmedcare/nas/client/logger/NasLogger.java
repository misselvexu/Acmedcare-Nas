package com.acmedcare.nas.client.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Nas Logger
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
public class NasLogger {

  private static final Logger LOG = LoggerFactory.getLogger("NAS-CLIENT");

  public static Logger logger() {
    return LOG;
  }
}
