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

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class HelpTest extends ClientTestTemplate {

  /*
   * (non-Javadoc)
   *
   * @see ClientTestTemplate#setUp()
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();

    client.login(ADMIN_USERNAME, ADMIN_PASSWORD);
  }

  /*
   * public void testHelpMulti() throws Exception { client.quit();
   *
   * for(int i = 0; i<1000000; i++) { client.connect("localhost", port);
   * client.login(ADMIN_USERNAME, ADMIN_PASSWORD); client.help();
   * client.quit(); } }
   */

  public void testHelp() throws Exception {
    assertEquals(214, client.help());
    assertTrue(client.getReplyString().indexOf("The following commands are implemented") > -1);
  }

  public void testHelpForCWD() throws Exception {
    assertEquals(214, client.help("CWD"));
    assertTrue(client.getReplyString().indexOf("Syntax: CWD") > -1);
  }

  public void testHelpForCWDLowerCase() throws Exception {
    assertEquals(214, client.help("cwd"));
    assertTrue(client.getReplyString().indexOf("Syntax: CWD") > -1);
  }

  public void testHelpForUnknownCommand() throws Exception {
    assertEquals(214, client.help("foo"));
    assertTrue(client.getReplyString().indexOf("The following commands are implemented") > -1);
  }
}
