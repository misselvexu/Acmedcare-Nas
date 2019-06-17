package com.acmedcare.nas.ftp.server.ftplet;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface DataConnectionFactory {

  /**
   * Open an active data connection
   *
   * @return The open data connection
   * @throws Exception
   */
  DataConnection openConnection() throws Exception;

  /**
   * Indicates whether the data socket created by this factory will be secure
   * that is, running over SSL/TLS.
   *
   * @return true if the data socket will be secured
   */

  boolean isSecure();

  /**
   * Close data socket. If no open data connection exists,
   * this will silently ignore the call.
   */
  void closeDataConnection();

}