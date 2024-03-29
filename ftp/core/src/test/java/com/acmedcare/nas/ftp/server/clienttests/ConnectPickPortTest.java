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

package com.acmedcare.nas.ftp.server.clienttests;

import com.acmedcare.nas.ftp.server.FtpServerFactory;
import com.acmedcare.nas.ftp.server.listener.ListenerFactory;
import com.acmedcare.nas.ftp.server.listener.nio.NioListener;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class ConnectPickPortTest extends ClientTestTemplate {

  @Override
  protected boolean isConnectClient() {
    return false;
  }

  @Override
  protected boolean isStartServer() {
    return false;
  }

  @Override
  protected FtpServerFactory createServer() throws Exception {
    FtpServerFactory server = super.createServer();

    ListenerFactory factory = new ListenerFactory();
    factory.setPort(0);

    server.addListener("default", factory.createListener());

    return server;
  }

  public void testPortWithZeroPort() throws Exception {
    assertEquals(0, ((NioListener) server.getServerContext().getListener("default")).getPort());

    server.start();

    assertTrue(((NioListener) server.getServerContext().getListener("default")).getPort() > 0);
  }
}
