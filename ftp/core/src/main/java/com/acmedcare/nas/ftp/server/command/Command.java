package com.acmedcare.nas.ftp.server.command;

import com.acmedcare.nas.ftp.server.ftplet.FtpException;
import com.acmedcare.nas.ftp.server.ftplet.FtpRequest;
import com.acmedcare.nas.ftp.server.impl.FtpIoSession;
import com.acmedcare.nas.ftp.server.impl.FtpServerContext;

import java.io.IOException;

/**
 * This interface encapsulates all the FTP commands.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface Command {

  /**
   * Execute command.
   *
   * @param session The current {@link FtpIoSession}
   * @param context The current {@link FtpServerContext}
   * @param request The current {@link FtpRequest}
   * @throws IOException e
   * @throws FtpException e
   */
  void execute(FtpIoSession session, FtpServerContext context, FtpRequest request)
      throws IOException, FtpException;
}
