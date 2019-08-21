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

package com.acmedcare.nas.ftp.server.usermanager.impl;

import com.acmedcare.nas.ftp.server.ftplet.Authority;
import com.acmedcare.nas.ftp.server.ftplet.AuthorizationRequest;
import com.acmedcare.nas.ftp.server.ftplet.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <p>Generic user class. The user attributes are:
 *
 * <ul>
 *   <li>userid
 *   <li>userpassword
 *   <li>enableflag
 *   <li>homedirectory
 *   <li>writepermission
 *   <li>idletime
 *   <li>uploadrate
 *   <li>downloadrate
 * </ul>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class BaseUser implements User {

  private String name = null;

  private String password = null;

  private int maxIdleTimeSec = 0; // no limit

  private String homeDir = null;

  private boolean isEnabled = true;

  private List<? extends Authority> authorities = new ArrayList<Authority>();

  /** Default constructor. */
  public BaseUser() {}

  /** Copy constructor. */
  public BaseUser(User user) {
    name = user.getName();
    password = user.getPassword();
    authorities = user.getAuthorities();
    maxIdleTimeSec = user.getMaxIdleTime();
    homeDir = user.getHomeDirectory();
    isEnabled = user.getEnabled();
  }

  /** Get the user name. */
  @Override
  public String getName() {
    return name;
  }

  /** Set user name. */
  public void setName(String name) {
    this.name = name;
  }

  /** Get the user password. */
  @Override
  public String getPassword() {
    return password;
  }

  /** Set user password. */
  public void setPassword(String pass) {
    password = pass;
  }

  @Override
  public List<Authority> getAuthorities() {
    if (authorities != null) {
      return Collections.unmodifiableList(authorities);
    } else {
      return null;
    }
  }

  public void setAuthorities(List<Authority> authorities) {
    if (authorities != null) {
      this.authorities = Collections.unmodifiableList(authorities);
    } else {
      this.authorities = null;
    }
  }

  /** Get the maximum idle time in second. */
  @Override
  public int getMaxIdleTime() {
    return maxIdleTimeSec;
  }

  /** Set the maximum idle time in second. */
  public void setMaxIdleTime(int idleSec) {
    maxIdleTimeSec = idleSec;
    if (maxIdleTimeSec < 0) {
      maxIdleTimeSec = 0;
    }
  }

  /** Get the user enable status. */
  @Override
  public boolean getEnabled() {
    return isEnabled;
  }

  /** Set the user enable status. */
  public void setEnabled(boolean enb) {
    isEnabled = enb;
  }

  /** Get the user home directory. */
  @Override
  public String getHomeDirectory() {
    return homeDir;
  }

  /** Set the user home directory. */
  public void setHomeDirectory(String home) {
    homeDir = home;
  }

  /** String representation. */
  @Override
  public String toString() {
    return name;
  }

  /** {@inheritDoc} */
  @Override
  public AuthorizationRequest authorize(AuthorizationRequest request) {
    // check for no authorities at all
    if (authorities == null) {
      return null;
    }

    boolean someoneCouldAuthorize = false;
    for (Authority authority : authorities) {
      if (authority.canAuthorize(request)) {
        someoneCouldAuthorize = true;

        request = authority.authorize(request);

        // authorization failed, return null
        if (request == null) {
          return null;
        }
      }
    }

    if (someoneCouldAuthorize) {
      return request;
    } else {
      return null;
    }
  }

  /** {@inheritDoc} */
  @Override
  public List<Authority> getAuthorities(Class<? extends Authority> clazz) {
    List<Authority> selected = new ArrayList<Authority>();

    for (Authority authority : authorities) {
      if (authority.getClass().equals(clazz)) {
        selected.add(authority);
      }
    }

    return selected;
  }
}
