package com.acmedcare.nas.exts.api;

import com.acmedcare.nas.api.NasBucketService;
import com.acmedcare.nas.api.NasFileService;
import com.acmedcare.nas.exts.api.properties.NasPropertiesLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Nas Service Factory
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-05.
 */
public final class NasServiceFactory {

  private static final Map<NasExtType, NasFileService> NAS_FILE_SERVICE_MAP =
      new ConcurrentHashMap<>();

  private static final Map<NasExtType, NasBucketService> NAS_BUCKET_SERVICE_MAP =
      new ConcurrentHashMap<>();

  private static final Map<NasExtType, NasPropertiesLoader> NAS_PROPERTIES_MAP =
      new ConcurrentHashMap<>();

  /** 扩展加载器 */
  private static final ExtensionLoader<NasFileService> NAS_FILE_SERVICE_EXTENSION_LOADER =
      buildNasFileServiceLoader();

  private static final ExtensionLoader<NasBucketService> NAS_BUCKET_SERVICE_EXTENSION_LOADER =
      buildNasBucketServiceLoader();

  private static final ExtensionLoader<NasPropertiesLoader> PROPERTIES_LOADER_EXTENSION_LOADER =
      buildNasPropertiesLoader();

  private static ExtensionLoader<NasPropertiesLoader> buildNasPropertiesLoader() {
    return ExtensionLoaderFactory.getExtensionLoader(
        NasPropertiesLoader.class,
        new ExtensionLoaderListener<NasPropertiesLoader>() {
          @Override
          public void onLoad(ExtensionClass<NasPropertiesLoader> extensionClass) {
            NAS_PROPERTIES_MAP.put(
                NasExtType.valueOf(extensionClass.getAlias().toUpperCase()),
                extensionClass.getExtInstance());
          }
        });
  }

  public static NasFileService getNasFileService(NasExtType nasExtType) {
    return NAS_FILE_SERVICE_EXTENSION_LOADER.getExtension(nasExtType.name().toLowerCase());
  }

  public static NasBucketService getNasBucketService(NasExtType nasExtType) {
    return NAS_BUCKET_SERVICE_EXTENSION_LOADER.getExtension(nasExtType.name().toLowerCase());
  }

  public static NasPropertiesLoader getNasPropertiesLoader(NasExtType nasExtType) {
    return PROPERTIES_LOADER_EXTENSION_LOADER.getExtension(nasExtType.name().toLowerCase());
  }

  private static ExtensionLoader<NasBucketService> buildNasBucketServiceLoader() {
    return ExtensionLoaderFactory.getExtensionLoader(
        NasBucketService.class,
        new ExtensionLoaderListener<NasBucketService>() {
          @Override
          public void onLoad(ExtensionClass<NasBucketService> extensionClass) {
            NAS_BUCKET_SERVICE_MAP.put(
                NasExtType.valueOf(extensionClass.getAlias().toUpperCase()),
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
                NasExtType.valueOf(extensionClass.getAlias().toUpperCase()),
                extensionClass.getExtInstance());
          }
        });
  }
}
