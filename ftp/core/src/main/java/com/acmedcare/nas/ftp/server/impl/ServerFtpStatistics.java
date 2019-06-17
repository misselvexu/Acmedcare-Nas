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

import com.acmedcare.nas.ftp.server.ftplet.FtpFile;
import com.acmedcare.nas.ftp.server.ftplet.FtpStatistics;

/**
 * <strong>Internal class, do not use directly.</strong>
 * <p>
 * This is same as <code>FtpStatistics</code> with
 * added observer and setting values functionalities.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface ServerFtpStatistics extends FtpStatistics {

  /**
   * Set statistics observer.
   */
  void setObserver(StatisticsObserver observer);

  /**
   * Set file observer.
   */
  void setFileObserver(FileObserver observer);

  /**
   * Increment upload count.
   */
  void setUpload(FtpIoSession session, FtpFile file, long size);

  /**
   * Increment download count.
   */
  void setDownload(FtpIoSession session, FtpFile file, long size);

  /**
   * Increment make directory count.
   */
  void setMkdir(FtpIoSession session, FtpFile dir);

  /**
   * Decrement remove directory count.
   */
  void setRmdir(FtpIoSession session, FtpFile dir);

  /**
   * Increment delete count.
   */
  void setDelete(FtpIoSession session, FtpFile file);

  /**
   * Increment current connection count.
   */
  void setOpenConnection(FtpIoSession session);

  /**
   * Decrement close connection count.
   */
  void setCloseConnection(FtpIoSession session);

  /**
   * Increment current login count.
   */
  void setLogin(FtpIoSession session);

  /**
   * Increment failed login count.
   */
  void setLoginFail(FtpIoSession session);

  /**
   * Decrement current login count.
   */
  void setLogout(FtpIoSession session);

  /**
   * Reset all cumulative total counters. Do not reset current counters, like
   * current logins, otherwise these will become negative when someone
   * disconnects.
   */
  void resetStatisticsCounters();
}
