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
import com.acmedcare.nas.ftp.server.ipfilter.IpFilterType;
import com.acmedcare.nas.ftp.server.ipfilter.RemoteIpFilter;
import com.acmedcare.nas.ftp.server.listener.ListenerFactory;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.mina.filter.firewall.Subnet;

import java.net.InetAddress;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class IpFilterTest extends ClientTestTemplate {

  private RemoteIpFilter filter = new RemoteIpFilter(IpFilterType.DENY);

  @Override
  protected FtpServerFactory createServer() throws Exception {
    FtpServerFactory server = super.createServer();

    ListenerFactory factory = new ListenerFactory(server.getListener("default"));

    factory.setSessionFilter(filter);
    server.addListener("default", factory.createListener());

    return server;
  }

  @Override
  protected boolean isConnectClient() {
    return false;
  }

  public void testDenyBlackList() throws Exception {
    filter.clear();
    filter.setType(IpFilterType.DENY);
    filter.add(new Subnet(InetAddress.getByName("localhost"), 32));
    try {
      client.connect("localhost", getListenerPort());
      fail("Must throw");
    } catch (FTPConnectionClosedException e) {
      // OK
    }
  }

  public void testDenyEmptyWhiteList() throws Exception {
    filter.clear();
    filter.setType(IpFilterType.ALLOW);
    try {
      client.connect("localhost", getListenerPort());
      fail("Must throw");
    } catch (FTPConnectionClosedException e) {
      // OK
    }
  }

  public void testWhiteList() throws Exception {
    filter.clear();
    filter.setType(IpFilterType.ALLOW);
    filter.add(new Subnet(InetAddress.getByName("localhost"), 32));
    client.connect("localhost", getListenerPort());
  }
}
