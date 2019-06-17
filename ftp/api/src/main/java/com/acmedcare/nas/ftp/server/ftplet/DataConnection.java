package com.acmedcare.nas.ftp.server.ftplet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface DataConnection {

  /**
   * Transfer data from the client (e.g. STOR).
   *
   * @param session The current {@link FtpSession}
   * @param out     The {@link OutputStream} containing the destination of the data from the client.
   * @return The length of the transferred data
   * @throws IOException
   */
  long transferFromClient(FtpSession session, OutputStream out) throws IOException;

  /**
   * Transfer data to the client (e.g. RETR).
   *
   * @param session The current {@link FtpSession}
   * @param in      Data to be transfered to the client
   * @return The length of the transferred data
   * @throws IOException
   */
  long transferToClient(FtpSession session, InputStream in) throws IOException;

  /**
   * Transfer a string to the client, e.g. during LIST
   *
   * @param session The current {@link FtpSession}
   * @param str     The string to transfer
   * @throws IOException
   */
  void transferToClient(FtpSession session, String str) throws IOException;
}
