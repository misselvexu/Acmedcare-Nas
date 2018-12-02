package com.acmedcare.nas.client;

import static com.acmedcare.nas.api.NasClientConstants.DEFAULT_CHARSET;
import static com.acmedcare.nas.api.NasClientConstants.NasRequest.DOWNLOAD;
import static com.acmedcare.nas.api.NasClientConstants.NasRequest.UPLOAD;
import static com.acmedcare.nas.api.NasClientConstants.SEPARATOR;

import com.acmedcare.framework.kits.jre.http.HttpRequest;
import com.acmedcare.framework.kits.jre.http.HttpRequest.UploadProgress;
import com.acmedcare.nas.api.NasBucketService;
import com.acmedcare.nas.api.NasClientConstants.AuthHeader;
import com.acmedcare.nas.api.NasClientConstants.ResponseCode;
import com.acmedcare.nas.api.NasFileService;
import com.acmedcare.nas.api.ProgressCallback;
import com.acmedcare.nas.api.bean.Bucket;
import com.acmedcare.nas.api.bean.BucketAttribute;
import com.acmedcare.nas.api.entity.ResponseEntity;
import com.acmedcare.nas.api.entity.UploadEntity;
import com.acmedcare.nas.api.exception.NasException;
import com.acmedcare.nas.client.logger.NasLogger;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
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

  private String appId;
  private String appKey;

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
    if (request != null) {
      request.header(AuthHeader.NAS_APP_ID, this.appId);
      request.header(AuthHeader.NAS_APP_KEY, this.appKey);
    }
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
  public UploadEntity upload(
      String fileName, String fileSuffix, File file, final ProgressCallback uploadProgressCallback)
      throws NasException {
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

      buildDefaultRequestHeader(request);

      request.part("file", fileName + "." + fileSuffix, file);

      if (uploadProgressCallback != null) {
        request.progress(
            new UploadProgress() {
              @Override
              public void onUpload(long uploaded, long total) {
                uploadProgressCallback.onProgress(uploaded, total);
              }
            });
      }

      if (request.ok() || request.created()) {
        String result = request.body(DEFAULT_CHARSET);
        NasLogger.logger().info("Upload file request ok, response is : {}", result);
        // parse result
        if (result != null && result.length() > 0) {
          try {
            JSONObject jsonObject = JSONObject.parseObject(result);
            int code = jsonObject.getInteger("code");
            if (code == 0) {
              // parse data
              JSONObject data = jsonObject.getJSONObject("data");
              if (data != null) {
                uploadEntity.setFid(data.getString("fid"));
                uploadEntity.setPublicUrl(data.getString("fileUrl"));
              } else {
                throw new NasException("Upload file request biz data is null or ''");
              }
            } else {
              throw new NasException("Upload file request biz failed, code = " + code);
            }
          } catch (Exception e) {
            throw new NasException("Upload file response is not invalid json.");
          }
        } else {
          throw new NasException("Upload file request response must not be null.");
        }

        uploadEntity.setResponseCode(ResponseCode.UPLOAD_OK);

      } else {
        // failed
        int responseCode = request.code();
        String message = request.body(DEFAULT_CHARSET);
        NasLogger.logger()
            .info(
                "Upload file request failed, response code is : {} ,reason: {}",
                responseCode,
                message);
        uploadEntity.setResponseCode(ResponseCode.UPLOAD_FAILED);
        uploadEntity.setMessage(message);
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
  public UploadEntity upload(
      String fileName, String fileSuffix, String filePath, ProgressCallback uploadProgressCallback)
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

    return upload(fileName, fileSuffix, file, uploadProgressCallback);
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

    if (fid == null || fid.trim().length() == 0) {
      throw new NasException("Invalid param fid.");
    }

    if (destFilePath == null || destFilePath.trim().length() == 0) {
      throw new NasException("Invalid param destFilePath.");
    }

    ResponseEntity responseEntity = new ResponseEntity();
    try {
      // build request
      URL url = new URL(String.format(buildRequestUrl(DOWNLOAD), fid));
      HttpRequest request = new HttpRequest(url, HttpRequest.METHOD_GET);

      if (request.ok()) {
        byte[] content = request.bytes();
        if (content != null && content.length > 0) {
          FileOutputStream fos = new FileOutputStream(destFilePath);
          fos.write(content);
          fos.close();

          // check dest file
          if (new File(destFilePath).exists()) {
            responseEntity.setResponseCode(ResponseCode.DOWNLOAD_OK);
          }
        } else {
          responseEntity.setResponseCode(ResponseCode.DOWNLOAD_FAILED);
          responseEntity.setMessage("Download request response content is null.");
        }
      } else {
        int responseCode = request.code();
        String message = request.body(DEFAULT_CHARSET);
        NasLogger.logger()
            .info(
                "Download file request failed, response code is : {} ,reason: {}",
                responseCode,
                message);
        responseEntity.setResponseCode(ResponseCode.DOWNLOAD_FAILED);
        responseEntity.setMessage(message);
      }

    } catch (Exception e) {
      throw new NasException("Upload file failed ", e);
    }

    return responseEntity;
  }

  void registerAccessKey(String appId, String appKey) {
    this.appId = appId;
    this.appKey = appKey;
  }
}
