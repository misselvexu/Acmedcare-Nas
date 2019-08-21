package com.acmedcare.nas.ftp.server.ftplet;

/**
 * Factory for file system implementations - it returns the file system view for user.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface FileSystemFactory {

  /**
   * Create user specific file system view.
   *
   * @param user The user for which the file system should be created
   * @return The current {@link FileSystemView} for the provided user
   * @throws FtpException
   */
  FileSystemView createFileSystemView(User user) throws FtpException;
}
