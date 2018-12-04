package com.acmedcare.nas.client.test;

import com.acmedcare.nas.api.NasFileService;
import com.acmedcare.nas.exts.api.NasExts;
import com.acmedcare.nas.exts.api.NasServiceFactory;

/**
 * Nas Exts Test Application
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
public class NasExtsTestApplication {

  public static void main(String[] args) {

    NasFileService nasFileService = NasServiceFactory.getNasFileService(NasExts.QINIU);
    System.out.println(nasFileService);
  }
}
