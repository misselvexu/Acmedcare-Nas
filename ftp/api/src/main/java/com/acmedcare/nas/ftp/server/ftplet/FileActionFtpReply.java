package com.acmedcare.nas.ftp.server.ftplet;

/**
 * A more specific type of FtpReply that is sent for commands that act on a single file or directory
 * such as MKD, DELE, RMD etc.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface FileActionFtpReply extends FtpReply {

  /**
   * Returns the file (or directory) on which the action was taken (e.g. uploaded, created, listed)
   *
   * @return the file on which the action was taken. May return <code>null</code>, if the file
   * information is not available.
   */
  FtpFile getFile();
}
