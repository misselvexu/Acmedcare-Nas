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

package com.acmedcare.nas.ftp.server.main;

import com.acmedcare.nas.ftp.server.FtpServer;
import com.acmedcare.nas.ftp.server.ftplet.Authority;
import com.acmedcare.nas.ftp.server.ftplet.UserManager;
import com.acmedcare.nas.ftp.server.impl.DefaultFtpServer;
import com.acmedcare.nas.ftp.server.usermanager.impl.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to add users to the user manager for a particular FtpServer configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class AddUser extends CommandLine {

  /** Instance methods only used internaly */
  protected AddUser() {}

  /**
   * Used to add users to the user manager for a particular FtpServer configuration
   *
   * @param args The first element of this array must specify the kind of configuration to be used
   *     to start the server.
   */
  public static void main(String args[]) {
    AddUser addUser = new AddUser();

    try {

      // get configuration
      FtpServer server = addUser.getConfiguration(args);
      if (server == null) {
        return;
      }

      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      UserManager um = ((DefaultFtpServer) server).getUserManager();

      BaseUser user = new BaseUser();

      System.out.println("Asking for details of the new user");

      System.out.println();
      String userName = askForString(in, "User name:", "User name is mandatory");
      if (userName == null) {
        return;
      }
      user.setName(userName);

      user.setPassword(askForString(in, "Password:"));

      String home = askForString(in, "Home directory:", "Home directory is mandatory");
      if (home == null) {
        return;
      }
      user.setHomeDirectory(home);

      user.setEnabled(askForBoolean(in, "Enabled (Y/N):"));

      user.setMaxIdleTime(askForInt(in, "Max idle time in seconds (0 for none):"));

      List<Authority> authorities = new ArrayList<Authority>();

      if (askForBoolean(in, "Write permission (Y/N):")) {
        authorities.add(new WritePermission());
      }

      int maxLogins = askForInt(in, "Maximum number of concurrent logins (0 for no restriction)");
      int maxLoginsPerIp =
          askForInt(in, "Maximum number of concurrent logins per IP (0 for no restriction)");

      authorities.add(new ConcurrentLoginPermission(maxLogins, maxLoginsPerIp));

      int downloadRate = askForInt(in, "Maximum download rate (0 for no restriction)");
      int uploadRate = askForInt(in, "Maximum upload rate (0 for no restriction)");

      authorities.add(new TransferRatePermission(downloadRate, uploadRate));

      user.setAuthorities(authorities);

      um.save(user);

      if (um instanceof PropertiesUserManager) {
        File file = ((PropertiesUserManager) um).getFile();

        if (file != null) {
          System.out.println("User saved to file: " + file.getAbsolutePath());
        } else {
          System.err.println(
              "User manager does not have a file configured, will not save user to file");
        }
      } else {
        System.out.println("User saved");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static String askForString(BufferedReader in, String question) throws IOException {
    System.out.println(question);
    return in.readLine();
  }

  private static String askForString(BufferedReader in, String question, String mandatoryMessage)
      throws IOException {
    String s = askForString(in, question);
    if (isEmpty(s)) {
      System.err.println(mandatoryMessage);
      return null;
    } else {
      return s;
    }
  }

  private static int askForInt(BufferedReader in, String question) throws IOException {
    System.out.println(question);
    String intValue = in.readLine();
    return Integer.parseInt(intValue);
  }

  private static boolean askForBoolean(BufferedReader in, String question) throws IOException {
    System.out.println(question);
    String boolValue = in.readLine();
    return "Y".equalsIgnoreCase(boolValue);
  }

  private static boolean isEmpty(String s) {
    if (s == null) {
      return true;
    } else {
      return s.trim().length() == 0;
    }
  }

  /** Print the usage message. */
  @Override
  protected void usage() {
    System.err.println("Usage: java " + AddUser.class.getName() + " [OPTION] [CONFIGFILE]");
    System.err.println("Starts the user management application, asking for user settings");
    System.err.println("");
    System.err.println("      --default              use the default configuration, ");
    System.err.println(
        "                             also used if no command line argument is given ");

    System.err.println("  -?, --help                 print this message");
  }
}
