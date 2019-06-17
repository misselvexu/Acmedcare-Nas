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

import com.acmedcare.nas.ftp.server.ftplet.DataTransferFtpReply;
import com.acmedcare.nas.ftp.server.ftplet.FtpFile;
import com.acmedcare.nas.ftp.server.ftplet.FtpRequest;

/**
 * An implementation of <code>DataTransferReply</code>.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */

public class LocalizedDataTransferFtpReply extends LocalizedFtpReply implements
    DataTransferFtpReply {

  /**
   * The file or directory that data transfer is related to.
   */
  private final FtpFile file;

  /**
   * total number of bytes transferred (bytes sent to the client or receivved
   * from the client)
   */
  private final long bytesTransferred;

  /**
   * Creates a new instance of <code>LocalizedFileTransferReply</code>.
   *
   * @param code             the reply code
   * @param message          the detailed message
   * @param file             the file or directory the data transfer is related to
   * @param bytesTransferred the number of bytes transferred
   */
  public LocalizedDataTransferFtpReply(int code, String message,
                                       FtpFile file, long bytesTransferred) {
    super(code, message);
    this.file = file;
    this.bytesTransferred = bytesTransferred;
  }

  @Override
  public FtpFile getFile() {
    return file;
  }

  @Override
  public long getBytesTransferred() {
    return bytesTransferred;
  }

  /**
   * Returns the localized reply that contains all details about the data
   * transfer.
   *
   * @param session  the FTP session
   * @param request  the FTP request
   * @param context  the FTP server context
   * @param code     the reply code
   * @param subId    the sub message ID
   * @param basicMsg the basic message
   * @param file     the file or directory that was transferred
   * @return the localized reply
   */
  public static LocalizedDataTransferFtpReply translate(FtpIoSession session,
                                                        FtpRequest request, FtpServerContext context, int code, String subId,
                                                        String basicMsg, FtpFile file) {
    String msg = FtpReplyTranslator.translateMessage(session, request,
        context, code, subId, basicMsg);

    return new LocalizedDataTransferFtpReply(code, msg, file, 0);
  }

  /**
   * Returns the localized reply that contains all details about the data
   * transfer.
   *
   * @param session          the FTP session
   * @param request          the FTP request
   * @param context          the FTP server context
   * @param code             the reply code
   * @param subId            the sub message ID
   * @param basicMsg         the basic message
   * @param file             the file or directory that was transferred
   * @param bytesTransferred total number of bytes transferred
   * @return the localized reply
   */
  public static LocalizedDataTransferFtpReply translate(FtpIoSession session,
                                                        FtpRequest request, FtpServerContext context, int code, String subId,
                                                        String basicMsg, FtpFile file, long bytesTransferred) {
    String msg = FtpReplyTranslator.translateMessage(session, request,
        context, code, subId, basicMsg);

    return new LocalizedDataTransferFtpReply(code, msg, file,
        bytesTransferred);
  }
}
