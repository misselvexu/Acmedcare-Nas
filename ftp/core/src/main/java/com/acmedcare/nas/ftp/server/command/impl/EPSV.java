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

import com.acmedcare.nas.ftp.server.DataConnectionException;
import com.acmedcare.nas.ftp.server.command.AbstractCommand;
import com.acmedcare.nas.ftp.server.ftplet.FtpReply;
import com.acmedcare.nas.ftp.server.ftplet.FtpRequest;
import com.acmedcare.nas.ftp.server.impl.FtpIoSession;
import com.acmedcare.nas.ftp.server.impl.FtpServerContext;
import com.acmedcare.nas.ftp.server.impl.LocalizedFtpReply;
import com.acmedcare.nas.ftp.server.impl.ServerDataConnectionFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <p>The EPSV command requests that a server listen on a data port and wait for a connection. The
 * EPSV command takes an optional argument. The response to this command includes only the TCP port
 * number of the listening connection. The format of the response, however, is similar to the
 * argument of the EPRT command. This allows the same parsing routines to be used for both commands.
 * In addition, the format leaves a place holder for the network protocol and/or network address,
 * which may be needed in the EPSV response in the future. The response code for entering passive
 * mode using an extended address MUST be 229.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class EPSV extends AbstractCommand {

  /** Execute command. */
  @Override
  public void execute(
      final FtpIoSession session, final FtpServerContext context, final FtpRequest request)
      throws IOException {

    // reset state variables
    session.resetState();

    // set data connection
    ServerDataConnectionFactory dataCon = session.getDataConnection();

    try {
      InetSocketAddress dataConAddress = dataCon.initPassiveDataConnection();
      // get connection info
      int servPort = dataConAddress.getPort();

      // send connection info to client
      String portStr = "|||" + servPort + '|';
      session.write(LocalizedFtpReply.translate(session, request, context, 229, "EPSV", portStr));

    } catch (DataConnectionException e) {
      session.write(
          LocalizedFtpReply.translate(
              session,
              request,
              context,
              FtpReply.REPLY_425_CANT_OPEN_DATA_CONNECTION,
              "EPSV",
              null));
      return;
    }
  }
}
