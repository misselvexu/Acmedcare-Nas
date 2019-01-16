/**
 *  Ftpd
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

package com.acmedcare.nas.server.ftpd;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Ftpd {

    public static final String vDATE = "20101121";
    public static final String vNUMBER = "0.98";
    public static final String copyright = "FTP SERVER: AnomicFTPD v" + vNUMBER
            + " build " + vDATE + " (C) by Michael Peter Christen";
    public static final String hline = "-------------------------------------------------------------------------------";
    public static final int timeoutDataConnection = 0; // milliseconds; timeout on data connections; 0=unlimited
    public static final int timeoutSocketConnection = 0; // milliseconds; timeout on Socket connections; 0=unlimited
    public static ServerSwitch settings; // daemon configuration
    public static int port = 2121;
    public static int loglevel = 2;
    public static String charcoding = null;
    public static InetAddress router_ip = null;

    private static SimpleDateFormat currYearFormatter = new SimpleDateFormat("MMM dd HH:mm", Locale.ENGLISH);
    private static SimpleDateFormat prevYearFormatter = new SimpleDateFormat("MMM dd  yyyy", Locale.ENGLISH);
    private static final int nowYear = (new GregorianCalendar(TimeZone.getTimeZone("PST")).get(Calendar.YEAR));
    public static final PrintWriter log = new PrintWriter(new OutputStreamWriter(System.out), true);

    // helper methods for date string in directory listing
    public static String fsDate(final Date d) {
        final Calendar c = new GregorianCalendar();
        c.setTime(d);
        if (c.get(Calendar.YEAR) == nowYear)
            return currYearFormatter.format(d);
        else
            return prevYearFormatter.format(d);
    }

    // helper methods for ip connection and ip class
    public static boolean online() {
        return IPType(publicIP()).startsWith("class");
    }

    public static InetAddress publicIP() {
        if (router_ip != null)
            return router_ip;
        else {
            try {
                // list all addresses
                final InetAddress[] ia = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
                // for (int i = 0; i < ia.length; i++) System.out.println("IP: " + ia[i].getHostAddress()); // DEBUG
                if (ia.length == 0) {
                    try {
                        return InetAddress.getLocalHost();
                    } catch (final UnknownHostException e) {
                        try {
                            return InetAddress.getByName("127.0.0.0");
                        } catch (final UnknownHostException ee) {
                            return null;
                        }
                    }
                }
                if (ia.length == 1)
                    // only one network connection available
                    return ia[0];
                // we have more addresses, find an address that is not local
                int b0, b1;
                for (int i = 0; i < ia.length; i++) {
                    b0 = ipc(ia[i].getAddress()[0]);
                    b1 = ipc(ia[i].getAddress()[1]);
                    if ((b0 != 10) && // class A reserved
                            (b0 != 127) && // loopback
                            ((b0 != 172) || (b1 < 16) || (b1 > 31)) && // class B reserved
                            ((b0 != 192) || (b0 != 168)) && // class C reserved
                            (ia[i].getHostAddress().indexOf(":") < 0))
                        return ia[i];
                }
                // there is only a local address, we filter out the possibly
                // returned loopback address 127.0.0.1
                for (int i = 0; i < ia.length; i++) {
                    if (ipc(ia[i].getAddress()[0]) != 127 && ia[i].getHostAddress().indexOf(":") < 0)
                        return ia[i];
                }
                // if all fails, give back whatever we have
                for (int i = 0; i < ia.length; i++) {
                    if (ia[i].getHostAddress().indexOf(":") < 0)
                        return ia[i];
                }
                return ia[0];
            } catch (final UnknownHostException e) {
                System.err.println("ERROR: (internal) " + e.getMessage());
                return null;
            }
        }
    }

    public static String IPType(final InetAddress ia) {
        /*
         * RFC 1166/1597/1918: Class A : 0.0.0.0 - 126.255.255.255 ## private:
         * 10.0.0.0 - 10.255.255.255 loopback : 127.0.0.0 - 127.255.255.255 ##
         * only 127.0.0.0 used Class B : 128.0.0.0 - 191.255.255.255 ## private:
         * 172.16.0.0 - 172.31.255.255 Class C : 192.0.0.0 - 223.255.255.255 ##
         * private: 192.168.0.0 - 192.168.255.255 Class D : 224.0.0.0 -
         * 239.255.255.255 ## multicast Class E : 240.0.0.0 - 255.255.255.255 ##
         * reserved Where the numbers 0 and 255 are also reserved and mean: 0 =
         * 'this', 255 = 'all'
         */
        // we return a name that reflects the type of the internet address
        // return values are:
        // 'local', 'loopback', 'class-a', 'class-b', 'class-c', 'class-d',
        // 'class-e'
        final int b0 = ipc(ia.getAddress()[0]);
        if (b0 < 127)
            if (b0 == 10)
                return "local";
            else
                return "class-a";
        if (b0 == 127)
            return "loopback";
        final int b1 = ipc(ia.getAddress()[1]);
        if ((b0 > 127) && (b0 < 192)) {
            if ((b0 == 172) && (b1 > 15) && (b1 < 32))
                return "local";
            else
                return "class-b";
        }
        if ((b0 > 191) && (b0 < 224)) {
            if ((b0 == 192) && (b1 == 168))
                return "local";
            else
                return "class-c";
        }
        if ((b0 > 223) && (b0 < 240))
            return "class-d";
        else
            return "class-e";
    }

    private static int ipc(final byte b) {
        return (b >= 0) ? (int) b : ((int) b) + 256;
    }

    // helper methods for system information
    public static String systemOSType() {
        return systemOST() + ", AnomicFTPD v" + vDATE;
    }

    public static String systemOST() {
        String loc = System.getProperty("user.timezone");
        final int p = loc.indexOf(FtpdControl.slash);
        if (p > 0) {
            loc = loc.substring(0, p);
        }
        loc = loc + FtpdControl.slash + System.getProperty("user.language");
        return System.getProperty("os.arch") + " "
                + System.getProperty("os.name") + " "
                + System.getProperty("os.version") + ", " + "java "
                + System.getProperty("java.version") + ", " + loc;
        // UNIX Type: x86 Windows 98 4.10 + java 1.4.0_02 : Europe/de
        // UNIX Type: L8 Version: BSD-199506
    }

    private static String formatString(String s, final int l) {
        while (s.length() < l) {
            s = s + " ";
        }
        return s;
    }

    // application wrapper
    public static void main(final String args[]) {
        try {
            System.out.println(copyright);
            System.out.println(hline);
            System.out.println("Please visit www.anomic.de for latest changes or new documentation.");
            System.out.println("The AnomicFTPD FTP Server comes with ABSOLUTELY NO WARRANTY!");
            System.out.println("This is free software, and you are welcome to redistribute it");
            System.out.println("under certain conditions; see file lgpl21.txt for details.");
            System.out.println(hline);

            // load settings
            settings = new FtpdSwitchboard("ftpd.init", "ftpd.settings");
            if (settings == null) {
                System.exit(1);
            }

            // check options
            boolean options = true;
            while ((options == true) && (args.length > 0)) {
                options = false;
                if (args[0].equals("-router")) {
                    // we will use a router. This will change the behaviour of
                    // the PASV command
                    try {
                        router_ip = InetAddress.getByName(args[1]);
                    } catch (final UnknownHostException e) {
                        router_ip = null;
                    }
                }

            }
            // load accounts
            final Enumeration accNames = FtpdPermissions.enumUsers();

            // set non-string values
            port = Integer.parseInt(settings.getConfig("port", "21"));
            loglevel = Integer.parseInt(settings.getConfig("loglevel", "2"));
            charcoding = settings.getConfig("charcoding", null);
            if (charcoding != null && charcoding.equals("NONE")) {
                charcoding = null;
            }
            currYearFormatter = new SimpleDateFormat(settings.getConfig("currYearFormat", "MMM dd HH:mm"), Locale.ENGLISH);
            prevYearFormatter = new SimpleDateFormat(settings.getConfig("prevYearFormat", "MMM dd yyyy"), Locale.ENGLISH);

            System.out.println("Your Configuration:");
            System.out.println();
            System.out.println("WELCOME STRING : \"" + settings.getConfig("welcome", "WELCOME TO THE ANOMIC FTP SERVER") + "\"");
            System.out.println("SYSTEM         : " + systemOST());
            if (settings.getConfig("clients", "*").length() > 1) {
                System.out.println("CLIENT IP      : " + settings.getConfig("clients", "*"));
            } else {
                System.out.println("CLIENT IP      : " + "* (warning: all clients from any IP may connect!)");
            }
            final int port = Integer.parseInt(settings.getConfig("port", "2121"));
            System.out.println("LOGLEVEL       : " + settings.getConfig("loglevel", "2"));
            System.out.println("LISTENING PORT : " + port);
            final InetAddress thisip = publicIP();
            System.out.println("THIS DOMAIN/IP : " + thisip.getHostAddress());

            System.out.println();
            System.out.println();
            System.out.println("To access this server, your can type in the following URL in your browser:");
            System.out.println();
            System.out.println("QUALIFIED URL  : ftp://<account>:<password>@"
                    + thisip.getHostAddress()
                    + ((port == 21) ? "" : (":" + port)) + FtpdControl.slash);
            System.out.println(" or");
            System.out.println("ANONYMOUS URL  : ftp://"
                    + thisip.getHostAddress()
                    + ((port == 21) ? "" : (":" + port)) + FtpdControl.slash);
            System.out.println();
            System.out.println();
            System.out.println("Active Access Rights:");
            System.out.println();
            System.out.println("  ACTIVE ACCOUNT     GROUP-SPECIFIC ROOT PATH             ACCESS RIGHTS");
            System.out.println("  ------------------ ------------------------------------ ---------------");
            String account;
            String secwarning = "";
            boolean winaccount = false;
            boolean winadmaccount = false;
            boolean unixaccount = false;
            boolean unixadmaccount = false;
            while (accNames.hasMoreElements()) {
                account = (String) accNames.nextElement();
                System.out.println("  "
                                + formatString(account, 18)
                                + " "
                                + formatString(FtpdPermissions.getRoot(account), 36)
                                + " "
                                + ((FtpdPermissions.permissionRead(account)) ? "READ/" : "-/")
                                + ((FtpdPermissions.permissionWrite(account)) ? "WRITE/" : "-/")
                                + ((FtpdPermissions.permissionExec(account)) ? "EXEC" : "-"));
                if (account.equals("bob") && FtpdPermissions.getPassword(account).equals("123password")) {
                    secwarning = secwarning + ", bob";
                    unixaccount = true;
                }
                if (account.equals("jim") && FtpdPermissions.getPassword(account).equals("456password")) {
                    secwarning = secwarning + ", jim";
                    winaccount = true;
                }
                if (account.equals("admin") && FtpdPermissions.getPassword(account).equals("789password")) {
                    secwarning = secwarning + ", admin";
                    winadmaccount = true;
                }
                if (account.equals("macadmin") && FtpdPermissions.getPassword(account).equals("789password")) {
                    secwarning = secwarning + ", macadmin";
                    unixadmaccount = true;
                }
            }
            System.out.println("");
            System.out.println("SEE FILE Ftpd.accounts FOR PASSWORDS, Ftpd.groups FOR PATH DECLARATION");
            if (!(secwarning.equals(""))) {
                // print a security warning that the default passwords have not
                // been changed
                System.out.println("");
                System.out.println("*WARNING*: the default passwords for the accounts " + secwarning.substring(2));
                System.out.println("           have not been changed! To run this server within a public network");
                System.out.println("           it is recommended to change these passwords in 'Ftpd.accounts'.");
                if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0) {
                    if ((winaccount) || (winadmaccount)) {
                        System.out.println("           YOU CAN FULLY ACCESS THIS COMPUTER WITH THE FOLLOWING URL:");
                        if (winaccount) {
                            System.out.println("           ftp://jim:456password@"
                                            + thisip.getHostAddress()
                                            + ((port == 21) ? "" : (":" + port))
                                            + FtpdControl.slash);
                        }
                        if (winadmaccount) {
                            System.out.println("           ftp://admin:789password@"
                                            + thisip.getHostAddress()
                                            + ((port == 21) ? "" : (":" + port))
                                            + FtpdControl.slash);
                        }
                        System.out.println("           This warning will disappear if you change the passwords.");
                    }
                } else {
                    if (unixaccount || unixadmaccount) {
                        System.out.println("           YOU CAN FULLY ACCESS THIS COMPUTER WITH THE FOLLOWING URL:");
                        if (unixaccount) {
                            System.out.println("           ftp://bob:123password@"
                                            + thisip.getHostAddress()
                                            + ((port == 21) ? "" : (":" + port))
                                            + FtpdControl.slash);
                        }
                        if (unixadmaccount) {
                            System.out.println("           ftp://macadmin:789password@"
                                            + thisip.getHostAddress()
                                            + ((port == 21) ? "" : (":" + port))
                                            + FtpdControl.slash);
                        }
                        System.out.println("           This warning will disappear if you change the passwords.");
                    }
                }
            }
            System.out.println(hline);

            try {
                final ServerCore server = new ServerCore(port,
                        1000 /* max # sessions */,
                        0 /* control socket timeout in milliseconds */,
                        false /* terminate sleeping threads */,
                        "FtpdProtocol" /* protocol command class */,
                        settings /* handed to command class */, loglevel /* loglevel */);
                server.run(); // not concurrently on purpose here
            } catch (final Exception e) {
                System.err.println("ERROR: " + e);
                // System.exit(1);
            }

        } catch (final Exception ee) {
            System.out.println("FATAL ERROR: " + ee.getMessage());
            ee.printStackTrace();
        }
    }
}
