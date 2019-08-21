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
import com.acmedcare.nas.ftp.server.command.impl.listing.*;
import com.acmedcare.nas.ftp.server.ftplet.*;
import com.acmedcare.nas.ftp.server.impl.FtpIoSession;
import com.acmedcare.nas.ftp.server.impl.FtpServerContext;
import com.acmedcare.nas.ftp.server.impl.IODataConnectionFactory;
import com.acmedcare.nas.ftp.server.impl.LocalizedFtpReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * <strong>Internal class, do not use directly.</strong> <code>
 * MLSD [&lt;SP&gt; &lt;pathname&gt;] &lt;CRLF&gt;</code><br>
 *
 * <p>This command causes a list to be sent from the server to the passive DTP. The pathname must
 * specify a directory and the server should transfer a list of files in the specified directory. A
 * null argument implies the user's current working or default directory. The data transfer is over
 * the data connection
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MLSD extends AbstractCommand {

  private final Logger LOG = LoggerFactory.getLogger(MLSD.class);

  private final DirectoryLister directoryLister = new DirectoryLister();

  /** Execute command. */
  @Override
  public void execute(
      final FtpIoSession session, final FtpServerContext context, final FtpRequest request)
      throws IOException, FtpException {

    try {

      // reset state
      session.resetState();

      // 24-10-2007 - added check if PORT or PASV is issued, see
      // https://issues.apache.org/jira/browse/FTPSERVER-110
      DataConnectionFactory connFactory = session.getDataConnection();
      if (connFactory instanceof IODataConnectionFactory) {
        InetAddress address = ((IODataConnectionFactory) connFactory).getInetAddress();
        if (address == null) {
          session.write(
              new DefaultFtpReply(
                  FtpReply.REPLY_503_BAD_SEQUENCE_OF_COMMANDS,
                  "PORT or PASV must be issued first"));
          return;
        }
      }

      // get data connection
      session.write(
          LocalizedFtpReply.translate(
              session, request, context, FtpReply.REPLY_150_FILE_STATUS_OKAY, "MLSD", null));

      // print listing data
      DataConnection dataConnection;
      try {
        dataConnection = session.getDataConnection().openConnection();
      } catch (Exception e) {
        LOG.debug("Exception getting the output data stream", e);
        session.write(
            LocalizedFtpReply.translate(
                session,
                request,
                context,
                FtpReply.REPLY_425_CANT_OPEN_DATA_CONNECTION,
                "MLSD",
                null));
        return;
      }

      boolean failure = false;
      try {
        // parse argument
        ListArgument parsedArg = ListArgumentParser.parse(request.getArgument());

        FileFormater formater = new MLSTFileFormater((String[]) session.getAttribute("MLST.types"));

        dataConnection.transferToClient(
            session.getFtpletSession(),
            directoryLister.listFiles(parsedArg, session.getFileSystemView(), formater));
      } catch (SocketException ex) {
        LOG.debug("Socket exception during data transfer", ex);
        failure = true;
        session.write(
            LocalizedFtpReply.translate(
                session,
                request,
                context,
                FtpReply.REPLY_426_CONNECTION_CLOSED_TRANSFER_ABORTED,
                "MLSD",
                null));
      } catch (IOException ex) {
        LOG.debug("IOException during data transfer", ex);
        failure = true;
        session.write(
            LocalizedFtpReply.translate(
                session,
                request,
                context,
                FtpReply.REPLY_551_REQUESTED_ACTION_ABORTED_PAGE_TYPE_UNKNOWN,
                "MLSD",
                null));
      } catch (IllegalArgumentException e) {
        LOG.debug("Illegal listing syntax: " + request.getArgument(), e);
        // if listing syntax error - send message
        session.write(
            LocalizedFtpReply.translate(
                session,
                request,
                context,
                FtpReply.REPLY_501_SYNTAX_ERROR_IN_PARAMETERS_OR_ARGUMENTS,
                "MLSD",
                null));
      }

      // if data transfer ok - send transfer complete message
      if (!failure) {
        session.write(
            LocalizedFtpReply.translate(
                session,
                request,
                context,
                FtpReply.REPLY_226_CLOSING_DATA_CONNECTION,
                "MLSD",
                null));
      }
    } finally {
      session.getDataConnection().closeDataConnection();
    }
  }
}
