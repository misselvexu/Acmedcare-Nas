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

import com.acmedcare.nas.ftp.server.ConnectionConfig;
import com.acmedcare.nas.ftp.server.ConnectionConfigFactory;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DefaultConnectionConfig implements ConnectionConfig {

  private final int maxLogins;

  private final boolean anonymousLoginEnabled;

  private final int maxAnonymousLogins;

  private final int maxLoginFailures;

  private final int loginFailureDelay;

  private final int maxThreads;

  public DefaultConnectionConfig() {
    this(true, 500, 10, 10, 3, 0);
  }

  /** Internal constructor, do not use directly. Use {@link ConnectionConfigFactory} instead */
  public DefaultConnectionConfig(
      boolean anonymousLoginEnabled,
      int loginFailureDelay,
      int maxLogins,
      int maxAnonymousLogins,
      int maxLoginFailures,
      int maxThreads) {
    this.anonymousLoginEnabled = anonymousLoginEnabled;
    this.loginFailureDelay = loginFailureDelay;
    this.maxLogins = maxLogins;
    this.maxAnonymousLogins = maxAnonymousLogins;
    this.maxLoginFailures = maxLoginFailures;
    this.maxThreads = maxThreads;
  }

  @Override
  public int getLoginFailureDelay() {
    return loginFailureDelay;
  }

  @Override
  public int getMaxAnonymousLogins() {
    return maxAnonymousLogins;
  }

  @Override
  public int getMaxLoginFailures() {
    return maxLoginFailures;
  }

  @Override
  public int getMaxLogins() {
    return maxLogins;
  }

  @Override
  public boolean isAnonymousLoginEnabled() {
    return anonymousLoginEnabled;
  }

  @Override
  public int getMaxThreads() {
    return maxThreads;
  }
}
