package com.acmedcare.nas.ftp.server.ftplet;

/**
 * Ftplet exception class.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class FtpException extends Exception {

  private static final long serialVersionUID = -1328383839915898987L;

  /**
   * Default constructor.
   */
  public FtpException() {
    super();
  }

  /**
   * Constructs a <code>FtpException</code> object with a message.
   *
   * @param msg a description of the exception
   */
  public FtpException(String msg) {
    super(msg);
  }

  /**
   * Constructs a <code>FtpException</code> object with a <code>Throwable</code> cause.
   *
   * @param th the original cause
   */
  public FtpException(Throwable th) {
    super(th.getMessage());
  }

  /**
   * Constructs a <code>BaseException</code> object with a <code>Throwable</code> cause.
   *
   * @param msg A description of the exception
   * @param th  The original cause
   */
  public FtpException(String msg, Throwable th) {
    super(msg);
  }

  /**
   * Get the root cause.
   *
   * @return The root cause
   * @deprecated Use {@link Exception#getCause()} instead
   */
  @Deprecated
  public Throwable getRootCause() {
    return getCause();
  }
}
