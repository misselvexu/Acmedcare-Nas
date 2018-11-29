package com.acmedcare.nas.api.bean;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Nas System Access
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 29/11/2018.
 */
@Getter
@Setter
@NoArgsConstructor
public class NasAccess implements Serializable {

  private static final long serialVersionUID = 8024908883612512147L;

  /** Access Key */
  private String accessKey;

  /** Access Secret */
  private String accessSecret;

  @Builder
  public NasAccess(String accessKey, String accessSecret) {
    this.accessKey = accessKey;
    this.accessSecret = accessSecret;
  }
}
