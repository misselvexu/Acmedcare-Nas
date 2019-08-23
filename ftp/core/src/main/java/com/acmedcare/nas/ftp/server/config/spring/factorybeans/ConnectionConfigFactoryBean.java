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

package com.acmedcare.nas.ftp.server.config.spring.factorybeans;

import com.acmedcare.nas.ftp.server.ConnectionConfig;
import com.acmedcare.nas.ftp.server.ConnectionConfigFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Spring {@link FactoryBean} which extends {@link ConnectionConfigFactory} making it easier to use
 * Spring's standard &lt;bean&gt; tag instead of FtpServer's custom XML tags to configure things.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @see ConnectionConfigFactory
 */
public class ConnectionConfigFactoryBean extends ConnectionConfigFactory implements FactoryBean {

  public Object getObject() throws Exception {
    return createConnectionConfig();
  }

  public Class<?> getObjectType() {
    return ConnectionConfig.class;
  }

  public boolean isSingleton() {
    return false;
  }
}