package com.acmedcare.nas.ftp.server.examples;

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

import com.acmedcare.nas.ftp.server.FtpServer;
import com.acmedcare.nas.ftp.server.FtpServerFactory;
import com.acmedcare.nas.ftp.server.listener.ListenerFactory;
import com.acmedcare.nas.ftp.server.ssl.SslConfigurationFactory;
import com.acmedcare.nas.ftp.server.usermanager.PropertiesUserManagerFactory;
import com.acmedcare.nas.ftp.server.usermanager.SaltedPasswordEncryptor;

import java.io.File;

public class EmbeddingFtpServer {

  public static void main(String[] args) throws Exception {
    FtpServerFactory serverFactory = new FtpServerFactory();

    ListenerFactory factory = new ListenerFactory();

    // set the port of the listener
    factory.setPort(2221);

    // define SSL configuration
    SslConfigurationFactory ssl = new SslConfigurationFactory();

    ssl.setKeystoreFile(new File(System.getProperty("user.dir"),"ftp/keystore.jks"));
    ssl.setKeystorePassword("Acmedcare+sslpassword");

    // set the SSL configuration for the listener
    factory.setSslConfiguration(ssl.createSslConfiguration());
    factory.setImplicitSsl(false);

    // replace the default listener
    serverFactory.addListener("default", factory.createListener());

    PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
    userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
    userManagerFactory.setFile(new File(System.getProperty("user.dir"),"ftp/nas-users.properties"));

    serverFactory.setUserManager(userManagerFactory.createUserManager());

    // start the server
    FtpServer server = serverFactory.createServer();

    server.start();
  }
}
