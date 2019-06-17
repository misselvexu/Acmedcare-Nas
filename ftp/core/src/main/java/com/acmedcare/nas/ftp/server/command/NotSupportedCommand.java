package com.acmedcare.nas.ftp.server.command;

import com.acmedcare.nas.ftp.server.ftplet.FtpReply;
import com.acmedcare.nas.ftp.server.ftplet.FtpRequest;
import com.acmedcare.nas.ftp.server.impl.FtpIoSession;
import com.acmedcare.nas.ftp.server.impl.FtpServerContext;
import com.acmedcare.nas.ftp.server.impl.LocalizedFtpReply;

import java.io.IOException;

/**
 * A command used primarily for overriding already installed commands when one wants to disable the
 * command.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class NotSupportedCommand extends AbstractCommand {

  /** Execute command */
  @Override
  public void execute(
      final FtpIoSession session, final FtpServerContext context, final FtpRequest request)
      throws IOException {

    // reset state variables
    session.resetState();

    // We do not support this command
    session.write(
        LocalizedFtpReply.translate(
            session,
            request,
            context,
            FtpReply.REPLY_502_COMMAND_NOT_IMPLEMENTED,
            "Not supported",
            null));
  }
}
