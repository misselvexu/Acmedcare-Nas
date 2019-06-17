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

package com.acmedcare.nas.ftp.server.command.impl;

import com.acmedcare.nas.ftp.server.command.AbstractCommand;
import com.acmedcare.nas.ftp.server.ftplet.FtpException;
import com.acmedcare.nas.ftp.server.ftplet.FtpReply;
import com.acmedcare.nas.ftp.server.ftplet.FtpRequest;
import com.acmedcare.nas.ftp.server.impl.FtpIoSession;
import com.acmedcare.nas.ftp.server.impl.FtpServerContext;
import com.acmedcare.nas.ftp.server.impl.LocalizedFtpReply;

import java.io.IOException;

/**
 * <strong>Internal class, do not use directly.</strong>
 * <p>
 * The FEAT command (introduced in [RFC-2389]) allows servers with additional
 * features to advertise these to a client by responding to the FEAT command. If
 * a server supports the FEAT command then it MUST advertise supported AUTH,
 * PBSZ and PROT commands in the reply.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class FEAT extends AbstractCommand {

  public void execute(final FtpIoSession session,
                      final FtpServerContext context, final FtpRequest request)
      throws IOException, FtpException {

    // reset state variables
    session.resetState();

    session.write(LocalizedFtpReply.translate(session, request, context,
        FtpReply.REPLY_211_SYSTEM_STATUS_REPLY, "FEAT", null));
  }
}
