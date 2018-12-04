package com.acmedcare.nas.exts.api.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * com.acmedcare.nas.exts.api.util
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-05.
 */
public class NameUtilsTest {

  @Test
  public void humpToLine() {
    String name = "access1K1ey";
    Assert.assertEquals("access1-k1ey", NameUtils.humpToLine(name));
  }
}
