/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.acmedcare.nas.ftp.server.listener.nio;

import com.acmedcare.nas.ftp.server.ftplet.FtpReply;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <p>A {@link MessageEncoder} that encodes {@link FtpReply}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class FtpResponseEncoder extends ProtocolEncoderAdapter {
  private static final CharsetEncoder ENCODER = Charset.forName("UTF-8").newEncoder();

  public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
      throws Exception {
    String value = message.toString();

    IoBuffer buf = IoBuffer.allocate(value.length()).setAutoExpand(true);

    buf.putString(value, ENCODER);

    buf.flip();
    out.write(buf);
  }
}
