package com.acmedcare.nas.ftp.server.ftplet;

/**
 * A ftplet configuration object used by a ftplet container used to pass information to a ftplet
 * during initialization. The configuration information contains initialization parameters.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface FtpletContext {

  /**
   * Get the user manager.
   *
   * @return The {@link UserManager}
   */
  UserManager getUserManager();

  /**
   * Get file system manager
   *
   * @return The {@link FileSystemFactory}
   */
  FileSystemFactory getFileSystemManager();

  /**
   * Get ftp statistics.
   *
   * @return The {@link FtpStatistics}
   */
  FtpStatistics getFtpStatistics();

  /**
   * Get Ftplet.
   *
   * @param name The name identifying the {@link Ftplet}
   * @return The {@link Ftplet} registred with the provided name, or null if none exists
   */
  Ftplet getFtplet(String name);
}
