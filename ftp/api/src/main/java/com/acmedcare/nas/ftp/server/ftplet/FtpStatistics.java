package com.acmedcare.nas.ftp.server.ftplet;

import java.net.InetAddress;
import java.util.Date;

/**
 * This interface holds all the ftp server statistical information.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface FtpStatistics {

  /**
   * Get the server start time.
   *
   * @return The {@link Date} when the server started
   */
  Date getStartTime();

  /**
   * Get number of files uploaded.
   *
   * @return The total number of uploads
   */
  int getTotalUploadNumber();

  /**
   * Get number of files downloaded.
   *
   * @return The total number of downloads
   */
  int getTotalDownloadNumber();

  /**
   * Get number of files deleted.
   *
   * @return The total number of deletions
   */
  int getTotalDeleteNumber();

  /**
   * Get total number of bytes uploaded.
   *
   * @return The total number of bytes uploaded
   */
  long getTotalUploadSize();

  /**
   * Get total number of bytes downloaded.
   *
   * @return The total number of bytes downloaded
   */
  long getTotalDownloadSize();

  /**
   * Get total directory created.
   *
   * @return The total number of created directories
   */
  int getTotalDirectoryCreated();

  /**
   * Get total directory removed.
   *
   * @return The total number of removed directories
   */
  int getTotalDirectoryRemoved();

  /**
   * Get total number of connections
   *
   * @return The total number of connections
   */
  int getTotalConnectionNumber();

  /**
   * Get current number of connections.
   *
   * @return The current number of connections
   */
  int getCurrentConnectionNumber();

  /**
   * Get total login number.
   *
   * @return The total number of logins
   */
  int getTotalLoginNumber();

  /**
   * Get total failed login number.
   *
   * @return The total number of failed logins
   */
  int getTotalFailedLoginNumber();

  /**
   * Get current login number
   *
   * @return The current number of logins
   */
  int getCurrentLoginNumber();

  /**
   * Get total anonymous login number.
   *
   * @return The total number of anonymous logins
   */
  int getTotalAnonymousLoginNumber();

  /**
   * Get current anonymous login number.
   *
   * @return The current number of anonymous logins
   */
  int getCurrentAnonymousLoginNumber();

  /**
   * Get the login number for the specific user
   *
   * @param user The {@link User} for which to retrieve the number of logins
   * @return The total number of logins for the provided user
   */
  int getCurrentUserLoginNumber(User user);

  /**
   * Get the login number for the specific user from the ipAddress
   *
   * @param user      login user account
   * @param ipAddress the ip address of the remote user
   * @return The total number of logins for the provided user and IP address
   */
  int getCurrentUserLoginNumber(User user, InetAddress ipAddress);
}
