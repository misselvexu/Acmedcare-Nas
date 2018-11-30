package com.acmedcare.nas.api.bean;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Nas System Access Config
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 29/11/2018.
 */
@Getter
@Setter
@NoArgsConstructor
public class NasConfig implements Serializable {

  private static final long serialVersionUID = 8024908883612512147L;

  /** Access Key */
  private String accessKey;

  /** Secret Key */
  private String secretKey;

  @Builder
  public NasConfig(String accessKey, String secretKey) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
  }
}
