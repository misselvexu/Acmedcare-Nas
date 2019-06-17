package com.acmedcare.nas.ftp.server.ftplet;

/**
 * A more specific type of FTP reply that is sent by the commands that transfer
 * data over the data connection. These commands include LIST, RETR, STOR, STOU
 * etc.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */

public interface DataTransferFtpReply extends FileActionFtpReply {

  /**
   * Returns the number of bytes transferred.
   *
   * @return the number of bytes transferred.
   */
  long getBytesTransferred();

}
