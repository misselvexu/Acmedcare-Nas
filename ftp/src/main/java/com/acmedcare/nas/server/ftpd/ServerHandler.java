/**
 *  ServerHandler
 *  Copyright 2004 by Michael Peter Christen,
 *  mc@anomic.de, Frankfurt a. M., Germany
 *  first published on http://www.anomic.de
 *  last major change: 09.03.2004
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program in the file lgpl21.txt
 *  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 ServerHandler:

 A Generic Server becomes a server for s specific protocol by impementation of
 a corresponding handler class. The handler class provides methods for each
 command of the protocol that is implemented.
 The Handler class is assigned to the ServerCore by passing the handlers
 name to the ServerCore upon initialization.
 Example:
 ServerCore server = new ServerCore(port, 1000, 0, false, "FtpdProtocol", null, 0);
 In this example the protocol handler "FtpdProtocol" is assigned. There a class
 named FtpdProtocol.java must be implemented, that implements this interface,
 a ServerHandler.
 Any protocol command can be implemented in either way:

 public String      COMMAND(String arg) throws IOException;
 public InputStream COMMAND(String arg) throws IOException;
 public void        COMMAND(String arg) throws IOException;

 ..where COMMAND is the command that had been passed to the server
 on the terminal connection. The 'arg' argument is the remaining part of
 the command on the terminal connection.
 If the handler method returns a NULL value, which is especially
 the case if the method implements a 'void' return-value method,
 then the server disconnects the connection.
 Any other return value (String or an InputStream) is returned to
 the client on it's own line through the terminal connection.
 If it is wanted that the server terminates right after submitting
 a last line, then this can be indicated by prefixing the return
 value by a '!'-character.

 If one of the command methods throws a IOException, then the
 server asks the error - method for a return value on the terminal
 connection.

 The greeting-method is used to request a string that is transmitted
 to the client as terminal output at the beginning of a connection
 session.
 */

package com.acmedcare.nas.server.ftpd;

public interface ServerHandler {

	// an init method that the server calls to provide hooks and
	// information to the session's sockets and information
	// the Switchboard allowes to trigger events through all sessions
	// and an supervision process.
	// the init-method is called once right after initialization of the
	// handler Object.
	public void init(ServerCore.Session session, ServerSwitch switchboard)
			throws java.io.IOException;

	// a response line upon connection is send to client
	// if no response line is wanted, return "" or null
	public String greeting();

	// return string in case of any error that occurs during communication
	// is always (but not only) called if an IO-dependent exception occurs.
	public String error(Throwable e);

}
