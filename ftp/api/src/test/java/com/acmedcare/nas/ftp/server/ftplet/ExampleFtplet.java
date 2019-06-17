package com.acmedcare.nas.ftp.server.ftplet;

import java.io.IOException;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ExampleFtplet extends DefaultFtplet {

  @Override
  public FtpletResult onMkdirEnd(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    session.write(new DefaultFtpReply(550, "Error!"));
    return FtpletResult.SKIP;
  }

  @Override
  public FtpletResult onMkdirStart(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    if (session.isSecure() && session.getDataConnection().isSecure()) {
      // all is good
    }
    return null;
  }

}
