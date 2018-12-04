package com.acmedcare.nas.exts.api;

import com.acmedcare.nas.api.Extensible;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension Loader
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
public class ExtensionLoader<T> {

  /** 扩展点加载的路径 */
  public static final String EXTENSION_LOAD_PATH = "META-INF/services/nas-exts/";

  private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionLoader.class);
  /** 当前加载的接口类名 */
  protected final Class<T> interfaceClass;

  /** 接口名字 */
  protected final String interfaceName;
  /** 加载监听器 */
  protected final ExtensionLoaderListener<T> listener;
  /** 扩展点是否单例 */
  protected final Extensible extensible;

  /**
   * Default Construct
   *
   * @param clazz interface class
   * @param listener listener for loader
   */
  public ExtensionLoader(Class<T> clazz, ExtensionLoaderListener<T> listener) {
    this.interfaceClass = clazz;
    this.interfaceName = clazz.getName();
    this.listener = listener;

    Extensible extensible = interfaceClass.getAnnotation(Extensible.class);
    if (extensible == null) {
      throw new IllegalArgumentException(
          "Error when load extensible interface "
              + interfaceName
              + ", must add annotation @Extensible.");
    } else {
      this.extensible = extensible;
    }
  }

  /**
   * 得到当前ClassLoader，先找线程池的，找不到就找中间件所在的ClassLoader
   *
   * @return ClassLoader
   */
  public static ClassLoader getCurrentClassLoader() {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    if (cl == null) {
      cl = ExtensionLoader.class.getClassLoader();
    }
    return cl == null ? ClassLoader.getSystemClassLoader() : cl;
  }

  /**
   * 得到当前ClassLoader
   *
   * @param clazz 某个类
   * @return ClassLoader
   */
  public static ClassLoader getClassLoader(Class<?> clazz) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader != null) {
      return loader;
    }
    if (clazz != null) {
      loader = clazz.getClassLoader();
      if (loader != null) {
        return loader;
      }
    }
    return ClassLoader.getSystemClassLoader();
  }

  /** @param path path必须以/结尾 */
  protected synchronized void loadFromFile(String path) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Loading extension of extensible {} from path: {}", interfaceName, path);
    }
    // 默认如果不指定文件名字，就是接口名
    String file =
        (extensible.file() == null || extensible.file().trim().length() == 0)
            ? interfaceName
            : extensible.file().trim();
    String fullFileName = path + file;

    try {
      ClassLoader classLoader = getClassLoader(getClass());

      //      loadFromClassLoader(classLoader, fullFileName);
    } catch (Throwable t) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(
            "Failed to load extension of extensible "
                + interfaceName
                + " from path:"
                + fullFileName,
            t);
      }
    }
  }
}
