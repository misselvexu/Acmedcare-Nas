package com.acmedcare.nas.ftp.server.ftplet;

import java.io.IOException;

/**
 * Default ftplet implementation. All the callback method returns null. It is
 * just an empty implementation. You can derive your ftplet implementation from
 * this class.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DefaultFtplet implements Ftplet {

  @Override
  public void init(FtpletContext ftpletContext) throws FtpException {
  }

  @Override
  public void destroy() {
  }

  @Override
  public FtpletResult onConnect(FtpSession session) throws FtpException,
      IOException {
    return null;
  }

  @Override
  public FtpletResult onDisconnect(FtpSession session) throws FtpException,
      IOException {
    return null;
  }

  @Override
  public FtpletResult beforeCommand(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    String command = request.getCommand().toUpperCase();

    if ("DELE".equals(command)) {
      return onDeleteStart(session, request);
    } else if ("STOR".equals(command)) {
      return onUploadStart(session, request);
    } else if ("RETR".equals(command)) {
      return onDownloadStart(session, request);
    } else if ("RMD".equals(command)) {
      return onRmdirStart(session, request);
    } else if ("MKD".equals(command)) {
      return onMkdirStart(session, request);
    } else if ("APPE".equals(command)) {
      return onAppendStart(session, request);
    } else if ("STOU".equals(command)) {
      return onUploadUniqueStart(session, request);
    } else if ("RNTO".equals(command)) {
      return onRenameStart(session, request);
    } else if ("SITE".equals(command)) {
      return onSite(session, request);
    } else {
      // TODO should we call a catch all?
      return null;
    }
  }

  @Override
  public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply)
      throws FtpException, IOException {

    // the reply is ignored for these callbacks

    String command = request.getCommand().toUpperCase();

    if ("PASS".equals(command)) {
      return onLogin(session, request);
    } else if ("DELE".equals(command)) {
      return onDeleteEnd(session, request);
    } else if ("STOR".equals(command)) {
      return onUploadEnd(session, request);
    } else if ("RETR".equals(command)) {
      return onDownloadEnd(session, request);
    } else if ("RMD".equals(command)) {
      return onRmdirEnd(session, request);
    } else if ("MKD".equals(command)) {
      return onMkdirEnd(session, request);
    } else if ("APPE".equals(command)) {
      return onAppendEnd(session, request);
    } else if ("STOU".equals(command)) {
      return onUploadUniqueEnd(session, request);
    } else if ("RNTO".equals(command)) {
      return onRenameEnd(session, request);
    } else {
      // TODO should we call a catch all?
      return null;
    }
  }

  /**
   * Override this method to intercept user logins
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onLogin(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept deletions
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onDeleteStart(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to handle deletions after completion
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onDeleteEnd(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept uploads
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onUploadStart(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to handle uploads after completion
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onUploadEnd(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept downloads
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onDownloadStart(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to handle downloads after completion
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept deletion of directories
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onRmdirStart(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to handle deletion of directories after completion
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onRmdirEnd(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept creation of directories
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onMkdirStart(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to handle creation of directories after completion
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onMkdirEnd(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept file appends
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onAppendStart(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept file appends after completion
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onAppendEnd(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept unique uploads
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onUploadUniqueStart(FtpSession session,
                                          FtpRequest request) throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to handle unique uploads after completion
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onUploadUniqueEnd(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept renames
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onRenameStart(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to handle renames after completion
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onRenameEnd(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }

  /**
   * Override this method to intercept SITE commands
   *
   * @param session The current {@link FtpSession}
   * @param request The current {@link FtpRequest}
   * @return The action for the container to take
   * @throws FtpException
   * @throws IOException
   */
  public FtpletResult onSite(FtpSession session, FtpRequest request)
      throws FtpException, IOException {
    return null;
  }
}