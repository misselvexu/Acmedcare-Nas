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

import com.acmedcare.nas.ftp.server.ConnectionConfig;
import com.acmedcare.nas.ftp.server.command.CommandFactory;
import com.acmedcare.nas.ftp.server.ftplet.FtpletContext;
import com.acmedcare.nas.ftp.server.ftpletcontainer.FtpletContainer;
import com.acmedcare.nas.ftp.server.listener.Listener;
import com.acmedcare.nas.ftp.server.message.MessageResource;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <p>This is basically <code>FtpletContext</code> with added connection manager, message resource
 * functionalities.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface FtpServerContext extends FtpletContext {

  ConnectionConfig getConnectionConfig();

  /** Get message resource. */
  MessageResource getMessageResource();

  /** Get ftplet container. */
  FtpletContainer getFtpletContainer();

  Listener getListener(String name);

  Map<String, Listener> getListeners();

  /** Get the command factory. */
  CommandFactory getCommandFactory();

  /** Release all components. */
  void dispose();

  /**
   * Returns the thread pool executor for this context.
   *
   * @return the thread pool executor for this context.
   */
  ThreadPoolExecutor getThreadPoolExecutor();
}
