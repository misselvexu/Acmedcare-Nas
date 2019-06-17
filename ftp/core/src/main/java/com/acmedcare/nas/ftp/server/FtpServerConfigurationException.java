package com.acmedcare.nas.ftp.server;

/**
 * Exception used during startup to indicate that the configuration is not correct.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class FtpServerConfigurationException extends RuntimeException {

  private static final long serialVersionUID = -1328432839915898987L;

  /** {@link RuntimeException#RuntimeException()} */
  public FtpServerConfigurationException() {
    super();
  }

  /** {@link RuntimeException#RuntimeException(String, Throwable)} */
  public FtpServerConfigurationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /** {@link RuntimeException#RuntimeException(String)} */
  public FtpServerConfigurationException(final String message) {
    super(message);
  }

  /** {@link RuntimeException#RuntimeException(Throwable)} */
  public FtpServerConfigurationException(final Throwable cause) {
    super(cause);
  }
}
