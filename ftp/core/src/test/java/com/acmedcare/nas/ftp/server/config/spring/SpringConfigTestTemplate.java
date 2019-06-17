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

import com.acmedcare.nas.ftp.server.FtpServer;
import junit.framework.TestCase;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ByteArrayResource;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class SpringConfigTestTemplate extends TestCase {

  protected FtpServer createServer(String config) {
    String completeConfig = "<server id=\"server\" xmlns=\"http://mina.apache.org/ftpserver/spring/v1\" "
        + "xmlns:beans=\"http://www.springframework.org/schema/beans\" "
        + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
        + "xsi:schemaLocation=\" "
        + "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd "
        + "http://mina.apache.org/ftpserver/spring/v1 http://mina.apache.org/ftpserver/ftpserver-1.0.xsd "
        + "\">"
        + config
        + "</server>";

    XmlBeanFactory factory = new XmlBeanFactory(
        new ByteArrayResource(completeConfig.getBytes()));

    return (FtpServer) factory.getBean("server");

  }
}
