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
import com.acmedcare.nas.ftp.server.ftplet.FtpReply;
import com.acmedcare.nas.ftp.server.ftplet.FtpRequest;
import com.acmedcare.nas.ftp.server.impl.FtpIoSession;
import com.acmedcare.nas.ftp.server.impl.FtpServerContext;
import com.acmedcare.nas.ftp.server.impl.LocalizedFtpReply;

import java.io.IOException;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <code>MODE &lt;SP&gt; <mode-code> &lt;CRLF&gt;</code><br>
 * <p>
 * The argument is a single Telnet character code specifying the data transfer
 * modes described in the Section on Transmission Modes.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MODE extends AbstractCommand {

  /**
   * Execute command
   */
  public void execute(final FtpIoSession session,
                      final FtpServerContext context, final FtpRequest request)
      throws IOException {

    // reset state
    session.resetState();

    // argument check
    if (!request.hasArgument()) {
      session.write(LocalizedFtpReply.translate(session, request, context,
          FtpReply.REPLY_501_SYNTAX_ERROR_IN_PARAMETERS_OR_ARGUMENTS,
          "MODE", null));
      return;
    }

    // set mode
    char md = request.getArgument().charAt(0);
    md = Character.toUpperCase(md);
    if (md == 'S') {
      session.getDataConnection().setZipMode(false);
      session.write(LocalizedFtpReply.translate(session, request, context,
          FtpReply.REPLY_200_COMMAND_OKAY, "MODE", "S"));
    } else if (md == 'Z') {
      session.getDataConnection().setZipMode(true);
      session.write(LocalizedFtpReply.translate(session, request, context,
          FtpReply.REPLY_200_COMMAND_OKAY, "MODE", "Z"));
    } else {
      session
          .write(LocalizedFtpReply
              .translate(
                  session,
                  request,
                  context,
                  FtpReply.REPLY_504_COMMAND_NOT_IMPLEMENTED_FOR_THAT_PARAMETER,
                  "MODE", null));
    }
  }
}
