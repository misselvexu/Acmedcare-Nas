package com.acmedcare.nas.client.exts.qiniu;

import com.acmedcare.nas.api.NasBucketService;
import com.acmedcare.nas.api.bean.Bucket;
import com.acmedcare.nas.api.bean.BucketAttribute;
import com.acmedcare.nas.api.entity.ResponseEntity;
import com.acmedcare.nas.api.exception.NasException;

/**
 * QiNiu Nas Bucket Client
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
public class QiNiuNasBucketClient implements NasBucketService {

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
  @Override
  public Bucket createNewBucket(String bucketId, String bucketName, BucketAttribute attribute)
      throws NasException {
    return null;
  }

  /**
   * Delete bucket by bucketId
   *
   * @param bucketId bucket id
   * @return execute result
   * @throws NasException exception
   */
  @Override
  public ResponseEntity deleteBucket(String bucketId) throws NasException {
    return null;
  }
}
