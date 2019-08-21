package com.acmedcare.nas.ftp.server.ftplet;

/**
 * This class encapsulates the return values of the ftplet methods.
 *
 * <p>DEFAULT < NO_FTPLET < SKIP < DISCONNECT
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public enum FtpletResult {

  /**
   * This return value indicates that the next ftplet method will be called. If no other ftplet is
   * available, the ftpserver will process the request.
   */
  DEFAULT,

  /**
   * This return value indicates that the other ftplet methods will not be called but the ftpserver
   * will continue processing this request.
   */
  NO_FTPLET,

  /**
   * It indicates that the ftpserver will skip everything. No further processing (both ftplet and
   * server) will be done for this request.
   */
  SKIP,

  /**
   * It indicates that the server will skip and disconnect the client. No other request from the
   * same client will be served.
   */
  DISCONNECT;
}
