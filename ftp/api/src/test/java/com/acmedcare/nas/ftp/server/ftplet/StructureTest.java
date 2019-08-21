package com.acmedcare.nas.ftp.server.ftplet;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class StructureTest extends TestCase {
  public void testParseF() {
    assertSame(Structure.FILE, Structure.parseArgument('F'));
    assertSame(Structure.FILE, Structure.parseArgument('f'));
  }

  public void testParseUnknown() {
    try {
      Structure.parseArgument('x');
      fail("Exception must be thrown");
    } catch (IllegalArgumentException e) {
      // ignore
    }
  }
}
