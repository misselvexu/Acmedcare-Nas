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

package com.acmedcare.nas.ftp.server.ftpletcontainer;

import com.acmedcare.nas.ftp.server.FtpServerFactory;
import com.acmedcare.nas.ftp.server.clienttests.ClientTestTemplate;
import com.acmedcare.nas.ftp.server.ftplet.FtpException;
import com.acmedcare.nas.ftp.server.ftplet.FtpSession;
import com.acmedcare.nas.ftp.server.ftplet.Ftplet;
import com.acmedcare.nas.ftp.server.ftplet.FtpletResult;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class FtpLetOnConnectTest extends ClientTestTemplate {
  private static final byte[] TESTDATA = "TESTDATA".getBytes();

  private static final byte[] DOUBLE_TESTDATA = "TESTDATATESTDATA".getBytes();

  private static final File TEST_FILE1 = new File(ROOT_DIR, "test1.txt");

  private static final File TEST_FILE2 = new File(ROOT_DIR, "test2.txt");

  private static final File TEST_DIR1 = new File(ROOT_DIR, "dir1");

  protected FtpletResult mockReturnValue = FtpletResult.DISCONNECT;

  /*
   * (non-Javadoc)
   *
   * @see ClientTestTemplate#setUp()
   */
  @Override
  protected void setUp() throws Exception {
    MockFtplet.callback = new MockFtpletCallback();

    initDirs();

    initServer();
  }

  @Override
  protected FtpServerFactory createServer() throws Exception {
    FtpServerFactory server = super.createServer();

    Map<String, Ftplet> ftplets = new LinkedHashMap<String, Ftplet>();
    ftplets.put("f1", new MockFtplet());
    server.setFtplets(ftplets);

    return server;
  }

  public void testDisconnectOnConnect() throws Exception {
    MockFtplet.callback =
        new MockFtpletCallback() {
          @Override
          public FtpletResult onConnect(FtpSession session) throws FtpException, IOException {
            return mockReturnValue;
          }
        };

    try {
      connectClient();
      fail("Must throw FTPConnectionClosedException");
    } catch (FTPConnectionClosedException e) {
      // OK
    } catch (SocketException e) {
      // OK
    }
  }

  public void testExceptionOnConnect() throws Exception {
    MockFtplet.callback =
        new MockFtpletCallback() {
          @Override
          public FtpletResult onConnect(FtpSession session) throws FtpException, IOException {
            throw new FtpException();
          }
        };

    try {
      connectClient();
      fail("Must throw FTPConnectionClosedException");
    } catch (FTPConnectionClosedException e) {
      // OK
    } catch (SocketException e) {
      // OK
    }
  }

  @Override
  protected void doConnect() throws Exception {
    client.connect("localhost", getListenerPort());
  }
}
