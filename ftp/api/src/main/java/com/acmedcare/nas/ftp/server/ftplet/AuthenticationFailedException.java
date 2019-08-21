package com.acmedcare.nas.ftp.server.ftplet;

/**
 * Thrown if an authentication request fails
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class AuthenticationFailedException extends FtpException {
  private static final long serialVersionUID = -1328383839915898987L;

  /**
   * Default constructor.
   */
  public AuthenticationFailedException() {
    super();
  }

  /**
   * Constructs a <code>AuthenticationFailedException</code> object with a message.
   *
   * @param msg A description of the exception
   */
  public AuthenticationFailedException(String msg) {
    super(msg);
  }

  /**
   * Constructs a <code>AuthenticationFailedException</code> object with a <code>Throwable</code>
   * cause.
   *
   * @param th The original cause
   */
  public AuthenticationFailedException(Throwable th) {
    super(th);
  }

  /**
   * Constructs a <code>AuthenticationFailedException</code> object with a <code>Throwable</code>
   * cause.
   *
   * @param msg A description of the exception
   * @param th  The original cause
   */
  public AuthenticationFailedException(String msg, Throwable th) {
    super(msg, th);
  }
}
