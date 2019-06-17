package com.acmedcare.nas.ftp.server.impl;

import com.acmedcare.nas.ftp.server.ftplet.FtpFile;
import com.acmedcare.nas.ftp.server.ftplet.FtpStatistics;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <p>This is same as <code>FtpStatistics</code> with added observer and setting values
 * functionalities.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface ServerFtpStatistics extends FtpStatistics {

  /** Set statistics observer. */
  void setObserver(StatisticsObserver observer);

  /** Set file observer. */
  void setFileObserver(FileObserver observer);

  /** Increment upload count. */
  void setUpload(FtpIoSession session, FtpFile file, long size);

  /** Increment download count. */
  void setDownload(FtpIoSession session, FtpFile file, long size);

  /** Increment make directory count. */
  void setMkdir(FtpIoSession session, FtpFile dir);

  /** Decrement remove directory count. */
  void setRmdir(FtpIoSession session, FtpFile dir);

  /** Increment delete count. */
  void setDelete(FtpIoSession session, FtpFile file);

  /** Increment current connection count. */
  void setOpenConnection(FtpIoSession session);

  /** Decrement close connection count. */
  void setCloseConnection(FtpIoSession session);

  /** Increment current login count. */
  void setLogin(FtpIoSession session);

  /** Increment failed login count. */
  void setLoginFail(FtpIoSession session);

  /** Decrement current login count. */
  void setLogout(FtpIoSession session);

  /**
   * Reset all cumulative total counters. Do not reset current counters, like current logins,
   * otherwise these will become negative when someone disconnects.
   */
  void resetStatisticsCounters();
}
