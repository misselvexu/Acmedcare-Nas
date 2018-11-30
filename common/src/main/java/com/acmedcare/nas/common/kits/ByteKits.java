package com.acmedcare.nas.common.kits;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ByteKits
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version v1.0 - 29/08/2018.
 */
public final class ByteKits {

  /**
   * InputStream to byte array
   *
   * @param inStream input stream
   * @return byte array
   * @throws IOException exception
   */
  public static byte[] input2byte(InputStream inStream) throws IOException {

    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
    byte[] buff = new byte[100];
    int rc;
    while ((rc = inStream.read(buff, 0, 100)) > 0) {
      swapStream.write(buff, 0, rc);
    }
    return swapStream.toByteArray();
  }
}
