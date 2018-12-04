package com.acmedcare.nas.client.exts.qiniu;

import com.acmedcare.nas.exts.api.properties.AutoConfigureProperties;
import com.acmedcare.nas.exts.api.properties.PropertiesKey;
import java.io.Serializable;

/**
 * QiNiu Properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
public class QiNiuProperties extends AutoConfigureProperties implements Serializable {

  private static final long serialVersionUID = 4851324617914837957L;

  @PropertiesKey private String accessKey;

  @PropertiesKey private String secretKey;

  @PropertiesKey private String bucketName;

  public String getAccessKey() {
    return accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public String getBucketName() {
    return bucketName;
  }

  @Override
  protected String getPropertiesPrefix() {
    return "qiniu";
  }
}
