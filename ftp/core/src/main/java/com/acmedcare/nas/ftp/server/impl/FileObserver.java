package com.acmedcare.nas.ftp.server.impl;

import com.acmedcare.nas.ftp.server.ftplet.FtpFile;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <p>This is the file related activity observer.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface FileObserver {

  /** User file upload notification. */
  void notifyUpload(FtpIoSession session, FtpFile file, long size);

  /** User file download notification. */
  void notifyDownload(FtpIoSession session, FtpFile file, long size);

  /** User file delete notification. */
  void notifyDelete(FtpIoSession session, FtpFile file);

  /** User make directory notification. */
  void notifyMkdir(FtpIoSession session, FtpFile file);

  /** User remove directory notification. */
  void notifyRmdir(FtpIoSession session, FtpFile file);
}
