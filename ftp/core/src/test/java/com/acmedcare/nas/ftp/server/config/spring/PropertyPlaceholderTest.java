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

package com.acmedcare.nas.ftp.server.config.spring;

import com.acmedcare.nas.ftp.server.impl.DefaultFtpServer;
import junit.framework.TestCase;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class PropertyPlaceholderTest extends TestCase {

  public void test() throws Throwable {
    System.setProperty("port2", "3333");

    FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(
        "src/test/resources/spring-config/config-property-placeholder.xml");

    DefaultFtpServer server = (DefaultFtpServer) ctx.getBean("server");

    assertEquals(2222, server.getListener("listener0").getPort());
    assertEquals(3333, server.getListener("listener1").getPort());
  }
}
