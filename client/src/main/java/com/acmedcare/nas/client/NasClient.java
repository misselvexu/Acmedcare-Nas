package com.acmedcare.nas.client;

import static com.acmedcare.nas.api.NasClientConstants.DEFAULT_CHARSET;
import static com.acmedcare.nas.api.NasClientConstants.NasRequest.UPLOAD;
import static com.acmedcare.nas.api.NasClientConstants.SEPARATOR;

import com.acmedcare.framework.kits.jre.http.HttpRequest;
import com.acmedcare.nas.api.NasBucketService;
import com.acmedcare.nas.api.NasClientConstants.ResponseCode;
import com.acmedcare.nas.api.NasFileService;
import com.acmedcare.nas.api.bean.Bucket;
import com.acmedcare.nas.api.bean.BucketAttribute;
import com.acmedcare.nas.api.entity.ResponseEntity;
import com.acmedcare.nas.api.entity.UploadEntity;
import com.acmedcare.nas.api.exception.NasException;
import com.acmedcare.nas.client.logger.NasLogger;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * Nas Client
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 25/11/2018.
 */
public class NasClient implements NasFileService, NasBucketService {

  private final List<String> serverAddrs;

  private final boolean https;

  public NasClient(List<String> serverAddrs) {
    this(serverAddrs, false);
  }

  public NasClient(List<String> serverAddrs, boolean https) {
    this.serverAddrs = serverAddrs;
    this.https = https;
    if (this.serverAddrs == null || this.serverAddrs.isEmpty()) {
      throw new NasException("Nas client init must config properties [serverAddrs] .");
    }
  }

  private String buildRequestUrl(String contextPath) {
    Random random = new Random();
    int index = random.nextInt(this.serverAddrs.size());
    String host = this.serverAddrs.get(index);
    if (host.endsWith(SEPARATOR)) {
      host = host.substring(0, host.length() - 1);
    }
    return (this.https ? "https://" : "http://") + host + contextPath;
  }

  private void buildDefaultRequestHeader(HttpRequest request) {

  }

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

  /**
   * Upload a new file
   *
   * @param file source file
   * @return upload entity instance of {@link UploadEntity}
   * @throws NasException exception
   */
  @Override
  public UploadEntity upload(String fileName, String fileSuffix, File file) throws NasException {
    if (file == null || !file.exists()) {
      throw new NasException("file instance is null or file is not exist.");
    }

    if (fileName == null || fileName.trim().length() == 0) {
      throw new NasException("fileName must not be null.");
    }

    if (fileSuffix == null || fileSuffix.trim().length() == 0) {
      throw new NasException("fileSuffix must not be null.");
    }

    UploadEntity uploadEntity = new UploadEntity();
    try {
      // build request
      URL url = new URL(buildRequestUrl(UPLOAD));
      HttpRequest request = new HttpRequest(url, HttpRequest.METHOD_POST);

      request.part("file", fileName, file);
      if (request.ok()) {
        String result = request.body(DEFAULT_CHARSET);
        NasLogger.logger().info("Upload file request ok, response is : {}", result);
        // parse result

        uploadEntity.setResponseCode(ResponseCode.UPLOAD_SUCCESS);

      } else {
        // failed
        int responseCode = request.code();
        NasLogger.logger().info("Upload file request failed, response code is : {}", responseCode);
        uploadEntity.setResponseCode(ResponseCode.UPLOAD_FAILED);
        uploadEntity.setMessage("Failed, HttpCode: " + responseCode);
      }
    } catch (Exception e) {
      throw new NasException("Upload file failed ", e);
    }

    return uploadEntity;
  }

  /**
   * Upload a new file
   *
   * @param filePath source file path
   * @return upload entity instance of {@link UploadEntity}
   * @throws NasException exception
   */
  @Override
  public UploadEntity upload(String fileName, String fileSuffix, String filePath)
      throws NasException {
    if (filePath == null || filePath.trim().length() == 0) {
      throw new NasException("filePath must not be null.");
    }

    if (fileName == null || fileName.trim().length() == 0) {
      throw new NasException("fileName must not be null.");
    }

    if (fileSuffix == null || fileSuffix.trim().length() == 0) {
      throw new NasException("fileSuffix must not be null.");
    }

    File file = new File(filePath);

    return upload(fileName, fileSuffix, file);
  }

  /**
   * Download file from fs server
   *
   * @param fid file unique id
   * @param destFilePath dest storage file path
   * @return download result
   * @throws NasException exception
   */
  @Override
  public ResponseEntity download(String fid, String destFilePath) throws NasException {
    return null;
  }
}
