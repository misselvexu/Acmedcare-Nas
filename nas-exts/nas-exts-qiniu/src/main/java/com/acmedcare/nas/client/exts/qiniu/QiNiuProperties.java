package com.acmedcare.nas.client.exts.qiniu;

import com.acmedcare.nas.exts.api.NasProperties;
import java.io.Serializable;

/**
 * QiNiu Properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
public class QiNiuProperties extends NasProperties implements Serializable {

  private static final long serialVersionUID = 4851324617914837957L;

  private String accessKey;

  private String secretKey;

  private String bucketName;

  public QiNiuProperties(String accessKey, String secretKey) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
  }

  public QiNiuProperties(String accessKey, String secretKey, String bucketName) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.bucketName = bucketName;
  }

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
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
}
