package com.acmedcare.nas.api;

import com.acmedcare.nas.api.bean.Bucket;
import com.acmedcare.nas.api.bean.BucketAttribute;

/**
 * Nas Service
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 25/11/2018.
 */
public interface NasBucketService {

  /**
   * Create new bucket storage instance
   *
   * @param name bucket name
   * @param attribute attribute
   * @return a instance of {@link Bucket}
   * @see BucketAttribute
   */
  Bucket createNewBucket(String name, BucketAttribute attribute);
}
