package com.acmedcare.nas.api;

import com.acmedcare.nas.api.bean.Bucket;
import com.acmedcare.nas.api.bean.BucketAttribute;
import com.acmedcare.nas.api.entity.ResponseEntity;
import com.acmedcare.nas.api.exception.NasException;

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
   * @param bucketId bucket id
   * @param bucketName bucket name
   * @param attribute attribute
   * @return a instance of {@link Bucket}
   * @throws NasException exception
   * @see BucketAttribute
   */
  Bucket createNewBucket(String bucketId, String bucketName, BucketAttribute attribute)
      throws NasException;

  /**
   * Delete bucket by bucketId
   *
   * @param bucketId bucket id
   * @return execute result
   * @throws NasException exception
   */
  ResponseEntity deleteBucket(String bucketId) throws NasException;
}
