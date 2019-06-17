/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.acmedcare.nas.ftp.server.impl;

import com.acmedcare.nas.ftp.server.ftplet.*;
import com.acmedcare.nas.ftp.server.listener.Listener;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;
import org.apache.mina.filter.ssl.SslFilter;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class FtpIoSession implements IoSession {

  /** Contains user name between USER and PASS commands */
  public static final String ATTRIBUTE_PREFIX = "com.acmedcare.nas.ftp.server.";

  private static final String ATTRIBUTE_USER_ARGUMENT = ATTRIBUTE_PREFIX + "user-argument";
  private static final String ATTRIBUTE_SESSION_ID = ATTRIBUTE_PREFIX + "session-id";
  private static final String ATTRIBUTE_USER = ATTRIBUTE_PREFIX + "user";
  private static final String ATTRIBUTE_LANGUAGE = ATTRIBUTE_PREFIX + "language";
  private static final String ATTRIBUTE_LOGIN_TIME = ATTRIBUTE_PREFIX + "login-time";
  private static final String ATTRIBUTE_DATA_CONNECTION = ATTRIBUTE_PREFIX + "data-connection";
  private static final String ATTRIBUTE_FILE_SYSTEM = ATTRIBUTE_PREFIX + "file-system";
  private static final String ATTRIBUTE_RENAME_FROM = ATTRIBUTE_PREFIX + "rename-from";
  private static final String ATTRIBUTE_FILE_OFFSET = ATTRIBUTE_PREFIX + "file-offset";
  private static final String ATTRIBUTE_DATA_TYPE = ATTRIBUTE_PREFIX + "data-type";
  private static final String ATTRIBUTE_STRUCTURE = ATTRIBUTE_PREFIX + "structure";
  private static final String ATTRIBUTE_FAILED_LOGINS = ATTRIBUTE_PREFIX + "failed-logins";
  private static final String ATTRIBUTE_LISTENER = ATTRIBUTE_PREFIX + "listener";
  private static final String ATTRIBUTE_MAX_IDLE_TIME = ATTRIBUTE_PREFIX + "max-idle-time";
  private static final String ATTRIBUTE_LAST_ACCESS_TIME = ATTRIBUTE_PREFIX + "last-access-time";
  private static final String ATTRIBUTE_CACHED_REMOTE_ADDRESS =
      ATTRIBUTE_PREFIX + "cached-remote-address";
  private final IoSession wrappedSession;
  private final FtpServerContext context;
  /** Last reply that was sent to the client, if any. */
  private FtpReply lastReply = null;

  /* Begin wrapped IoSession methods */

  public FtpIoSession(IoSession wrappedSession, FtpServerContext context) {
    this.wrappedSession = wrappedSession;
    this.context = context;
  }

  /** @see IoSession#close() */
  @Override
  public CloseFuture close() {
    return wrappedSession.close();
  }

  /** @see IoSession#close(boolean) */
  @Override
  public CloseFuture close(boolean immediately) {
    return wrappedSession.close(immediately);
  }

  /** @see IoSession#closeNow() */
  @Override
  public CloseFuture closeNow() {
    return wrappedSession.closeNow();
  }

  /** @see IoSession#closeOnFlush() */
  @Override
  public CloseFuture closeOnFlush() {
    return wrappedSession.closeOnFlush();
  }

  /** @see IoSession#containsAttribute(Object) */
  @Override
  public boolean containsAttribute(Object key) {
    return wrappedSession.containsAttribute(key);
  }

  /** @see IoSession#getAttachment() */
  @Override
  @SuppressWarnings("deprecation")
  public Object getAttachment() {
    return wrappedSession.getAttachment();
  }

  /** @see IoSession#getAttribute(Object) */
  @Override
  public Object getAttribute(Object key) {
    return wrappedSession.getAttribute(key);
  }

  /** @see IoSession#getAttribute(Object, Object) */
  @Override
  public Object getAttribute(Object key, Object defaultValue) {
    return wrappedSession.getAttribute(key, defaultValue);
  }

  /** @see IoSession#getAttributeKeys() */
  @Override
  public Set<Object> getAttributeKeys() {
    return wrappedSession.getAttributeKeys();
  }

  /** @see IoSession#getBothIdleCount() */
  @Override
  public int getBothIdleCount() {
    return wrappedSession.getBothIdleCount();
  }

  /** @see IoSession#getCloseFuture() */
  @Override
  public CloseFuture getCloseFuture() {
    return wrappedSession.getCloseFuture();
  }

  /** @see IoSession#getConfig() */
  @Override
  public IoSessionConfig getConfig() {
    return wrappedSession.getConfig();
  }

  /** @see IoSession#getCreationTime() */
  @Override
  public long getCreationTime() {
    return wrappedSession.getCreationTime();
  }

  /** @see IoSession#getFilterChain() */
  @Override
  public IoFilterChain getFilterChain() {
    return wrappedSession.getFilterChain();
  }

  /** @see IoSession#getHandler() */
  @Override
  public IoHandler getHandler() {
    return wrappedSession.getHandler();
  }

  /** @see IoSession#getId() */
  @Override
  public long getId() {
    return wrappedSession.getId();
  }

  /** @see IoSession#getIdleCount(IdleStatus) */
  @Override
  public int getIdleCount(IdleStatus status) {
    return wrappedSession.getIdleCount(status);
  }

  /** @see IoSession#getLastBothIdleTime() */
  @Override
  public long getLastBothIdleTime() {
    return wrappedSession.getLastBothIdleTime();
  }

  /** @see IoSession#getLastIdleTime(IdleStatus) */
  @Override
  public long getLastIdleTime(IdleStatus status) {
    return wrappedSession.getLastIdleTime(status);
  }

  /** @see IoSession#getLastIoTime() */
  @Override
  public long getLastIoTime() {
    return wrappedSession.getLastIoTime();
  }

  /** @see IoSession#getLastReadTime() */
  @Override
  public long getLastReadTime() {
    return wrappedSession.getLastReadTime();
  }

  /** @see IoSession#getLastReaderIdleTime() */
  @Override
  public long getLastReaderIdleTime() {
    return wrappedSession.getLastReaderIdleTime();
  }

  /** @see IoSession#getLastWriteTime() */
  @Override
  public long getLastWriteTime() {
    return wrappedSession.getLastWriteTime();
  }

  /** @see IoSession#getLastWriterIdleTime() */
  @Override
  public long getLastWriterIdleTime() {
    return wrappedSession.getLastWriterIdleTime();
  }

  /** @see IoSession#getLocalAddress() */
  @Override
  public SocketAddress getLocalAddress() {
    return wrappedSession.getLocalAddress();
  }

  /** @see IoSession#getReadBytes() */
  @Override
  public long getReadBytes() {
    return wrappedSession.getReadBytes();
  }

  /** @see IoSession#getReadBytesThroughput() */
  @Override
  public double getReadBytesThroughput() {
    return wrappedSession.getReadBytesThroughput();
  }

  /** @see IoSession#getReadMessages() */
  @Override
  public long getReadMessages() {
    return wrappedSession.getReadMessages();
  }

  /** @see IoSession#getReadMessagesThroughput() */
  @Override
  public double getReadMessagesThroughput() {
    return wrappedSession.getReadMessagesThroughput();
  }

  /** @see IoSession#getReaderIdleCount() */
  @Override
  public int getReaderIdleCount() {
    return wrappedSession.getReaderIdleCount();
  }

  /** @see IoSession#getRemoteAddress() */
  @Override
  public SocketAddress getRemoteAddress() {
    // when closing a socket, the remote address might be reset to null
    // therefore, we attempt to keep a cached copy around

    SocketAddress address = wrappedSession.getRemoteAddress();
    if (address == null && containsAttribute(ATTRIBUTE_CACHED_REMOTE_ADDRESS)) {
      return (SocketAddress) getAttribute(ATTRIBUTE_CACHED_REMOTE_ADDRESS);
    } else {
      setAttribute(ATTRIBUTE_CACHED_REMOTE_ADDRESS, address);
      return address;
    }
  }

  /** @see IoSession#getScheduledWriteBytes() */
  @Override
  public long getScheduledWriteBytes() {
    return wrappedSession.getScheduledWriteBytes();
  }

  /** @see IoSession#getScheduledWriteMessages() */
  @Override
  public int getScheduledWriteMessages() {
    return wrappedSession.getScheduledWriteMessages();
  }

  /** @see IoSession#getService() */
  @Override
  public IoService getService() {
    return wrappedSession.getService();
  }

  /** @see IoSession#getServiceAddress() */
  @Override
  public SocketAddress getServiceAddress() {
    return wrappedSession.getServiceAddress();
  }

  /** @see IoSession#getTransportMetadata() */
  @Override
  public TransportMetadata getTransportMetadata() {
    return wrappedSession.getTransportMetadata();
  }

  /** @see IoSession#getWriterIdleCount() */
  @Override
  public int getWriterIdleCount() {
    return wrappedSession.getWriterIdleCount();
  }

  /** @see IoSession#getWrittenBytes() */
  @Override
  public long getWrittenBytes() {
    return wrappedSession.getWrittenBytes();
  }

  /** @see IoSession#getWrittenBytesThroughput() */
  @Override
  public double getWrittenBytesThroughput() {
    return wrappedSession.getWrittenBytesThroughput();
  }

  /** @see IoSession#getWrittenMessages() */
  @Override
  public long getWrittenMessages() {
    return wrappedSession.getWrittenMessages();
  }

  /** @see IoSession#getWrittenMessagesThroughput() */
  @Override
  public double getWrittenMessagesThroughput() {
    return wrappedSession.getWrittenMessagesThroughput();
  }

  /** @see IoSession#isClosing() */
  @Override
  public boolean isClosing() {
    return wrappedSession.isClosing();
  }

  /** @see IoSession#isConnected() */
  @Override
  public boolean isConnected() {
    return wrappedSession.isConnected();
  }

  /** @see IoSession#isActive() */
  @Override
  public boolean isActive() {
    return wrappedSession.isActive();
  }

  /** @see IoSession#isIdle(IdleStatus) */
  @Override
  public boolean isIdle(IdleStatus status) {
    return wrappedSession.isIdle(status);
  }

  /** @see IoSession#read() */
  @Override
  public ReadFuture read() {
    return wrappedSession.read();
  }

  /** @see IoSession#removeAttribute(Object) */
  @Override
  public Object removeAttribute(Object key) {
    return wrappedSession.removeAttribute(key);
  }

  /** @see IoSession#removeAttribute(Object, Object) */
  @Override
  public boolean removeAttribute(Object key, Object value) {
    return wrappedSession.removeAttribute(key, value);
  }

  /** @see IoSession#replaceAttribute(Object, Object, Object) */
  @Override
  public boolean replaceAttribute(Object key, Object oldValue, Object newValue) {
    return wrappedSession.replaceAttribute(key, oldValue, newValue);
  }

  /** @see IoSession#resumeRead() */
  @Override
  public void resumeRead() {
    wrappedSession.resumeRead();
  }

  /** @see IoSession#resumeWrite() */
  @Override
  public void resumeWrite() {
    wrappedSession.resumeWrite();
  }

  /** @see IoSession#setAttachment(Object) */
  @Override
  @SuppressWarnings("deprecation")
  public Object setAttachment(Object attachment) {
    return wrappedSession.setAttachment(attachment);
  }

  /** @see IoSession#setAttribute(Object) */
  @Override
  public Object setAttribute(Object key) {
    return wrappedSession.setAttribute(key);
  }

  /** @see IoSession#setAttribute(Object, Object) */
  @Override
  public Object setAttribute(Object key, Object value) {
    return wrappedSession.setAttribute(key, value);
  }

  /** @see IoSession#setAttributeIfAbsent(Object) */
  @Override
  public Object setAttributeIfAbsent(Object key) {
    return wrappedSession.setAttributeIfAbsent(key);
  }

  /** @see IoSession#setAttributeIfAbsent(Object, Object) */
  @Override
  public Object setAttributeIfAbsent(Object key, Object value) {
    return wrappedSession.setAttributeIfAbsent(key, value);
  }

  /** @see IoSession#suspendRead() */
  @Override
  public void suspendRead() {
    wrappedSession.suspendRead();
  }

  /** @see IoSession#suspendWrite() */
  @Override
  public void suspendWrite() {
    wrappedSession.suspendWrite();
  }

  /** @see IoSession#write(Object) */
  @Override
  public WriteFuture write(Object message) {
    WriteFuture future = wrappedSession.write(message);
    this.lastReply = (FtpReply) message;
    return future;
  }

  /** @see IoSession#write(Object, SocketAddress) */
  @Override
  public WriteFuture write(Object message, SocketAddress destination) {
    WriteFuture future = wrappedSession.write(message, destination);
    this.lastReply = (FtpReply) message;
    return future;
  }

  /* End wrapped IoSession methods */
  public void resetState() {
    removeAttribute(ATTRIBUTE_RENAME_FROM);
    removeAttribute(ATTRIBUTE_FILE_OFFSET);
  }

  public synchronized ServerDataConnectionFactory getDataConnection() {
    if (containsAttribute(ATTRIBUTE_DATA_CONNECTION)) {
      return (ServerDataConnectionFactory) getAttribute(ATTRIBUTE_DATA_CONNECTION);
    } else {
      IODataConnectionFactory dataCon = new IODataConnectionFactory(context, this);
      dataCon.setServerControlAddress(((InetSocketAddress) getLocalAddress()).getAddress());
      setAttribute(ATTRIBUTE_DATA_CONNECTION, dataCon);

      return dataCon;
    }
  }

  public FileSystemView getFileSystemView() {
    return (FileSystemView) getAttribute(ATTRIBUTE_FILE_SYSTEM);
  }

  public User getUser() {
    return (User) getAttribute(ATTRIBUTE_USER);
  }

  public void setUser(User user) {
    setAttribute(ATTRIBUTE_USER, user);
  }

  /** Is logged-in */
  public boolean isLoggedIn() {
    return containsAttribute(ATTRIBUTE_USER);
  }

  public Listener getListener() {
    return (Listener) getAttribute(ATTRIBUTE_LISTENER);
  }

  public void setListener(Listener listener) {
    setAttribute(ATTRIBUTE_LISTENER, listener);
  }

  public FtpSession getFtpletSession() {
    return new DefaultFtpSession(this);
  }

  public String getLanguage() {
    return (String) getAttribute(ATTRIBUTE_LANGUAGE);
  }

  public void setLanguage(String language) {
    setAttribute(ATTRIBUTE_LANGUAGE, language);
  }

  public String getUserArgument() {
    return (String) getAttribute(ATTRIBUTE_USER_ARGUMENT);
  }

  public void setUserArgument(String userArgument) {
    setAttribute(ATTRIBUTE_USER_ARGUMENT, userArgument);
  }

  public int getMaxIdleTime() {
    return (Integer) getAttribute(ATTRIBUTE_MAX_IDLE_TIME, 0);
  }

  public void setMaxIdleTime(int maxIdleTime) {
    setAttribute(ATTRIBUTE_MAX_IDLE_TIME, maxIdleTime);

    int listenerTimeout = getListener().getIdleTimeout();

    // the listener timeout should be the upper limit, unless set to unlimited
    // if the user limit is set to be unlimited, use the listener value is the threshold
    //     (already used as the default for all sessions)
    // else, if the user limit is less than the listener idle time, use the user limit
    if (listenerTimeout <= 0 || (maxIdleTime > 0 && maxIdleTime < listenerTimeout)) {
      wrappedSession.getConfig().setBothIdleTime(maxIdleTime);
    }
  }

  public synchronized void increaseFailedLogins() {
    int failedLogins = (Integer) getAttribute(ATTRIBUTE_FAILED_LOGINS, 0);
    failedLogins++;
    setAttribute(ATTRIBUTE_FAILED_LOGINS, failedLogins);
  }

  public int getFailedLogins() {
    return (Integer) getAttribute(ATTRIBUTE_FAILED_LOGINS, 0);
  }

  public void setLogin(FileSystemView fsview) {
    setAttribute(ATTRIBUTE_LOGIN_TIME, new Date());
    setAttribute(ATTRIBUTE_FILE_SYSTEM, fsview);
  }

  public void reinitialize() {
    logoutUser();
    removeAttribute(ATTRIBUTE_USER);
    removeAttribute(ATTRIBUTE_USER_ARGUMENT);
    removeAttribute(ATTRIBUTE_LOGIN_TIME);
    removeAttribute(ATTRIBUTE_FILE_SYSTEM);
    removeAttribute(ATTRIBUTE_RENAME_FROM);
    removeAttribute(ATTRIBUTE_FILE_OFFSET);
  }

  public void logoutUser() {
    ServerFtpStatistics stats = ((ServerFtpStatistics) context.getFtpStatistics());
    if (stats != null) {
      stats.setLogout(this);
      LoggerFactory.getLogger(this.getClass())
          .debug("Statistics login decreased due to user logout");
    } else {
      LoggerFactory.getLogger(this.getClass())
          .warn("Statistics not available in session, can not decrease login  count");
    }
  }

  public FtpFile getRenameFrom() {
    return (FtpFile) getAttribute(ATTRIBUTE_RENAME_FROM);
  }

  public void setRenameFrom(FtpFile renFr) {
    setAttribute(ATTRIBUTE_RENAME_FROM, renFr);
  }

  public long getFileOffset() {
    return (Long) getAttribute(ATTRIBUTE_FILE_OFFSET, 0L);
  }

  public void setFileOffset(long fileOffset) {
    setAttribute(ATTRIBUTE_FILE_OFFSET, fileOffset);
  }

  /** @see FtpSession#getSessionId() */
  public UUID getSessionId() {
    synchronized (wrappedSession) {
      if (!wrappedSession.containsAttribute(ATTRIBUTE_SESSION_ID)) {
        wrappedSession.setAttribute(ATTRIBUTE_SESSION_ID, UUID.randomUUID());
      }
      return (UUID) wrappedSession.getAttribute(ATTRIBUTE_SESSION_ID);
    }
  }

  public Structure getStructure() {
    return (Structure) getAttribute(ATTRIBUTE_STRUCTURE, Structure.FILE);
  }

  public void setStructure(Structure structure) {
    setAttribute(ATTRIBUTE_STRUCTURE, structure);
  }

  public DataType getDataType() {
    return (DataType) getAttribute(ATTRIBUTE_DATA_TYPE, DataType.ASCII);
  }

  public void setDataType(DataType dataType) {
    setAttribute(ATTRIBUTE_DATA_TYPE, dataType);
  }

  public Date getLoginTime() {
    return (Date) getAttribute(ATTRIBUTE_LOGIN_TIME);
  }

  public Date getLastAccessTime() {
    return (Date) getAttribute(ATTRIBUTE_LAST_ACCESS_TIME);
  }

  public Certificate[] getClientCertificates() {
    if (getFilterChain().contains(SslFilter.class)) {
      SslFilter sslFilter = (SslFilter) getFilterChain().get(SslFilter.class);

      SSLSession sslSession = sslFilter.getSslSession(this);

      if (sslSession != null) {
        try {
          return sslSession.getPeerCertificates();
        } catch (SSLPeerUnverifiedException e) {
          // ignore, certificate will not be available to the session
        }
      }
    }

    // no certificates available
    return null;
  }

  public void updateLastAccessTime() {
    setAttribute(ATTRIBUTE_LAST_ACCESS_TIME, new Date());
  }

  /** @see IoSession#getCurrentWriteMessage() */
  @Override
  public Object getCurrentWriteMessage() {
    return wrappedSession.getCurrentWriteMessage();
  }

  /** @see IoSession#getCurrentWriteRequest() */
  @Override
  public WriteRequest getCurrentWriteRequest() {
    return wrappedSession.getCurrentWriteRequest();
  }

  /** @see IoSession#setCurrentWriteRequest(WriteRequest) */
  @Override
  public void setCurrentWriteRequest(WriteRequest currentWriteRequest) {
    wrappedSession.setCurrentWriteRequest(currentWriteRequest);
  }

  /** @see IoSession#isBothIdle() */
  @Override
  public boolean isBothIdle() {
    return wrappedSession.isBothIdle();
  }

  /** @see IoSession#isReaderIdle() */
  @Override
  public boolean isReaderIdle() {
    return wrappedSession.isReaderIdle();
  }

  /** @see IoSession#isWriterIdle() */
  @Override
  public boolean isWriterIdle() {
    return wrappedSession.isWriterIdle();
  }

  /**
   * Indicates whether the control socket for this session is secure, that is, running over SSL/TLS
   *
   * @return true if the control socket is secured
   */
  public boolean isSecure() {
    return getFilterChain().contains(SslFilter.class);
  }

  /**
   * Increase the number of bytes written on the data connection
   *
   * @param increment The number of bytes written
   */
  public void increaseWrittenDataBytes(int increment) {
    if (wrappedSession instanceof AbstractIoSession) {
      ((AbstractIoSession) wrappedSession).increaseScheduledWriteBytes(increment);
      ((AbstractIoSession) wrappedSession)
          .increaseWrittenBytes(increment, System.currentTimeMillis());
    }
  }

  /**
   * Increase the number of bytes read on the data connection
   *
   * @param increment The number of bytes written
   */
  public void increaseReadDataBytes(int increment) {
    if (wrappedSession instanceof AbstractIoSession) {
      ((AbstractIoSession) wrappedSession).increaseReadBytes(increment, System.currentTimeMillis());
    }
  }

  /**
   * Returns the last reply that was sent to the client.
   *
   * @return the last reply that was sent to the client.
   */
  public FtpReply getLastReply() {
    return lastReply;
  }

  /** @see IoSession#getWriteRequestQueue() */
  @Override
  public WriteRequestQueue getWriteRequestQueue() {
    return wrappedSession.getWriteRequestQueue();
  }

  /** @see IoSession#isReadSuspended() */
  @Override
  public boolean isReadSuspended() {
    return wrappedSession.isReadSuspended();
  }

  /** @see IoSession#isWriteSuspended() */
  @Override
  public boolean isWriteSuspended() {
    return wrappedSession.isWriteSuspended();
  }

  /** @see IoSession#updateThroughput(long, boolean) */
  @Override
  public void updateThroughput(long currentTime, boolean force) {
    wrappedSession.updateThroughput(currentTime, force);
  }

  @Override
  public boolean isSecured() {
    return getFilterChain().contains(SslFilter.class);
  }
}
