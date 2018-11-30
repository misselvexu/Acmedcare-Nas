package com.acmedcare.nas.common.config;

import com.acmedcare.nas.common.kits.IOKits;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Conf File Reader
 *
 * <p>Support Comment With : '/' ,'#' ,';'
 *
 * <pre>
 *  Conf Example:
 *
 *  [key-a]
 *  value-a
 *
 *  [key-b]
 *  value-b
 *
 * </pre>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version v1.0 - 27/09/2018.
 */
public class ConfFileReader {

  /**
   * Read File Config From Source File
   *
   * @param source source file
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> read(File source) {

    Map<String, Object> configs = Maps.newLinkedHashMap();
    if (source != null && source.exists()) {
      try {
        List<String> lines = IOKits.readLines(new FileReader(source));
        Collection<String> result =
            Collections2.filter(
                lines,
                new Predicate<String>() {
                  @Override
                  public boolean apply(String input) {
                    if (StringUtils.isBlank(input)) {
                      return false;
                    }
                    return !StringUtils.startsWithAny(input.trim(), "/", "#", ";");
                  }
                });

        if (result != null && result.size() > 0) {
          String tempKey = null;
          int count = 0;
          for (String line : result) {
            if (StringUtils.startsWithAny(line.trim(), "[")) {
              tempKey = StringUtils.substringBetween(line.trim(), "[", "]");
              count = 0;
            } else {
              count += 1;
              // no key line
              if (count > 1) {
                // multi values
                ArrayList<Object> values = Lists.newArrayList();
                if (configs.containsKey(tempKey) && count == 2) {
                  values.add(configs.get(tempKey).toString());
                  configs.remove(tempKey);
                } else if (count > 2) {
                  values = (ArrayList<Object>) configs.get(tempKey);
                }

                values.add(line.trim());
                configs.put(tempKey, values);
              } else {
                configs.put(tempKey, line.trim());
              }
            }
          }
        }

      } catch (Exception ignored) {
      }
    }
    return configs;
  }
}
