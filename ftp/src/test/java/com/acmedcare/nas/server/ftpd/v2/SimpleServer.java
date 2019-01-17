package com.acmedcare.nas.server.ftpd.v2;

import com.acmedcare.nas.server.ftpd.v2.impl.NativeFileSystem;
import com.acmedcare.nas.server.ftpd.v2.impl.NoOpAuthenticator;

import java.io.File;
import java.io.IOException;

public class SimpleServer {

  public static void main(String[] args) throws IOException {
    // Uses the current working directory as the root
    File root = new File("/Users/misselvexu/Documents/acmedcare.gitlab.com/Acmedcare-Nas/ftp.dir/pub");

    // Creates a native file system
    NativeFileSystem fs = new NativeFileSystem(root);

    // Creates a noop authenticator
    NoOpAuthenticator auth = new NoOpAuthenticator(fs);

    // Creates the server with the authenticator
    FTPServer server = new FTPServer(auth);

    // Start listening synchronously
    server.listenSync(2121);
  }
}
