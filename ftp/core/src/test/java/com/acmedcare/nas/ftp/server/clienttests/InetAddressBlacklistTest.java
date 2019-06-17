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
import org.apache.commons.net.ftp.FTPConnectionClosedException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class InetAddressBlacklistTest extends ClientTestTemplate {
  @Override
  protected FtpServerFactory createServer() throws Exception {
    FtpServerFactory server = super.createServer();

    ListenerFactory factory = new ListenerFactory(server.getListener("default"));

    List<InetAddress> blockedAddresses = new ArrayList<InetAddress>();
    blockedAddresses.add(InetAddress.getByName("localhost"));

    factory.setBlockedAddresses(blockedAddresses);

    server.addListener("default", factory.createListener());

    return server;
  }

  @Override
  protected boolean isConnectClient() {
    return false;
  }

  public void testConnect() throws Exception {
    try {
      client.connect("localhost", getListenerPort());
      fail("Must throw");
    } catch (FTPConnectionClosedException e) {
      // OK
    }
  }
}
