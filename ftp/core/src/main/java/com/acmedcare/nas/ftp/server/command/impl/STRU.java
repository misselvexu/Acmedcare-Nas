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
import com.acmedcare.nas.ftp.server.ftplet.Structure;
import com.acmedcare.nas.ftp.server.impl.FtpIoSession;
import com.acmedcare.nas.ftp.server.impl.FtpServerContext;
import com.acmedcare.nas.ftp.server.impl.LocalizedFtpReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <strong>Internal class, do not use directly.</strong> <code>
 * STRU &lt;SP&gt; &lt;structure-code&gt; &lt;CRLF&gt;</code><br>
 *
 * <p>The argument is a single Telnet character code specifying file structure.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class STRU extends AbstractCommand {

  private final Logger LOG = LoggerFactory.getLogger(STRU.class);

  /** Execute command */
  @Override
  public void execute(
      final FtpIoSession session, final FtpServerContext context, final FtpRequest request)
      throws IOException {

    // reset state variables
    session.resetState();

    // argument check
    if (!request.hasArgument()) {
      session.write(
          LocalizedFtpReply.translate(
              session,
              request,
              context,
              FtpReply.REPLY_501_SYNTAX_ERROR_IN_PARAMETERS_OR_ARGUMENTS,
              "STRU",
              null));
      return;
    }

    // set structure
    char stru = request.getArgument().charAt(0);
    try {
      session.setStructure(Structure.parseArgument(stru));
      session.write(
          LocalizedFtpReply.translate(
              session, request, context, FtpReply.REPLY_200_COMMAND_OKAY, "STRU", null));
    } catch (IllegalArgumentException e) {
      LOG.debug("Illegal structure argument: " + request.getArgument(), e);
      session.write(
          LocalizedFtpReply.translate(
              session,
              request,
              context,
              FtpReply.REPLY_504_COMMAND_NOT_IMPLEMENTED_FOR_THAT_PARAMETER,
              "STRU",
              null));
    }
  }
}
