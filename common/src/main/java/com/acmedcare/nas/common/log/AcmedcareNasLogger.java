package com.acmedcare.nas.common.log;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Acmedcare Logger Utils
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version v1.0 - 28/08/2018.
 */
@UtilityClass
public final class AcmedcareNasLogger {

  private static final String LOGGER_NAME = "NAS-APP-LOGGER";

  private static final Logger LOGGER = LoggerFactory.getLogger(AcmedcareNasLogger.class);

  /**
   * Logger Factory Method
   *
   * @return logger instance
   */
  public static Logger logger() {
    return LOGGER;
  }
}
