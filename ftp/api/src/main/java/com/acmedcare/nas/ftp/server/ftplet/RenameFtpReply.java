package com.acmedcare.nas.ftp.server.ftplet;

/**
 * A more specific type of reply that is sent when a file is attempted to
 * rename. This reply is sent by the RNTO command.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */

public interface RenameFtpReply extends FtpReply {

  /**
   * Returns the file before the rename.
   *
   * @return the file before the rename. May return <code>null</code>, if
   * the file information is not available.
   */
  FtpFile getFrom();

  /**
   * Returns the file after the rename.
   *
   * @return the file after the rename. May return <code>null</code>, if
   * the file information is not available.
   */
  FtpFile getTo();

}
