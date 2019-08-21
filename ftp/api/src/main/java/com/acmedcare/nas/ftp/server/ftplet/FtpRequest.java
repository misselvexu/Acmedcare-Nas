package com.acmedcare.nas.ftp.server.ftplet;

/**
 * One FtpRequest made by the client.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface FtpRequest {

  /**
   * Get the client request string.
   *
   * @return The full request line, e.g. "MKDIR newdir"
   */
  String getRequestLine();

  /**
   * Returns the ftp request command.
   *
   * @return The command part of the request line, e.g. "MKDIR"
   */
  String getCommand();

  /**
   * Get the ftp request argument.
   *
   * @return The argument part of the request line, e.g. "newdir"
   */
  String getArgument();

  /**
   * Check if request contains an argument
   *
   * @return true if an argument is available
   */
  boolean hasArgument();

  /**
   * Returns the timestamp (milliseconds since the epoch time) when this
   * request was received.
   *
   * @return the timestamp (milliseconds since the epoch time) when this
   * request was received.
   */
  long getReceivedTime();
}
