package com.acmedcare.nas.exts.api;

/**
 * Extension Loader
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
public class ExtensionLoader<T> {

  /** 扩展点加载的路径 */
  public static final String EXTENSION_LOAD_PATH = "META-INF/services/nas-exts/";

  /** 当前加载的接口类名 */
  protected final Class<T> interfaceClass;

  /** 接口名字 */
  protected final String interfaceName;

  /** 加载监听器 */
  protected final ExtensionLoaderListener<T> listener;

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
  }
}
