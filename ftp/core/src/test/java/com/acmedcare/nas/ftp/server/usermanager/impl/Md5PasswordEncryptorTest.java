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

import com.acmedcare.nas.ftp.server.usermanager.Md5PasswordEncryptor;
import com.acmedcare.nas.ftp.server.usermanager.PasswordEncryptor;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class Md5PasswordEncryptorTest extends ClearTextPasswordEncryptorTest {

  @Override
  protected PasswordEncryptor createPasswordEncryptor() {
    return new Md5PasswordEncryptor();
  }

  @Override
  public void testMatches() {
    PasswordEncryptor encryptor = createPasswordEncryptor();

    assertTrue(encryptor.matches("foo", "ACBD18DB4CC2F85CEDEF654FCCC4A4D8"));

    // check lower case
    assertTrue(encryptor.matches("foo", "acbd18DB4CC2F85CEDEF654FCCC4A4D8"));

    assertFalse(encryptor.matches("foo", "bar"));
  }
}
