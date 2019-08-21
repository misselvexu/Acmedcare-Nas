package com.acmedcare.nas.ftp.server;

import com.acmedcare.nas.ftp.server.util.IoUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides the version of this release of FtpServer
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class Version {

  /**
   * Get the version of this FtpServer
   *
   * @return The current version
   */
  public static String getVersion() {
    Properties props = new Properties();
    InputStream in = null;

    try {
      in =
          Version.class
              .getClassLoader()
              .getResourceAsStream("com/acmedcare/nas/ftp/server/ftpserver.properties");
      props.load(in);
      return props.getProperty("ftpserver.version");
    } catch (IOException e) {
      throw new RuntimeException("Failed to read version", e);
    } finally {
      IoUtils.close(in);
    }
  }
}
