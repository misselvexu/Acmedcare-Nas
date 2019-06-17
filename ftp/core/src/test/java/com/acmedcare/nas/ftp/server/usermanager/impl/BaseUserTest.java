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
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class BaseUserTest extends TestCase {

  private static final Authority ALWAYS_ALLOW_AUTHORITY =
      new Authority() {
        @Override
        public AuthorizationRequest authorize(AuthorizationRequest request) {
          return request;
        }

        @Override
        public boolean canAuthorize(AuthorizationRequest request) {
          return true;
        }
      };

  private static final Authority NEVER_ALLOW_AUTHORITY =
      new Authority() {
        @Override
        public AuthorizationRequest authorize(AuthorizationRequest request) {
          return null;
        }

        @Override
        public boolean canAuthorize(AuthorizationRequest request) {
          return true;
        }
      };

  private static final Authority CANT_AUTHORITY =
      new Authority() {
        @Override
        public AuthorizationRequest authorize(AuthorizationRequest request) {
          return null;
        }

        @Override
        public boolean canAuthorize(AuthorizationRequest request) {
          return false;
        }
      };

  private static final AuthorizationRequest REQUEST = new AuthorizationRequest() {};

  private BaseUser user = new BaseUser();

  public void testAllow() {
    List<Authority> authorities = new ArrayList<Authority>();
    authorities.add(ALWAYS_ALLOW_AUTHORITY);

    user.setAuthorities(authorities);

    assertSame(REQUEST, user.authorize(REQUEST));
  }

  public void testDisallow() {
    List<Authority> authorities = new ArrayList<Authority>();
    authorities.add(NEVER_ALLOW_AUTHORITY);

    user.setAuthorities(authorities);

    assertNull(user.authorize(REQUEST));
  }

  public void testMultipleDisallowLast() {
    List<Authority> authorities = new ArrayList<Authority>();
    authorities.add(ALWAYS_ALLOW_AUTHORITY);
    authorities.add(NEVER_ALLOW_AUTHORITY);

    user.setAuthorities(authorities);

    assertNull(user.authorize(REQUEST));
  }

  public void testMultipleAllowLast() {
    List<Authority> authorities = new ArrayList<Authority>();
    authorities.add(NEVER_ALLOW_AUTHORITY);
    authorities.add(ALWAYS_ALLOW_AUTHORITY);

    user.setAuthorities(authorities);

    assertNull(user.authorize(REQUEST));
  }

  public void testNonCanAuthorize() {
    List<Authority> authorities = new ArrayList<Authority>();
    authorities.add(CANT_AUTHORITY);

    user.setAuthorities(authorities);

    assertNull(user.authorize(REQUEST));
  }
}
