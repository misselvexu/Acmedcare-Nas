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

import com.acmedcare.nas.ftp.server.FtpServer;
import com.acmedcare.nas.ftp.server.FtpServerConfigurationException;
import com.acmedcare.nas.ftp.server.FtpServerFactory;
import com.acmedcare.nas.ftp.server.listener.Listener;
import com.acmedcare.nas.ftp.server.listener.ListenerFactory;
import junit.framework.TestCase;

import java.net.BindException;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DefaultFtpServerTest extends TestCase {

  public void testFailStartingSecondListener() throws Exception {
    // FTPSERVER-197

    FtpServerFactory serverFactory = new FtpServerFactory();

    ListenerFactory listenerFactory = new ListenerFactory();
    listenerFactory.setPort(0);

    // let's create two listeners on the same port, second should not start

    Listener defaultListener = listenerFactory.createListener();
    Listener secondListener = listenerFactory.createListener();


    serverFactory.addListener("default", defaultListener);
    serverFactory.addListener("second", secondListener);

    FtpServer server = serverFactory.createServer();

    try {
      server.start();

      // Windows seems to allow for both listeners to bind on the same port...
      //fail("Must throw FtpServerConfigurationException");
    } catch (FtpServerConfigurationException e) {
      if (e.getCause() instanceof BindException) {
        // OK!

        // we failed to start, make sure things are shut down correctly
        assertTrue(defaultListener.isStopped());
        assertTrue(secondListener.isStopped());
        assertTrue(server.isStopped());
      } else {
        throw e;
      }
    }
  }

  public void testStartFtpServer() throws Exception {
    // FTPSERVER-197

    FtpServerFactory serverFactory = new FtpServerFactory();

    ListenerFactory listenerFactory = new ListenerFactory();
    listenerFactory.setPort(0);

    // let's create two listeners on the same port, second should not start

    Listener defaultListener = listenerFactory.createListener();
    Listener secondListener = listenerFactory.createListener();


    serverFactory.addListener("default", defaultListener);

    FtpServer server = serverFactory.createServer();

    try {
      server.start();

      // Windows seems to allow for both listeners to bind on the same port...
      //fail("Must throw FtpServerConfigurationException");
    } catch (FtpServerConfigurationException e) {
      if (e.getCause() instanceof BindException) {
        // OK!

        // we failed to start, make sure things are shut down correctly
        assertTrue(defaultListener.isStopped());
        assertTrue(secondListener.isStopped());
        assertTrue(server.isStopped());
      } else {
        throw e;
      }
    }
  }
}
