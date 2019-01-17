package com.acmedcare.nas.server.ftpd.v2;

import com.acmedcare.nas.server.ftpd.v2.api.FTPListener;
import com.acmedcare.nas.server.ftpd.v2.api.FileSystem;
import com.acmedcare.nas.server.ftpd.v2.api.UserAuthenticator;
import com.acmedcare.nas.server.ftpd.v2.impl.NativeFileSystem;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * CustomerServer
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-01-17.
 */
public class CustomerServer {
  public static void main(String[] args) throws Exception {

    // Create the FTP server
    FTPServer server = new FTPServer();

    // Create our custom authenticator
    UserbaseAuthenticator auth = new UserbaseAuthenticator();

    // Register a few users
    auth.registerUser("ftpuser", "111111");

    // Set our custom authenticator
    server.setAuthenticator(auth);

    // Register an instance of this class as a listener
    server.addListener(
        new FTPListener() {
          @Override
          public void onConnected(FTPConnection con) {
            // Creates our command handler
            CommandHandler handler = new CommandHandler(con);

            // Register our custom command
            con.registerCommand("CUSTOM", "CUSTOM <string>", handler::customCommand);
          }

          @Override
          public void onDisconnected(FTPConnection con) {
          }
        });

    // Changes the timeout to 10 minutes
    server.setTimeout(10 * 60 * 1000); // 10 minutes

    // Changes the buffer size
    server.setBufferSize(1024 * 5); // 5 kilobytes

    // Start it synchronously in our localhost and in the port 21
    server.listenSync(21);
  }

  /**
   * A simple user base which encodes passwords in MD5 (not really for security, it's just as an
   * example)
   *
   * @author Guilherme Chaguri
   */
  public static class UserbaseAuthenticator implements UserAuthenticator {

    private final Map<String, byte[]> userbase = new HashMap<>();

    private byte[] toMD5(String pass) {
      try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(pass.getBytes("UTF-8"));
      } catch (Exception ex) {
        return pass.getBytes();
      }
    }

    public void registerUser(String username, String password) {
      userbase.put(username, toMD5(password));
    }

    @Override
    public boolean needsUsername(FTPConnection con) {
      return true;
    }

    @Override
    public boolean needsPassword(FTPConnection con, String username, InetAddress address) {
      return true;
    }

    @Override
    public FileSystem<File> authenticate(
        FTPConnection con, InetAddress address, String username, String password)
        throws AuthException {
      // Check for a user with that username in the database
      if (!userbase.containsKey(username)) {
        throw new AuthException();
      }

      // Gets the correct, original password
      byte[] originalPass = userbase.get(username);

      // Calculates the MD5 for the given password
      byte[] inputPass = toMD5(password);

      // Check for wrong password
      if (!Arrays.equals(originalPass, inputPass)) {
        throw new AuthException();
      }

      // Use the username as a directory for file storage
      File path = new File(System.getProperty("nas.ftp.dir"));
      return new NativeFileSystem(path);
    }
  }

  /**
   * A class that handle a custom command. You can easily add more commands with more methods
   *
   * @author Guilherme Chaguri
   */
  public static class CommandHandler {

    private final FTPConnection con;

    public CommandHandler(FTPConnection con) {
      this.con = con;
    }

    // Our custom command
    public void customCommand(String argument) throws IOException {
      // In FileZilla, you can run custom commands at "Server" -> "Enter custom command"
      System.out.println("Here is the argument: " + argument);

      // You can send custom responses for this command
      // If you don't store the FTPConnection, you can use throw an exception instead of using
      // con.sendResponse
      // Check for existing response codes in the specification (
      // https://tools.ietf.org/html/rfc959#page-39 )
      // If you don't send any response, a "200 Done" will be automatically sent
      con.sendResponse(200, ":D");
      /*throw new ResponseException(200, ":D");*/
    }
  }
}
