/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.acmedcare.nas.ftp.server.message.impl;

import com.acmedcare.nas.ftp.server.FtpServerConfigurationException;
import com.acmedcare.nas.ftp.server.message.MessageResource;
import com.acmedcare.nas.ftp.server.message.MessageResourceFactory;
import com.acmedcare.nas.ftp.server.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <strong>Internal class, do not use directly.</strong>
 *
 * <p>Class to get FtpServer reply messages. This supports i18n. Basic message search path is:
 *
 * <p><strong><strong>Internal class, do not use directly.</strong></strong>
 *
 * <p>Custom Language Specific Messages -> Default Language Specific Messages -> Custom Common
 * Messages -> Default Common Messages -> null (not found)
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DefaultMessageResource implements MessageResource {

  private static final String RESOURCE_PATH = "com/acmedcare/nas/ftp/server/message/";
  private final Logger LOG = LoggerFactory.getLogger(DefaultMessageResource.class);
  private final List<String> languages;

  private final Map<String, PropertiesPair> messages;

  /** Internal constructor, do not use directly. Use {@link MessageResourceFactory} instead. */
  public DefaultMessageResource(List<String> languages, File customMessageDirectory) {
    if (languages != null) {
      this.languages = Collections.unmodifiableList(languages);
    } else {
      this.languages = null;
    }

    // populate different properties
    messages = new HashMap<String, PropertiesPair>();
    if (languages != null) {
      for (String language : languages) {
        PropertiesPair pair = createPropertiesPair(language, customMessageDirectory);
        messages.put(language, pair);
      }
    }
    PropertiesPair pair = createPropertiesPair(null, customMessageDirectory);
    messages.put(null, pair);
  }

  /** Create Properties pair object. It stores the default and the custom messages. */
  private PropertiesPair createPropertiesPair(String lang, File customMessageDirectory) {
    PropertiesPair pair = new PropertiesPair();

    // load default resource
    String defaultResourceName;
    if (lang == null) {
      defaultResourceName = RESOURCE_PATH + "FtpStatus.properties";
    } else {
      defaultResourceName = RESOURCE_PATH + "FtpStatus_" + lang + ".properties";
    }
    InputStream in = null;
    try {
      in = getClass().getClassLoader().getResourceAsStream(defaultResourceName);
      if (in != null) {
        try {
          pair.defaultProperties.load(in);
        } catch (IOException e) {
          throw new FtpServerConfigurationException(
              "Failed to load messages from \""
                  + defaultResourceName
                  + "\", file not found in classpath");
        }
      } else {
        throw new FtpServerConfigurationException(
            "Failed to load messages from \""
                + defaultResourceName
                + "\", file not found in classpath");
      }
    } finally {
      IoUtils.close(in);
    }

    // load custom resource
    File resourceFile = null;
    if (lang == null) {
      resourceFile = new File(customMessageDirectory, "FtpStatus.gen");
    } else {
      resourceFile = new File(customMessageDirectory, "FtpStatus_" + lang + ".gen");
    }
    in = null;
    try {
      if (resourceFile.exists()) {
        in = new FileInputStream(resourceFile);
        pair.customProperties.load(in);
      }
    } catch (Exception ex) {
      LOG.warn("MessageResourceImpl.createPropertiesPair()", ex);
      throw new FtpServerConfigurationException("MessageResourceImpl.createPropertiesPair()", ex);
    } finally {
      IoUtils.close(in);
    }

    return pair;
  }

  /** Get all the available languages. */
  @Override
  public List<String> getAvailableLanguages() {
    if (languages == null) {
      return null;
    } else {
      return Collections.unmodifiableList(languages);
    }
  }

  /** Get the message. If the message not found, it will return null. */
  @Override
  public String getMessage(int code, String subId, String language) {
    // find the message key
    String key = String.valueOf(code);
    if (subId != null) {
      key = key + '.' + subId;
    }

    // get language specific value
    String value = null;
    PropertiesPair pair = null;
    if (language != null) {
      language = language.toLowerCase();
      pair = messages.get(language);
      if (pair != null) {
        value = pair.customProperties.getProperty(key);
        if (value == null) {
          value = pair.defaultProperties.getProperty(key);
        }
      }
    }

    // if not available get the default value
    if (value == null) {
      pair = messages.get(null);
      if (pair != null) {
        value = pair.customProperties.getProperty(key);
        if (value == null) {
          value = pair.defaultProperties.getProperty(key);
        }
      }
    }

    return value;
  }

  /** Get all messages. */
  @Override
  public Map<String, String> getMessages(String language) {
    Properties messages = new Properties();

    // load properties sequentially
    // (default,custom,default language,custom language)
    PropertiesPair pair = this.messages.get(null);
    if (pair != null) {
      messages.putAll(pair.defaultProperties);
      messages.putAll(pair.customProperties);
    }
    if (language != null) {
      language = language.toLowerCase();
      pair = this.messages.get(language);
      if (pair != null) {
        messages.putAll(pair.defaultProperties);
        messages.putAll(pair.customProperties);
      }
    }

    Map<String, String> result = new HashMap<String, String>();
    for (Object key : messages.keySet()) {
      result.put(key.toString(), messages.getProperty(key.toString()));
    }

    return Collections.unmodifiableMap(result);
  }

  /** Dispose component - clear all maps. */
  public void dispose() {
    Iterator<String> it = messages.keySet().iterator();
    while (it.hasNext()) {
      String language = it.next();
      PropertiesPair pair = messages.get(language);
      pair.customProperties.clear();
      pair.defaultProperties.clear();
    }
    messages.clear();
  }

  private static class PropertiesPair {
    public Properties defaultProperties = new Properties();

    public Properties customProperties = new Properties();
  }
}
