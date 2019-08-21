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

import com.acmedcare.nas.ftp.server.DataConnectionConfiguration;
import com.acmedcare.nas.ftp.server.command.AbstractCommand;
import com.acmedcare.nas.ftp.server.ftplet.FtpReply;
import com.acmedcare.nas.ftp.server.ftplet.FtpRequest;
import com.acmedcare.nas.ftp.server.impl.FtpIoSession;
import com.acmedcare.nas.ftp.server.impl.FtpServerContext;
import com.acmedcare.nas.ftp.server.impl.LocalizedFtpReply;
import com.acmedcare.nas.ftp.server.util.IllegalInetAddressException;
import com.acmedcare.nas.ftp.server.util.IllegalPortException;
import com.acmedcare.nas.ftp.server.util.SocketAddressEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * <strong>Internal class, do not use directly.</strong> <code>
 * PORT &lt;SP&gt; <host-port> &lt;CRLF&gt;</code><br>
 *
 * <p>The argument is a HOST-PORT specification for the data port to be used in data connection.
 * There are defaults for both the user and server data ports, and under normal circumstances this
 * command and its reply are not needed. If this command is used, the argument is the concatenation
 * of a 32-bit internet host address and a 16-bit TCP port address. This address information is
 * broken into 8-bit fields and the value of each field is transmitted as a decimal number (in
 * character string representation). The fields are separated by commas. A port command would be:
 *
 * <p>PORT h1,h2,h3,h4,p1,p2
 *
 * <p>where h1 is the high order 8 bits of the internet host address.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class PORT extends AbstractCommand {

  private final Logger LOG = LoggerFactory.getLogger(PORT.class);

  /** Execute command. */
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
              "PORT",
              null));
      return;
    }

    // is port enabled
    DataConnectionConfiguration dataCfg = session.getListener().getDataConnectionConfiguration();
    if (!dataCfg.isActiveEnabled()) {
      session.write(
          LocalizedFtpReply.translate(
              session,
              request,
              context,
              FtpReply.REPLY_501_SYNTAX_ERROR_IN_PARAMETERS_OR_ARGUMENTS,
              "PORT.disabled",
              null));
      return;
    }

    InetSocketAddress address;
    try {
      address = SocketAddressEncoder.decode(request.getArgument());

      // port must not be 0
      if (address.getPort() == 0) {
        throw new IllegalPortException("PORT port must not be 0");
      }
    } catch (IllegalInetAddressException e) {
      session.write(
          LocalizedFtpReply.translate(
              session,
              request,
              context,
              FtpReply.REPLY_501_SYNTAX_ERROR_IN_PARAMETERS_OR_ARGUMENTS,
              "PORT",
              null));
      return;
    } catch (IllegalPortException e) {
      LOG.debug("Invalid data port: " + request.getArgument(), e);
      session.write(
          LocalizedFtpReply.translate(
              session,
              request,
              context,
              FtpReply.REPLY_501_SYNTAX_ERROR_IN_PARAMETERS_OR_ARGUMENTS,
              "PORT.invalid",
              null));
      return;
    } catch (UnknownHostException e) {
      LOG.debug("Unknown host", e);
      session.write(
          LocalizedFtpReply.translate(
              session,
              request,
              context,
              FtpReply.REPLY_501_SYNTAX_ERROR_IN_PARAMETERS_OR_ARGUMENTS,
              "PORT.host",
              null));
      return;
    }

    // check IP
    if (dataCfg.isActiveIpCheck()) {
      if (session.getRemoteAddress() instanceof InetSocketAddress) {
        InetAddress clientAddr = ((InetSocketAddress) session.getRemoteAddress()).getAddress();
        if (!address.getAddress().equals(clientAddr)) {
          session.write(
              LocalizedFtpReply.translate(
                  session,
                  request,
                  context,
                  FtpReply.REPLY_501_SYNTAX_ERROR_IN_PARAMETERS_OR_ARGUMENTS,
                  "PORT.mismatch",
                  null));
          return;
        }
      }
    }

    session.getDataConnection().initActiveDataConnection(address);
    session.write(
        LocalizedFtpReply.translate(
            session, request, context, FtpReply.REPLY_200_COMMAND_OKAY, "PORT", null));
  }
}
