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
import com.acmedcare.nas.ftp.server.message.MessageResource;

import java.io.IOException;
import java.util.List;

/**
 * <strong>Internal class, do not use directly.</strong>
 * <p>
 * A new command "LANG" is added to the FTP command set to allow server-FTP
 * process to determine in which language to present server greetings and the
 * textual part of command responses.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LANG extends AbstractCommand {

  /**
   * Execute command.
   */
  public void execute(final FtpIoSession session,
                      final FtpServerContext context, final FtpRequest request)
      throws IOException, FtpException {

    // reset state
    session.resetState();

    // default language
    String language = request.getArgument();
    if (language == null) {
      session.setLanguage(null);
      session.write(LocalizedFtpReply.translate(session, request, context,
          FtpReply.REPLY_200_COMMAND_OKAY, "LANG", null));
      return;
    }

    // check and set language
    language = language.toLowerCase();
    MessageResource msgResource = context.getMessageResource();
    List<String> availableLanguages = msgResource.getAvailableLanguages();
    if (availableLanguages != null) {
      for (int i = 0; i < availableLanguages.size(); ++i) {
        if (availableLanguages.get(i).equals(language)) {
          session.setLanguage(language);
          session.write(LocalizedFtpReply.translate(session, request,
              context, FtpReply.REPLY_200_COMMAND_OKAY, "LANG",
              null));
          return;
        }
      }
    }

    // not found - send error message
    session.write(LocalizedFtpReply.translate(session, request, context,
        FtpReply.REPLY_504_COMMAND_NOT_IMPLEMENTED_FOR_THAT_PARAMETER,
        "LANG", null));
  }
}
