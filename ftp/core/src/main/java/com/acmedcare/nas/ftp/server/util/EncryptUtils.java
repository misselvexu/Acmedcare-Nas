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

package com.acmedcare.nas.ftp.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <strong>Internal class, do not use directly.</strong>
 * <p>
 * String encryption utility methods.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class EncryptUtils {

  /**
   * Encrypt byte array.
   */
  public final static byte[] encrypt(byte[] source, String algorithm)
      throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance(algorithm);
    md.reset();
    md.update(source);
    return md.digest();
  }

  /**
   * Encrypt string
   */
  public final static String encrypt(String source, String algorithm)
      throws NoSuchAlgorithmException {
    byte[] resByteArray = encrypt(source.getBytes(), algorithm);
    return StringUtils.toHexString(resByteArray);
  }

  /**
   * Encrypt string using MD5 algorithm
   */
  public final static String encryptMD5(String source) {
    if (source == null) {
      source = "";
    }

    String result = "";
    try {
      result = encrypt(source, "MD5");
    } catch (NoSuchAlgorithmException ex) {
      // this should never happen
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   * Encrypt string using SHA algorithm
   */
  public final static String encryptSHA(String source) {
    if (source == null) {
      source = "";
    }

    String result = "";
    try {
      result = encrypt(source, "SHA");
    } catch (NoSuchAlgorithmException ex) {
      // this should never happen
      throw new RuntimeException(ex);
    }
    return result;
  }

}
