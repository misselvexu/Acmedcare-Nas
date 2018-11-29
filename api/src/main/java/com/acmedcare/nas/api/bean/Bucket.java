package com.acmedcare.nas.api.bean;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * File Bucket
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 29/11/2018.
 */
@Getter
@Setter
public class Bucket implements Serializable {

  private static final long serialVersionUID = 3098294977233678888L;

  /** File Bucket Id */
  private String bucketId;

  /** File Bucket Name */
  private String bucketName;


}
