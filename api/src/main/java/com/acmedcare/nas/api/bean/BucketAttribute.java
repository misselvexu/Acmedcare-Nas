package com.acmedcare.nas.api.bean;

import java.io.Serializable;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Bucket Attribute
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 29/11/2018.
 */
@Getter
@Setter
@NoArgsConstructor
@Builder
public class BucketAttribute implements Serializable {

  private static final long serialVersionUID = 7938096477226091930L;

  /**
   * bucket privately flag
   *
   * <p>Default: true
   */
  @Default private boolean privately = true;
}
