package com.acmedcare.nas.exts.api;

import com.acmedcare.nas.exts.api.exception.NasContextException;
import com.acmedcare.nas.exts.api.properties.AutoConfigureProperties;
import com.acmedcare.nas.exts.api.properties.PropertiesKey;
import com.acmedcare.nas.exts.api.util.NameUtils;
import com.acmedcare.nas.exts.api.util.ReflectionUtils;
import com.acmedcare.nas.exts.api.util.StringUtils;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Nas Base Properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
public class NasProperties extends Properties implements Serializable {

  private static final long serialVersionUID = 584891296601423292L;
  private static final Logger LOGGER = LoggerFactory.getLogger(NasProperties.class);

  private static final String PROPERTIES_SEPARATOR = ".";

  /**
   * Decode Properties Bean
   *
   * @param aClass target class type
   * @param <T> class type
   * @return instance of assigned bean
   */
  public <T> T decodePropertiesBean(Class<? extends AutoConfigureProperties> aClass)
      throws NasContextException {

    try {

      // new instance
      Object o = aClass.newInstance();

      Field[] fields = aClass.getDeclaredFields();
      if (fields != null && fields.length > 0) {
        String propertyKeyPrefix =
            String.valueOf(
                ReflectionUtils.invokeMethodByName(o, "getPropertiesPrefix", new Object[] {}));

        for (Field field : fields) {
          if (Modifier.isFinal(field.getModifiers())) {
            continue;
          }
          if (field.isAnnotationPresent(PropertiesKey.class)) {
            PropertiesKey propertiesKey = field.getAnnotation(PropertiesKey.class);
            String propertyKeyName = NameUtils.humpToLine(field.getName());
            if (StringUtils.hasText(propertiesKey.value())) {
              propertyKeyName = NameUtils.humpToLine(propertiesKey.value());
            }

            String fullKeyName = propertyKeyPrefix + PROPERTIES_SEPARATOR + propertyKeyName;
            if (containsKey(fullKeyName)) {
              String value = getProperty(fullKeyName);
              LOGGER.info(" Found properties {} -> {} ", fullKeyName, value);
              ReflectionUtils.invokeSetter(o, field.getName(), value);
            } else {
              LOGGER.warn(
                  "Class: {} field : {} -> {} , but can't find mapping in properties instance, @see annotation [PropertiesKey] defined",
                  aClass.getName(),
                  field.getName(),
                  fullKeyName);
            }
          }
        }
      }
      // return instance
      return (T) o;
    } catch (Exception e) {
      LOGGER.error("Decode properties config bean failed ", e);
      throw new NasContextException("Decode properties config bean failed ", e);
    }
  }
}
