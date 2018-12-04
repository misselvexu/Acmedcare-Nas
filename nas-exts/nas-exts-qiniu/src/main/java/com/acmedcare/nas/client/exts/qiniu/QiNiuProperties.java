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

  public String getAccessKey() {
    return accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public String getBucketName() {
    return bucketName;
  }
}
