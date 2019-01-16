/**
 *  FtpdControl
 *  Copyright 2004 by Michael Peter Christen,
 *  mc@anomic.de, Frankfurt a. M., Germany
 *  first published 09.03.2004 on http://www.anomic.de
 *  last major change: 29.11.2010
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
 FtpdControl documentation:

 The AnomicFTPD server provides 'hooks' that can be used to perform actions
 bevore a special task is performed and after the task has been completed.
 By altering this class you can get complete control over the virtual file
 system management, directory listing formats and the behaviour of most FTP
 commands regarding permissions (see ftpdPermission.java).
 */

package com.acmedcare.nas.server.ftpd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class FtpdControl {

    // statics
    protected static final SimpleDateFormat MFMTFormatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
    protected static final SimpleDateFormat MDTMFormatter = new SimpleDateFormat("yyyyMMddHHmmss.SSS", Locale.ENGLISH);
    protected static final TimeZone GMTTimeZone = TimeZone.getTimeZone("PST"); // the GMT Time Zone

    public final static String slash = "/";
    public final static String backslash = "\\";
    
    // class variables that are also used by FtpdProtocol
    protected String user; // the current user
    protected InetAddress userAddress; // the address of the client
    protected long start; // start-stop time
    protected String userWD; // current working directory of the user (the virtual path)
    protected Hashtable opts; // property set for the opts command
    protected ServerSwitch switchboard; // configuration and event manager

    public FtpdControl() {
        this.user = null;
        this.userAddress = null;
        this.switchboard = null;
        this.opts = new Hashtable();
        this.userWD = slash;
    }

    // string coding management
    protected String encodeChar(final String s) {
        if ((Ftpd.charcoding == null) || (Ftpd.charcoding.equals("NONE")))
            return s;
        try {
            String se = new String(s.getBytes(Ftpd.charcoding));
            int p;
            while ((p = se.indexOf("%20")) >= 0) {
                se = se.substring(0, p) + " " + se.substring(p + 3);
            }
            // if (!(se.equals(s))) System.out.println("ENCODED: '" + s + "' to '" + se + "'");
            return se;
        } catch (final IllegalArgumentException e) { // error on java 1.6
            printlog(0, "IRREGULARITY: string encoding '" + Ftpd.charcoding + "' failed (java version too low)");
            Ftpd.charcoding = null; // we will not succeed with that
            return s;
        } catch (final UnsupportedEncodingException e) {
            printlog(0, "IRREGULARITY: string encoding '" + Ftpd.charcoding + "' failed (java version too low)");
            Ftpd.charcoding = null; // we will not succeed with that
            return s;
        }
    }

    protected String decodeChar(final String s) {
        if ((Ftpd.charcoding == null) || (Ftpd.charcoding.equals("NONE")))
            return s;
        try {
            String sd = new String(s.getBytes(), Ftpd.charcoding);
            int p;
            while ((p = sd.indexOf("%20")) >= 0) {
                sd = sd.substring(0, p) + " " + sd.substring(p + 3);
            }
            // if (!(sd.equals(s))) System.out.println("DECODED: '" + s +"' to '" + sd + "'");
            return sd;
        } catch (final IllegalArgumentException e) { // error on java 1.6
            printlog(0, "IRREGULARITY: string decoding '" + Ftpd.charcoding
                    + "' failed (java version too low)");
            Ftpd.charcoding = null; // we will not succeed with that
            return s;
        } catch (final UnsupportedEncodingException e) {
            printlog(0, "IRREGULARITY: string decoding '" + Ftpd.charcoding
                    + "' failed (java version too low)");
            Ftpd.charcoding = null; // we will not succeed with that
            return s;
        }
    }

    /**
     * generates a file object pointing to a directory in the systems current path
     * of the server working directory.
     * @return the current server path as path in the operating system
     */
    protected File serverPath() {
        // construct real absolute paths out of virtual absolute paths
        String root = FtpdPermissions.getRoot(this.user);
        if (root == null) return null;
        File rootf = new File(root);
        return new File(rootf, this.userWD.substring(1));
    }

    /**
     * translate a given file name into a path of the operating system using
     * the current working directory of the ftp server
     * @param filename
     * @return the server file as file in the operating system
     */
    protected File serverFile(final String filename) {
        // construct real absolute paths out of virtual absolute paths
        if (filename == null || filename.length() == 0) {
            return serverPath();
        } else if (filename.startsWith(slash) || filename.startsWith(backslash)) {
            String root = FtpdPermissions.getRoot(this.user);
            if (root == null) return null;
            File rootf = new File(root);
            return new File(rootf, filename.substring(1));
        } else {
            File sp = serverPath();
            if (sp == null) return null;
            return new File(sp, filename);
        }
    }

    /**
     * adds a file name to the current working directory of the server working path
     * @param filename
     * @return a virtual absolute path in the server working directory
     */
    public String appendUserFile(String filename) {
        if (filename.startsWith(slash) || filename.startsWith(backslash)) {
            // the file is absolute
            return slash + filename.substring(1);
        }
        if (filename.endsWith(slash) || filename.endsWith(backslash)) {
            filename = filename.substring(0, filename.length() - 1);
        }
        if (this.userWD.endsWith(slash)) return this.userWD + filename;
        return this.userWD + slash + filename;
    }
    
    public String removeUserSubPath() {
        String s = this.userWD;
        if (s.endsWith(slash)) s = s.substring(0, s.length() - 1);
        int p = s.lastIndexOf(slash);
        if (p < 0) return this.userWD;
        return s.substring(0, p + 1); // last slash will be at the end of the path string
    }
    
    // HOOKS

    // CREATION OF FOLDERS
    protected boolean eventMakeFolderPre(final File path) {
        return FtpdPermissions.permissionWrite(this.user);
    }

    protected void eventMakeFolderPost(final File path) {
        if (Ftpd.port < 1000) {
            changeaccess(path);
        }
    }

    // DELETION OF FILES
    protected boolean eventDeleteFilePre(final File path) {
        return FtpdPermissions.permissionWrite(this.user);
    }

    protected void eventDeleteFilePost(final File path) {
    }

    // DELETION OF FOLDERS
    protected boolean eventDeleteFolderPre(final File path) {
        if (!FtpdPermissions.permissionWrite(this.user))
            return false;
        final String[] l = path.list();
        for (int i = 0; i < l.length; i++) {
            if (invisibleFile(l[i])) {
                (new File(path, l[i])).delete();
            }
        }
        return true;
    }

    protected void eventDeleteFolderPost(final File path) {
    }

    // DOWNLOAD OF FILES
    protected boolean eventDownloadFilePre(final File path) {
        final boolean p = FtpdPermissions.permissionRead(this.user);
        this.start = Calendar.getInstance(GMTTimeZone).getTime().getTime();
        return p;
    }

    protected void eventDownloadFilePost(final File path, final long filelength) {
        // String key = "DOWNLOAD:" + user + ":" + path.toString();
        final long stop = Calendar.getInstance(GMTTimeZone).getTime().getTime();
        final String speed = (this.start == stop) ? ""
                : (" ("
                        + ((long) (filelength * 1000 / 1024 / (stop - this.start))) + " kbytes/second)");
        printlog(1, "DOWNLOAD \"" + path.toString() + "\", "
                + ((int) filelength / 1024) + " kbytes in "
                + (((int) ((stop - this.start) / 100)) / 10) + " seconds"
                + speed);
        aquireMacFSTC(path);
    }

    private void aquireMacFSTC(final File f) {
        try {
            // check if there is already information available
            if ((SystemUtils.macFSTypeCache == null)
                    || (SystemUtils.macFSTypeCache.size() == 0)) {
                final File mactypes = ServerAbstractSwitch
                        .propertiesFile("Ftpd.mactypes");
                if (mactypes.exists()) {
                    SystemUtils.macFSTypeCache = ServerAbstractSwitch
                            .loadHashtable(mactypes);
                }
            }

            if (SystemUtils.aquireMacFSCreator(f)) {
                // save the creator cache
                // System.out.println("DEBUG: saving MacFSCreator " +
                // SystemUtils.macFSCreatorCache.toString());
                printlog(1, "AQUIRED MAC FILE CREATOR OF \"" + f.toString()
                        + "\" AS \"" + SystemUtils.getMacFSCreator(f) + "\"");
            }

            if (SystemUtils.aquireMacFSType(f)) {
                // System.out.println("DEBUG: saving MacFSType " +
                // SystemUtils.macFSTypeCache.toString());
                try {
                    final File mactypes = ServerAbstractSwitch
                            .propertiesFile("Ftpd.mactypes");
                    ServerAbstractSwitch.saveHashtable(mactypes,
                            SystemUtils.macFSTypeCache,
                            "this file is automatically created");
                    printlog(1, "AQUIRED AND SAVED MAC FILE TYPE OF \""
                            + f.toString() + "\" AS \""
                            + SystemUtils.getMacFSType(f) + "\"");
                } catch (final IOException e) {
                }
            }
        } catch (final Exception e) {
            // if anything goes wrong here, we don't want the server to break up
            // with an error, since the file type feature is simply a smart
            // add-on,
            // and not essential. Beeing silent here will just be fine.
        }
    }

    // UPLOAD OF FILES
    protected boolean eventUploadFilePre(final File path) {
        final boolean p = FtpdPermissions.permissionWrite(this.user);
        this.start = Calendar.getInstance(GMTTimeZone).getTime().getTime();
        return p;
    }

    protected void eventUploadFilePost(final File path, final long filelength) {
        // String key = "UPLOAD:" + user + ":" + path.toString();
        final long stop = Calendar.getInstance(GMTTimeZone).getTime().getTime();
        final String speed = (this.start == stop) ? ""
                : (" ("
                        + ((long) (filelength * 1000 / 1024 / (stop - this.start))) + " kbytes/second)");
        printlog(1, "UPLOAD \"" + path.toString() + "\", "
                + ((int) filelength / 1024) + " kbytes in "
                + (((int) ((stop - this.start) / 100)) / 10) + " seconds"
                + speed);
        if (Ftpd.port < 1000) {
            changeaccess(path);
        }
        applyMacFSTC(path);
    }

    private void applyMacFSTC(final File f) {
        try {
            // check if there is already information available
            if ((SystemUtils.macFSTypeCache == null)
                    || (SystemUtils.macFSTypeCache.size() == 0)) {
                final File mactypes = ServerAbstractSwitch
                        .propertiesFile("Ftpd.mactypes");
                if (mactypes.exists()) {
                    SystemUtils.macFSTypeCache = ServerAbstractSwitch
                            .loadHashtable(mactypes);
                }
            }

            // apply creator change
            if (SystemUtils.applyMacFSCreator(f)) {
                printlog(1, "CHANGED MAC FILE CREATOR OF \"" + f.toString()
                        + "\" TO \"" + SystemUtils.getMacFSCreator(f) + "\"");
            }
            // apply type change
            if (SystemUtils.applyMacFSType(f)) {
                printlog(1, "CHANGED MAC FILE TYPE OF \"" + f.toString()
                        + "\" TO \"" + SystemUtils.getMacFSType(f) + "\"");
            }
        } catch (final Exception e) {
            // if anything goes wrong here, we don't want the server to break up
            // with an error, since the file type feature is simply a smart
            // add-on,
            // and not essential. Beeing silent here will just be fine.
        }
    }

    // RENAMING OF FILES
    protected boolean eventRenameFilePre(final File path) {
        return FtpdPermissions.permissionWrite(this.user);
    }

    protected void eventRenameFilePost(final File path) {
    }

    // EXECUTION OF SYSTEM COMMANDS
    // This is highly system-dependent
    protected void exec(final String s) {
        try {
            final Process proc = Runtime.getRuntime().exec(
                    s);
            try {
                proc.waitFor(); // wait until that process has terminated
                printlog(1, "EXEC: " + s + " = " + proc.exitValue());
            } catch (final InterruptedException e) {
                printlog(0, "IRREGULARITY: exec interrupted: " + e.getMessage());
            }
        } catch (final SecurityException e) {
            printlog(0, "IRREGULARITY: exec not permitted: " + e.getMessage());
        } catch (final Exception e) {
            printlog(0, "IRREGULARITY: exec not possible on this machine: "
                    + e.getMessage());
        }
    }

    // BEGINNING OF SESSIONS
    protected boolean eventBeginSession() {
        printlog(0, "session started");
        return true;
    }

    // ENDING A SESSION
    protected void eventEndSession() {
        printlog(0, "session terminated");
    }

    // helper functions
    protected void printlog(final int level, final String message) {
        // 0 - print connection statements and error messages only
        // 1 - print also download/upload information
        // 2 - print every command received/sent on telnet channel
        if (level <= Ftpd.loglevel) {
            Ftpd.log.println(ServerCore.logDate() + " "
                    + this.userAddress.getHostAddress() + slash
                    + ((this.user == null) ? "-" : this.user) + " " + message);
        }
    }

    // change access attributes of files in unix systems
    // this function is always called after the creation of new files or folders
    // on the host system
    private void changeaccess(final File file) {
        try {
            if (file.isDirectory()) {
                exec("chmod "
                        + this.switchboard.getConfig("unixfoldermask", "777")
                        + " " + file.toString());
            } else {
                exec("chmod "
                        + this.switchboard.getConfig("unixfilemask", "666")
                        + " " + file.toString());
            }
            exec("chown -f " + this.switchboard.getConfig("unixuser", "root")
                    + " " + file.toString());
            exec("chown -f :" + this.switchboard.getConfig("unixgroup", "root")
                    + " " + file.toString());
        } catch (final Exception e) {
            // this sometimes seems to throw a java.lang.InternalError
            // that may occur if the exec technology or commands are not
            // supported
            // in this case we are simply silent, because that is a strong
            // indication that no different user right are possible on the
            // server. It will be fine.
        }
    }

    // this defines all files that shall be treated as invisible
    // if a file is invisible, it will be blinked out from the result of a LIST
    // command
    // But there is another important effect: if an directory, that appears
    // empty from the
    // point of view of the client shall be deleted, but is not and contains
    // invisible files,
    // then it cannot be deleted. Since this is not obvious for the client, the
    // RMD command
    // deletes all invisible files in a folder if the folder shall be deleted
    // before deletion
    // of that folder.
    // If that effect shall be suppressed, it can only be done by denying to
    // delete folders.
    // This can be realized by modifiing the eventDeleteFolderPre method.
    protected static boolean invisibleFile(final String s) {
        // files that shall be invisible in an MacOS 9.x and MacOS X system
        return ((s.equals(".DS_Store")) || ((s.startsWith("Icon")) && (s
                .length() == 5)));
    }

    protected InputStream createIndex(final File ind) throws IOException {
        // AnomicFTPD can create index.html - files on-the-fly. The index files
        // will not really be created in the
        // host file system, but appear on the client side as they would be.
        // even if index.html files can virtually
        // accessed from any folder from th epoint of view of the client, they
        // do not appear in directory listings
        // since they do not exist there. If a 'real' index.html file exists, it
        // is accessed instead of this virtual
        // existing index.html
        if ((!ind.isAbsolute()) || (ind.exists())
                || (!ind.getName().equals("index.html")))
            throw new IOException("index path inappropriate");
        final String parent = ind.getParent();
        if (parent == null)
            throw new IOException("index creation internal error");
        final File inode = new File(parent);
        final String[] name = inode.list();
        // open the file and start to write
        final StringBuffer bb = new StringBuffer();
        String clientpath = this.userWD;
        if (clientpath.length() > 0) {
            clientpath = clientpath.substring(1); // cut '\' or '/' at beginning
        }
        if (clientpath.length() == 0) {
            clientpath = slash;
        } else {
            clientpath = slash + clientpath;
        }
        bb.append("<TITLE>DIRECTORY OF " + clientpath + "</TITLE>\r\n");
        bb.append("<!-- generated by AnomicFTPD -->\r\n");
        bb.append("<H2>CURRENT PATH IS " + clientpath + "</H2>\r\n");
        bb.append("<PRE>\r\n");
        File filename;
        String printname;
        String pathname;
        for (int n = 0; n < name.length; ++n) {
            filename = new File(inode, name[n]);
            if (filename.isDirectory()) {
                pathname = clientpath + name[n] + "/index.html";
                printname = name[n] + slash;
            } else {
                pathname = clientpath + name[n];
                printname = name[n] + " [" + filename.length() + " bytes]";
            }
            bb.append("<A HREF=\"" + pathname + "\">" + printname + "</A>\r\n");
        }
        bb.append("</PRE>");
        return new ByteArrayInputStream(bb.toString().getBytes());
    }

    // create directory listing
    protected String ls(final File path, final String prefix, final boolean full) {
        if (!path.isAbsolute()) return ""; // we accept only absolute paths here
        final boolean mayRead = FtpdPermissions.permissionRead(this.user);
        final boolean mayWrite = FtpdPermissions.permissionWrite(this.user);
        final String usergroup = FtpdPermissions.getGroup(this.user);
        final boolean countSubDir = this.switchboard.getConfig("countsubdir", "false").equals("true");
        final boolean root = path.toString().equals(FtpdPermissions.getRoot(this.user));
        if (path.isDirectory()) {
            final String[] element = path.list();
            final StringBuffer buf = new StringBuffer();
            File f;
            if (root && this.user.equals("anonymous")) buf.append(lsNotifier(new File(path, "_AnomicFTPD_free_software_anomic.de"), usergroup));
            for (int n = 0; n < element.length; ++n) {
                f = new File(path, element[n]);
                if ((!(invisibleFile(element[n]))) && (f.exists())) {
                    buf.append(prefix);
                    if (full) {
                        buf.append(lsFile(f, mayRead, mayWrite, usergroup,
                                countSubDir));
                    } else {
                        buf.append(element[n] + "\r\n");
                    }
                }
            }
            return buf.toString();
        } else
            return lsFile(path, mayRead, mayWrite, usergroup, countSubDir);
    }
    
    protected String lsNotifier(final File path, final String usergroup) {
        String buf = "-";
        String perm = "---";

        buf += perm + perm + perm + "   1";
        final String g = usergroup;
        buf += " " + ((g.length() > 7) ? g.substring(0, 8) : lenformatted(g, 8, false));
        buf += " " + ((g.length() > 7) ? g.substring(0, 8) : lenformatted(g, 8, false));
        buf += lenformatted(" 0", 11, true);
        return buf + " " + Ftpd.fsDate(new Date(path.lastModified())) + " " + encodeChar(path.getName()) + "\r\n";
    }
    
    protected String lsFile(final File path, final boolean mayRead,
            final boolean mayWrite, final String usergroup,
            final boolean countSubDir) {
        // full information
        // if (!path.exists()) return ""; // return "550 \"" + path.getPath() +
        // "\" no such file";
        String buf = "";
        if (path.isDirectory()) {
            buf += "d";
        } else if (path.isFile()) {
            buf += "-";
        } else {
            buf += "?";
        }
        String perm = "";
        if ((mayRead) && (path.canRead())) {
            perm += "r";
        } else {
            perm += "-";
        }
        boolean root = false;
        if ((mayWrite) && (path.canWrite())) {
            perm += "w";
            root = false;
        } else {
            perm += "-";
            root = true;
        }
        if (path.isDirectory()) {
            perm += "x";
        } else {
            perm += "-";
        }
        buf += perm + perm + perm + "   1";
        final String g = (root) ? "root" : usergroup;
        buf += " "
                + ((g.length() > 7) ? g.substring(0, 8) : lenformatted(g, 8,
                        false));
        buf += " "
                + ((g.length() > 7) ? g.substring(0, 8) : lenformatted(g, 8,
                        false));
        if (path.isDirectory()) {
            // counting the number of entries in subdirectory is the behaviur of
            // the unxi 'ls -l'
            // command that we cann adopt here.
            // in real-life applications we learned the the result is not used
            // and the calculation is
            // a great performance issue. We use therefore a switch to shut on
            // or off this functionality
            if (countSubDir) {
                final String[] l = path.list();
                buf += lenformatted(" " + ((l == null) ? 0 : l.length), 11,
                        true);
                if (l == null) {
                    printlog(3, "Contradictous directory property: "
                            + path.toString());
                }
            } else {
                buf += lenformatted(" " + 0, 11, true);
            }
        } else {
            buf += lenformatted(" " + path.length(), 11, true);
        }
        return buf + " " + Ftpd.fsDate(new Date(path.lastModified())) + " "
                + encodeChar(path.getName()) + "\r\n";
    }

    private String lenformatted(String s, int l, final boolean right) {
        l = l - s.length();
        if (right) {
            while (l > 0) {
                s = " " + s;
                l--;
            }
        } else {
            while (l > 0) {
                s = s + " ";
                l--;
            }
        }
        return s;
    }

    protected static String[] argument2args(final String argList) {
        // command line parser
        StringTokenizer tokens = new StringTokenizer(argList);
        final String[] args = new String[tokens.countTokens()];
        for (int i = 0; tokens.hasMoreTokens(); i++) {
            args[i] = tokens.nextToken();
        }
        tokens = null; // free mem
        return args;
    }

    protected static String car(final String argList) {
        if ((argList == null) || (argList.length() == 0))
            return null;
        final int pos = argList.indexOf(' ');
        if (pos >= 0)
            return argList.substring(0, pos);
        else
            return argList;
    }

    protected static String cdr(final String argList) {
        if ((argList == null) || (argList.length() == 0))
            return null;
        final int pos = argList.indexOf(' ');
        if (pos >= 0)
            return argList.substring(pos + 1);
        else
            return null;
    }

    protected static String[] shift(final String args[]) {
        if ((args == null) || (args.length == 0))
            return args;
        else {
            final String newArgs[] = new String[args.length - 1];
            System.arraycopy(args, 1, newArgs, 0, args.length - 1);
            return newArgs;
        }
    }

    protected String sitecommand(String command, final String args) {
        // 200, 202, 500, 501, 530
        command = command.toUpperCase();
        if (command.equals("HELP"))
            return ("200-Recognized SITE commands:\r\n"
                    + " LS or DIR                          -- directory listing through terminal session\r\n"
                    + " CHMOD uuu <path>                   -- change access right of <path>\r\n"
                    + " JAVA <class> *<args>               -- call of a java main class with args\r\n"
                    + " SHUTDOWN                           -- can be used to shut down a Mac running OS 8/9\r\n"
                    + " UTIME <filename> <access-time> <modify-time> <create-time> UTC;  <time>=yyyyMMddHHmmss\r\n"
                    + "200 " + ((FtpdPermissions.permissionExec(this.user)) ? "permission granted to use the JAVA/SHUTDOWN commands"
                    : "the user must have set the exec flag in Ftpd.groups to use the JAVA/SHUTDOWN commands"));
        else {
            if (command.equals("CHMOD")) {
                if (!(FtpdPermissions.permissionWrite(this.user))) return "501 no permission to write";
                String mode = car(args);
                final String path = cdr(args);
                if ((cdr(path) != null) || (mode.length() != 3))
                    return "501 SITE CHMOD command syntax error";
                final File file = serverFile(path);
                if (!(file.exists()))
                    return "501 SITE CHMOD argument error: " + appendUserFile(path) + " does not exist";

                // retrieve rwx information from the code
                final boolean shallRead = ((((byte) mode.charAt(0) & 4) != 0)
                        || (((byte) mode.charAt(1) & 4) != 0) || (((byte) mode
                        .charAt(2) & 4) != 0));
                final boolean shallWrite = ((((byte) mode.charAt(0) & 2) != 0)
                        || (((byte) mode.charAt(1) & 2) != 0) || (((byte) mode
                        .charAt(2) & 2) != 0));

                // reconstruct rwx string
                mode = "" + ((shallRead ? 4 : 0) + (shallWrite ? 2 : 0));
                mode = mode + mode + mode;

                // check if the command is superfluous
                if ((shallRead == file.canRead())
                        && (shallWrite == file.canWrite()))
                    // we don't need to change anything
                    return "200 CHMOD " + mode + " " + appendUserFile(path) + " superfluous";

                try { // a Java 1.2 function call inside
                    // the only chmod function that can be accessed form java
                    // without exec on system level
                    if ((!shallWrite) && (file.canWrite())) {
                        file.setReadOnly(); // one method to set read-only
                    }
                } catch (final NoSuchMethodError e) {
                    printlog(
                            0,
                            "IRREGULARITY: setReadOnly not supported (java version too low, you need Java-2)");
                } // silently ignore error

                // check again if an exec is necessary
                if ((shallRead == file.canRead())
                        && (shallWrite == file.canWrite()))
                    // we don't need to change anything
                    return "200 CHMOD " + mode + " " + appendUserFile(path) + " done by java function";

                // try to use a system call
                exec("chmod " + mode + " " + file.toString()); // a different
                                                                // method; can
                                                                // read/write
                                                                // also

                // finally check another time if the exec was successfull
                if ((shallRead == file.canRead())
                        && (shallWrite == file.canWrite()))
                    // we don't need to change anything
                    return "200 CHMOD " + mode + " " + appendUserFile(path) + " done by system exec";

                // no success
                return "501 CHMOD " + mode + " " + appendUserFile(path)
                        + " not successful (syntax ok, file op failed)";
            } else if (command.equals("UTIME")) {
                // replace with
                // "'SITE UTIME <filename> <access-time> <modify-time> <create-time> UTC'; <time>=yyyyMMddHHmmss"
                // example:
                // "SITE UTIME index.html 20031027122221 20031027103701 20031027103701 UTC"
                if (!(FtpdPermissions.permissionWrite(this.user)))
                    return "501 no permission to write";
                String[] argList = argument2args(args);
                String filename = argList[0];
                argList = shift(argList);
                while (argList.length > 4) {
                    filename = filename + " " + argList[0];
                    argList = shift(argList);
                }
                final File file = serverFile(filename);
                if (argList.length < 4)
                    return "501 missing parameter: SITE UTIME <file> <atime> <mtime> <ctime> UTC";
                if (!(file.exists()))
                    return "501 SITE UTIME argument error: " + appendUserFile(filename)
                            + " does not exist";
                // we take only the <modify-time> as input since we cannot
                // change atime and ctime
                // parse the date
                if (argList[1].length() != 14)
                    return "501 UTIME date/time parameter syntax error: use yyyyMMddHHmmss";
                long date;
                try {
                    date = MFMTFormatter.parse(argList[1]).getTime();
                } catch (final java.text.ParseException e) {
                    return ("501 UTIME date/time parameter syntax error: use yyyyMMddHHmmss");
                }
                // do the date change
                try { // a Java 1.2 function call inside
                    if (file.setLastModified(date))
                        return "200 UTIME <modify-date> = " + argList[1]
                                + " applied to " + appendUserFile(filename);
                    else
                        return "501 UTIME " + appendUserFile(filename) + " FAILED";
                } catch (final NoSuchMethodError e) {
                    printlog(
                            0,
                            "IRREGULARITY: setLastModified not supported (java version too low, you need Java-2)");
                    return "501 java 2 (jdk 1.2 - compliant) is necessary to perform this function";
                }
            } else if ((command.equals("LS")) | (command.equals("DIR"))) {
                if (!(FtpdPermissions.permissionRead(this.user)))
                    return "501 no permission to read";
                return "200-\r\n" + ls(serverPath(), " ", true) + "200";
            } else if (command.equals("SHUTDOWN")) {
                if (!(FtpdPermissions.permissionExec(this.user)))
                    return "530 no permission to exec for this user. set exec flag in Ftpd.groups";
                exec(/*
                     * FtpdSwitchboard.localPath1 +
                     * System.getProperty("file.separator") +
                     */"shutdown.os89script");
                return "200 shutdown initiated. future commands are void. goodby.";
            } else if (command.equals("JAVA")) {
                if (!(FtpdPermissions.permissionExec(this.user)))
                    return "530 no permission to exec for this user. set exec flag in Ftpd.groups";
                final String file = car(args);
                final String[] argList = argument2args(cdr(args));
                if (argList.length == 0)
                    return "501 not enough parameters for JAVA";
                try {
                    return javaexec(file, argList);
                } catch (final IOException e) {
                    return "501 class " + file + " not found";
                }
            } else if (command.equals("EXEC")) {
                if (!(FtpdPermissions.permissionExec(this.user)))
                    return "530 no permission to exec for this user. set exec flag in Ftpd.groups";
                return "501";
            } else
                return "501 SITE command parameter not implemented";
        }
    }

    public String javaexec(String javaclass, final String[] args)
            throws IOException {

        // change the path of the javaclass argument
        if (javaclass.endsWith(".class")) {
            javaclass = javaclass.substring(0, javaclass.length() - 6);
        }
        if (!(serverFile(javaclass + ".class").exists()))
            return "501 java class does not exist";

        // load the class
        try {
            final Class c = (new CachedClassLoader()).loadClass(serverFile(javaclass).toString());

            // locate public static main(String[]) method
            Class[] parameterType = new Class[1];
            parameterType[0] = Class.forName("[Ljava.lang.String;");
            Method m = c.getMethod("main", parameterType);

            // invoke object.main()
            final Object argList[] = new Object[1];
            argList[0] = args;
            final Object result = m.invoke(null, argList);
            parameterType = null;
            m = null;

            // handle result
            if (result != null)
                return "250 " + result;
            else
                return "250 " + javaclass + " executed";

        } catch (final ClassNotFoundException e) {
            // class javaclass does not exist, go silently over it to not show
            // everybody that the
            // system attempted to load a javaclass file
            return "550 java command '" + javaclass + "' not supported. Try 'HELP'.";
        } catch (final NoSuchMethodException e) {
            return "550 no \"public static main(String args[])\" in " + javaclass;
        } catch (final InvocationTargetException e) {
            final Throwable orig = e.getTargetException();
            return "550 Exception from " + javaclass + ": " + orig.getMessage();
        } catch (final IllegalAccessException e) {
            return "550 Illegal access for " + javaclass + ": class is probably not declared as public";
        } catch (final NullPointerException e) {
            return "550 main(String args[]) is not defined as static for " + javaclass;
        } catch (final Exception e) {
            return "550 Exception caught: " + e;
        }
    }

}
