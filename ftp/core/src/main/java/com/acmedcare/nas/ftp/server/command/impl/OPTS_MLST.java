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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <p>Client-Server listing negotation. Instruct the server what listing types to include in machine
 * directory/file listings.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class OPTS_MLST extends AbstractCommand {

  private static final String[] AVAILABLE_TYPES = {"Size", "Modify", "Type", "Perm"};

  /** Execute command. */
  @Override
  public void execute(
      final FtpIoSession session, final FtpServerContext context, final FtpRequest request)
      throws IOException, FtpException {

    // reset state
    session.resetState();

    // get the listing types
    String argument = request.getArgument();

    String listTypes;
    String types[];
    int spIndex = argument.indexOf(' ');
    if (spIndex == -1) {
      types = new String[0];
      listTypes = "";
    } else {
      listTypes = argument.substring(spIndex + 1);

      // parse all the type tokens
      StringTokenizer st = new StringTokenizer(listTypes, ";");
      types = new String[st.countTokens()];
      for (int i = 0; i < types.length; ++i) {
        types[i] = st.nextToken();
      }
    }
    // set the list types
    String[] validatedTypes = validateSelectedTypes(types);
    if (validatedTypes != null) {
      session.setAttribute("MLST.types", validatedTypes);
      session.write(
          LocalizedFtpReply.translate(
              session, request, context, FtpReply.REPLY_200_COMMAND_OKAY, "OPTS.MLST", listTypes));
    } else {
      session.write(
          LocalizedFtpReply.translate(
              session,
              request,
              context,
              FtpReply.REPLY_501_SYNTAX_ERROR_IN_PARAMETERS_OR_ARGUMENTS,
              "OPTS.MLST",
              listTypes));
    }
  }

  private String[] validateSelectedTypes(final String types[]) {

    // ignore null types
    if (types == null) {
      return new String[0];
    }

    List<String> selectedTypes = new ArrayList<String>();
    // check all the types
    for (int i = 0; i < types.length; ++i) {
      for (int j = 0; j < AVAILABLE_TYPES.length; ++j) {
        if (AVAILABLE_TYPES[j].equalsIgnoreCase(types[i])) {
          selectedTypes.add(AVAILABLE_TYPES[j]);
          break;
        }
      }
    }

    return selectedTypes.toArray(new String[0]);
  }
}
