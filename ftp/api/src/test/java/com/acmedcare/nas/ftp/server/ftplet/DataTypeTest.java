package com.acmedcare.nas.ftp.server.ftplet;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DataTypeTest extends TestCase {

  public void testParseA() {
    assertSame(DataType.ASCII, DataType.parseArgument('A'));
    assertSame(DataType.ASCII, DataType.parseArgument('a'));
  }

  public void testParseI() {
    assertSame(DataType.BINARY, DataType.parseArgument('I'));
    assertSame(DataType.BINARY, DataType.parseArgument('i'));
  }

  public void testParseUnknown() {
    try {
      DataType.parseArgument('x');
      fail("Exception must be thrown");
    } catch (IllegalArgumentException e) {
      // ignore
    }
  }

}
