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

import com.acmedcare.nas.ftp.server.DataConnectionConfigurationFactory;
import com.acmedcare.nas.ftp.server.FtpServerFactory;
import com.acmedcare.nas.ftp.server.listener.ListenerFactory;
import com.acmedcare.nas.ftp.server.test.TestUtil;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

import java.util.Random;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class PasvTest extends ClientTestTemplate {

  @Override
  protected boolean isConnectClient() {
    return false;
  }

  @Override
  protected FtpServerFactory createServer() throws Exception {
    FtpServerFactory server = super.createServer();

    ListenerFactory listenerFactory = new ListenerFactory(server.getListener("default"));

    DataConnectionConfigurationFactory dccFactory = new DataConnectionConfigurationFactory();

    int passivePort = TestUtil.findFreePort(12000 + new Random().nextInt(20000));

    dccFactory.setPassivePorts(passivePort + "-" + passivePort);

    listenerFactory.setDataConnectionConfiguration(dccFactory.createDataConnectionConfiguration());

    server.addListener("default", listenerFactory.createListener());

    return server;
  }

  public void testMultiplePasv() throws Exception {
    for (int i = 0; i < 5; i++) {
      client.connect("localhost", getListenerPort());
      client.login(ADMIN_USERNAME, ADMIN_PASSWORD);
      client.pasv();

      client.quit();
      client.disconnect();
    }
  }

  /**
   * This tests that the correct IP is returned, that is the IP that the
   * client has connected to.
   * <p>
   * Note that this test will only work if you got more than one NIC and the
   * server is allowed to listen an all NICs
   */
  public void testPasvIp() throws Exception {
    String[] ips = TestUtil.getHostAddresses();

    for (int i = 0; i < ips.length; i++) {

      String ip = ips[i];
      String ftpIp = ip.replace('.', ',');

      if (!ip.startsWith("0.")) {
        try {
          client.connect(ip, getListenerPort());
        } catch (FTPConnectionClosedException e) {
          // try again
          Thread.sleep(200);
          client.connect(ip, getListenerPort());
        }
        client.login(ADMIN_USERNAME, ADMIN_PASSWORD);
        client.pasv();

        assertTrue("Can't find " + ftpIp + " in " + client.getReplyString(), client.getReplyString().indexOf(ftpIp) > -1);

        client.quit();
        client.disconnect();
      }
    }
  }
}
