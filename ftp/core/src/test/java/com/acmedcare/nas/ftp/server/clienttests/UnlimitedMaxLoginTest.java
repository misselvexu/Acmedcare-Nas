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

import com.acmedcare.nas.ftp.server.ConnectionConfigFactory;
import com.acmedcare.nas.ftp.server.FtpServerFactory;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class UnlimitedMaxLoginTest extends ClientTestTemplate {
  private static final String UNKNOWN_USERNAME = "foo";

  private static final String UNKNOWN_PASSWORD = "bar";

  @Override
  protected FtpServerFactory createServer() throws Exception {
    FtpServerFactory server = super.createServer();

    ConnectionConfigFactory ccFactory = new ConnectionConfigFactory();

    ccFactory.setMaxLoginFailures(0);

    server.setConnectionConfig(ccFactory.createConnectionConfig());
    return server;
  }

  public void testLogin() throws Exception {
    // must never be disconnected
    assertFalse(client.login(UNKNOWN_USERNAME, UNKNOWN_PASSWORD));
    assertFalse(client.login(UNKNOWN_USERNAME, UNKNOWN_PASSWORD));
    assertFalse(client.login(UNKNOWN_USERNAME, UNKNOWN_PASSWORD));
    assertFalse(client.login(UNKNOWN_USERNAME, UNKNOWN_PASSWORD));
    assertFalse(client.login(UNKNOWN_USERNAME, UNKNOWN_PASSWORD));
    assertFalse(client.login(UNKNOWN_USERNAME, UNKNOWN_PASSWORD));
    assertFalse(client.login(UNKNOWN_USERNAME, UNKNOWN_PASSWORD));
  }
}
