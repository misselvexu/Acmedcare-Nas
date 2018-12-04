package com.acmedcare.nas.exts.api.properties;

import static com.acmedcare.nas.exts.api.util.ClassLoaderUtils.getClassLoader;

import com.acmedcare.nas.exts.api.NasProperties;
import com.acmedcare.nas.exts.api.exception.NasContextException;
import com.acmedcare.nas.exts.api.util.PlatformDependent;
import java.io.InputStream;

/**
 * Abstract Nas Properties Loader
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-05.
 */
public abstract class AbstractNasPropertiesLoader<T> implements NasPropertiesLoader {

  private static final String JVM_DEFAULT_CONFIG_PATH = "META-INF/";
  private static final String ANDROID_DEFAULT_CONFIG_PATH = "/assets/";

  /**
   * Load properties from system config
   *
   * @return instance of {@link NasProperties}
   * @throws NasContextException exception
   */
  @Override
  public T loadProperties(ClassLoader classLoader) throws NasContextException {

    try {
      classLoader = classLoader == null ? getClassLoader(getClass()) : classLoader;
      String fileName = JVM_DEFAULT_CONFIG_PATH + propertyFileName();
      if (PlatformDependent.isAndroid()) {
        fileName = ANDROID_DEFAULT_CONFIG_PATH + propertyFileName();
      }
      InputStream inputStream =
          classLoader != null
              ? classLoader.getResourceAsStream(fileName)
              : ClassLoader.getSystemResourceAsStream(fileName);

      NasProperties properties = new NasProperties();
      properties.load(inputStream);
      return (T) properties;
    } catch (Exception e) {
      throw new NasContextException("Default load properties failed", e);
    }
  }
}
