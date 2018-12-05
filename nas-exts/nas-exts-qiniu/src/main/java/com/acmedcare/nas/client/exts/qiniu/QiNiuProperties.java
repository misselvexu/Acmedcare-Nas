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

  @PropertiesKey private String publicUrl;

  public String getPublicUrl() {
    return publicUrl;
  }

  public void setPublicUrl(String publicUrl) {
    this.publicUrl = publicUrl;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  @Override
  protected String getPropertiesPrefix() {
    return "qiniu";
  }

  @Override
  public String toString() {
    String sb =
        "QiNiuProperties{"
            + "accessKey='"
            + accessKey
            + '\''
            + ", secretKey='"
            + secretKey
            + '\''
            + ", bucketName='"
            + bucketName
            + '\''
            + ", publicUrl='"
            + publicUrl
            + '\''
            + '}';
    return sb;
  }
}
