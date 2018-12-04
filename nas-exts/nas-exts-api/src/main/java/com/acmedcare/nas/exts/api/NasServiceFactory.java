package com.acmedcare.nas.exts.api;

import com.acmedcare.nas.api.NasBucketService;
import com.acmedcare.nas.api.NasFileService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Nas File Service Factory
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-05.
 */
public final class NasServiceFactory {

  private static final Map<NasExts, NasFileService> NAS_FILE_SERVICE_MAP =
      new ConcurrentHashMap<>();

  private static final Map<NasExts, NasBucketService> NAS_BUCKET_SERVICE_MAP =
      new ConcurrentHashMap<>();

  /** 扩展加载器 */
  private static final ExtensionLoader<NasFileService> NAS_FILE_SERVICE_EXTENSION_LOADER =
      buildNasFileServiceLoader();

  private static final ExtensionLoader<NasBucketService> NAS_BUCKET_SERVICE_EXTENSION_LOADER =
      buildNasBucketServiceLoader();

  public static NasFileService getNasFileService(NasExts nasExts) {
    return NAS_FILE_SERVICE_EXTENSION_LOADER.getExtension(nasExts.name().toLowerCase());
  }

  public static NasBucketService getNasBucketService(NasExts nasExts) {
    return NAS_BUCKET_SERVICE_EXTENSION_LOADER.getExtension(nasExts.name().toLowerCase());
  }

  private static ExtensionLoader<NasBucketService> buildNasBucketServiceLoader() {
    return ExtensionLoaderFactory.getExtensionLoader(
        NasBucketService.class,
        new ExtensionLoaderListener<NasBucketService>() {
          @Override
          public void onLoad(ExtensionClass<NasBucketService> extensionClass) {
            NAS_BUCKET_SERVICE_MAP.put(
                NasExts.valueOf(extensionClass.getAlias().toUpperCase()),
                extensionClass.getExtInstance());
          }
        });
  }

  private static ExtensionLoader<NasFileService> buildNasFileServiceLoader() {
    return ExtensionLoaderFactory.getExtensionLoader(
        NasFileService.class,
        new ExtensionLoaderListener<NasFileService>() {
          @Override
          public void onLoad(ExtensionClass<NasFileService> extensionClass) {
            NAS_FILE_SERVICE_MAP.put(
                NasExts.valueOf(extensionClass.getAlias().toUpperCase()),
                extensionClass.getExtInstance());
          }
        });
  }
}
