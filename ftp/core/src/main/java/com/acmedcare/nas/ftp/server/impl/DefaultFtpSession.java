package com.acmedcare.nas.ftp.server.impl;

import com.acmedcare.nas.ftp.server.ftplet.*;

import java.net.InetSocketAddress;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.UUID;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <p>FTP session
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DefaultFtpSession implements FtpSession {

  private final FtpIoSession ioSession;

  /**
   * Default constructor.
   */
  public DefaultFtpSession(final FtpIoSession ioSession) {
    this.ioSession = ioSession;
  }

  /**
   * Is logged-in
   */
  @Override
  public boolean isLoggedIn() {
    return ioSession.isLoggedIn();
  }

  /**
   * Get FTP data connection.
   */
  @Override
  public DataConnectionFactory getDataConnection() {
    return ioSession.getDataConnection();
  }

  /**
   * Get file system view.
   */
  @Override
  public FileSystemView getFileSystemView() {
    return ioSession.getFileSystemView();
  }

  /**
   * Get connection time.
   */
  @Override
  public Date getConnectionTime() {
    return new Date(ioSession.getCreationTime());
  }

  /**
   * Get the login time.
   */
  @Override
  public Date getLoginTime() {
    return ioSession.getLoginTime();
  }

  /**
   * Get last access time.
   */
  @Override
  public Date getLastAccessTime() {
    return ioSession.getLastAccessTime();
  }

  /**
   * Get file offset.
   */
  @Override
  public long getFileOffset() {
    return ioSession.getFileOffset();
  }

  /**
   * Get rename from file object.
   */
  @Override
  public FtpFile getRenameFrom() {
    return ioSession.getRenameFrom();
  }

  /**
   * Returns user name entered in USER command
   *
   * @return user name entered in USER command
   */
  @Override
  public String getUserArgument() {
    return ioSession.getUserArgument();
  }

  /**
   * Get language.
   */
  @Override
  public String getLanguage() {
    return ioSession.getLanguage();
  }

  /**
   * Get user.
   */
  @Override
  public User getUser() {
    return ioSession.getUser();
  }

  /**
   * Get remote address
   */
  @Override
  public InetSocketAddress getClientAddress() {
    if (ioSession.getRemoteAddress() instanceof InetSocketAddress) {
      return ((InetSocketAddress) ioSession.getRemoteAddress());
    } else {
      return null;
    }
  }

  /**
   * Get attribute
   */
  @Override
  public Object getAttribute(final String name) {
    if (name.startsWith(FtpIoSession.ATTRIBUTE_PREFIX)) {
      throw new IllegalArgumentException("Illegal lookup of internal attribute");
    }

    return ioSession.getAttribute(name);
  }

  /**
   * Set attribute.
   */
  @Override
  public void setAttribute(final String name, final Object value) {
    if (name.startsWith(FtpIoSession.ATTRIBUTE_PREFIX)) {
      throw new IllegalArgumentException("Illegal setting of internal attribute");
    }

    ioSession.setAttribute(name, value);
  }

  @Override
  public int getMaxIdleTime() {
    return ioSession.getMaxIdleTime();
  }

  @Override
  public void setMaxIdleTime(final int maxIdleTime) {
    ioSession.setMaxIdleTime(maxIdleTime);
  }

  /**
   * Get the data type.
   */
  @Override
  public DataType getDataType() {
    return ioSession.getDataType();
  }

  /**
   * Get structure.
   */
  @Override
  public Structure getStructure() {
    return ioSession.getStructure();
  }

  @Override
  public Certificate[] getClientCertificates() {
    return ioSession.getClientCertificates();
  }

  @Override
  public InetSocketAddress getServerAddress() {
    if (ioSession.getLocalAddress() instanceof InetSocketAddress) {
      return ((InetSocketAddress) ioSession.getLocalAddress());
    } else {
      return null;
    }
  }

  @Override
  public int getFailedLogins() {
    return ioSession.getFailedLogins();
  }

  @Override
  public void removeAttribute(final String name) {
    if (name.startsWith(FtpIoSession.ATTRIBUTE_PREFIX)) {
      throw new IllegalArgumentException("Illegal removal of internal attribute");
    }

    ioSession.removeAttribute(name);
  }

  @Override
  public void write(FtpReply reply) throws FtpException {
    ioSession.write(reply);
  }

  @Override
  public boolean isSecure() {
    // TODO Auto-generated method stub
    return ioSession.isSecure();
  }

  /**
   * Increase the number of bytes written on the data connection
   *
   * @param increment The number of bytes written
   */
  public void increaseWrittenDataBytes(int increment) {
    ioSession.increaseWrittenDataBytes(increment);
  }

  /**
   * Increase the number of bytes read on the data connection
   *
   * @param increment The number of bytes written
   */
  public void increaseReadDataBytes(int increment) {
    ioSession.increaseReadDataBytes(increment);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getSessionId() {
    return ioSession.getSessionId();
  }
}
