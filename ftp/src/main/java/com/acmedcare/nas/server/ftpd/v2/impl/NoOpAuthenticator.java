/*
 * Copyright 2017 Acmedcare+
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.acmedcare.nas.server.ftpd.v2.impl;

import com.acmedcare.nas.server.ftpd.v2.FTPConnection;
import com.acmedcare.nas.server.ftpd.v2.api.FileSystem;
import com.acmedcare.nas.server.ftpd.v2.api.UserAuthenticator;

import java.net.InetAddress;

/**
 * No Operation Authenticator
 *
 * <p>Allows any user in with a predefined file system
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class NoOpAuthenticator implements UserAuthenticator {

  private final FileSystem fs;

  /**
   * Creates the authenticator
   *
   * @param fs A file system
   * @see NativeFileSystem
   */
  public NoOpAuthenticator(FileSystem fs) {
    this.fs = fs;
  }

  @Override
  public boolean needsUsername(FTPConnection con) {
    return false;
  }

  @Override
  public boolean needsPassword(FTPConnection con, String username, InetAddress address) {
    return false;
  }

  @Override
  public FileSystem authenticate(
      FTPConnection con, InetAddress address, String username, String password)
      throws AuthException {
    return fs;
  }
}
