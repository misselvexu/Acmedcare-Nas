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
import com.acmedcare.nas.ftp.server.command.Command;
import com.acmedcare.nas.ftp.server.ftplet.FtpException;
import com.acmedcare.nas.ftp.server.ftplet.FtpReply;
import com.acmedcare.nas.ftp.server.ftplet.FtpRequest;
import com.acmedcare.nas.ftp.server.impl.FtpIoSession;
import com.acmedcare.nas.ftp.server.impl.FtpServerContext;
import com.acmedcare.nas.ftp.server.impl.LocalizedFtpReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <strong>Internal class, do not use directly.</strong>
 * <p>
 * Handle SITE command.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SITE extends AbstractCommand {

  private final Logger LOG = LoggerFactory.getLogger(SITE.class);

  /**
   * Execute command.
   */
  public void execute(final FtpIoSession session,
                      final FtpServerContext context, final FtpRequest request)
      throws IOException, FtpException {

    // get request name
    String argument = request.getArgument();
    if (argument != null) {
      int spaceIndex = argument.indexOf(' ');
      if (spaceIndex != -1) {
        argument = argument.substring(0, spaceIndex);
      }
      argument = argument.toUpperCase();
    }

    // no params
    if (argument == null) {
      session.resetState();
      session.write(LocalizedFtpReply.translate(session, request, context,
          FtpReply.REPLY_200_COMMAND_OKAY, "SITE", null));
      return;
    }

    // call appropriate command method
    String siteRequest = "SITE_" + argument;
    Command command = context.getCommandFactory().getCommand(siteRequest);
    try {
      if (command != null) {
        command.execute(session, context, request);
      } else {
        session.resetState();
        session.write(LocalizedFtpReply.translate(session, request, context,
            FtpReply.REPLY_502_COMMAND_NOT_IMPLEMENTED, "SITE",
            argument));
      }
    } catch (Exception ex) {
      LOG.warn("SITE.execute()", ex);
      session.resetState();
      session.write(LocalizedFtpReply.translate(session, request, context,
          FtpReply.REPLY_500_SYNTAX_ERROR_COMMAND_UNRECOGNIZED,
          "SITE", null));
    }

  }
}
