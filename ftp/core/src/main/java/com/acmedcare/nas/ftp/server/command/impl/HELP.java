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
import com.acmedcare.nas.ftp.server.message.MessageResource;

import java.io.IOException;

/**
 * <strong>Internal class, do not use directly.</strong> <code>
 * HELP [&lt;SP&gt; <string>] &lt;CRLF&gt;</code><br>
 *
 * <p>This command shall cause the server to send helpful information regarding its implementation
 * status over the control connection to the user. The command may take an argument (e.g., any
 * command name) and return more specific information as a response.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class HELP extends AbstractCommand {

  /** Execute command. */
  @Override
  public void execute(
      final FtpIoSession session, final FtpServerContext context, final FtpRequest request)
      throws IOException {

    // reset state variables
    session.resetState();

    // print global help
    if (!request.hasArgument()) {
      session.write(
          LocalizedFtpReply.translate(
              session, request, context, FtpReply.REPLY_214_HELP_MESSAGE, null, null));
      return;
    }

    // print command specific help if available
    String ftpCmd = request.getArgument().toUpperCase();
    MessageResource resource = context.getMessageResource();
    if (resource.getMessage(FtpReply.REPLY_214_HELP_MESSAGE, ftpCmd, session.getLanguage())
        == null) {
      ftpCmd = null;
    }
    session.write(
        LocalizedFtpReply.translate(
            session, request, context, FtpReply.REPLY_214_HELP_MESSAGE, ftpCmd, null));
  }
}
