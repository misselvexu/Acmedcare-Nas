package com.acmedcare.nas.ftp.server.ftplet;

/**
 * This is an abstraction over the user file system view.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface FileSystemView {

  /**
   * Get the user home directory.
   *
   * @return The {@link FtpFile} for the users home directory
   * @throws FtpException
   */
  FtpFile getHomeDirectory() throws FtpException;

  /**
   * Get user current directory.
   *
   * @return The {@link FtpFile} for the users current directory
   * @throws FtpException
   */
  FtpFile getWorkingDirectory() throws FtpException;

  /**
   * Change directory.
   *
   * @param dir The path of the directory to set as the current directory for the user
   * @return true if successful
   * @throws FtpException
   */
  boolean changeWorkingDirectory(String dir) throws FtpException;

  /**
   * Get file object.
   *
   * @param file The path to the file to get
   * @return The {@link FtpFile} for the provided path
   * @throws FtpException
   */
  FtpFile getFile(String file) throws FtpException;

  /**
   * Does the file system support random file access?
   *
   * @return true if the file supports random access
   * @throws FtpException
   */
  boolean isRandomAccessible() throws FtpException;

  /**
   * Dispose file system view.
   */
  void dispose();
}
