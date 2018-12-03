package com.acmedcare.nas.exts.api;

/**
 * Extension Class
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
public class ExtensionClass<T> {

  /** 扩展接口实现类名 */
  protected final Class<? extends T> clazz;

  public ExtensionClass(Class<? extends T> clazz) {
    this.clazz = clazz;
  }
}
