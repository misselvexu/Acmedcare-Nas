package com.acmedcare.nas.client.exts.qiniu;

import static com.acmedcare.nas.api.NasClientConstants.DEFAULT_CHARSET;

import com.acmedcare.framework.kits.jre.http.HttpRequest;
import com.acmedcare.nas.api.Extension;
import com.acmedcare.nas.api.NasClientConstants.ResponseCode;
import com.acmedcare.nas.api.NasFileService;
import com.acmedcare.nas.api.ProgressCallback;
import com.acmedcare.nas.api.entity.ResponseEntity;
import com.acmedcare.nas.api.entity.UploadEntity;
import com.acmedcare.nas.api.exception.NasException;
import com.acmedcare.nas.exts.api.NasExtType;
import com.acmedcare.nas.exts.api.NasProperties;
import com.acmedcare.nas.exts.api.NasServiceFactory;
import com.acmedcare.nas.exts.api.properties.NasPropertiesLoader;
import com.acmedcare.nas.exts.api.util.StringUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QiNiu Nas Client
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
@Extension("qiniu")
public class QiNiuNasFileClient implements NasFileService {

  private static final Logger LOGGER = LoggerFactory.getLogger(QiNiuNasFileClient.class);
  private QiNiuProperties qiNiuProperties;
  private Configuration configuration;
  private UploadManager uploadManager;
  private Auth auth;

  private QiNiuProperties nasProperties() {
    if (qiNiuProperties == null) {
      NasPropertiesLoader nasPropertiesLoader =
          NasServiceFactory.getNasPropertiesLoader(NasExtType.QINIU);
      NasProperties nasProperties = (NasProperties) nasPropertiesLoader.loadProperties(null);
      qiNiuProperties = nasProperties.decodePropertiesBean(QiNiuProperties.class);
    }
    return qiNiuProperties;
  }

  private UploadManager uploadManager() {
    if (configuration == null) {
      configuration = new Configuration();
    }

    if (uploadManager == null) {
      uploadManager = new UploadManager(configuration);
    }

    if (auth == null) {
      auth = Auth.create(nasProperties().getAccessKey(), nasProperties().getSecretKey());
    }

    return uploadManager;
  }

  private String uploadToken() {
    // check
    uploadManager();
    return auth.uploadToken(nasProperties().getBucketName());
  }

  /**
   * Upload a new file
   *
   * @param fileName file name
   * @param fileSuffix file suffix
   * @param file source file
   * @param uploadProgressCallback upload progress callback
   * @return upload entity instance of {@link UploadEntity}
   * @throws NasException exception
   */
  @Override
  public UploadEntity upload(
      String fileName, String fileSuffix, File file, ProgressCallback uploadProgressCallback)
      throws NasException {

    String key = fileName + "." + fileSuffix;
    try {
      Response response = uploadManager().put(file, key, uploadToken());
      String responseBody = response.bodyString();
      DefaultPutRet putRet = new Gson().fromJson(responseBody, DefaultPutRet.class);
      UploadEntity uploadEntity = new UploadEntity();

      // parse result
      if (putRet == null) {
        throw new NasException("[QiNiu] upload response is null ");
      } else {
        uploadEntity.setResponseCode(ResponseCode.UPLOAD_OK);
        uploadEntity.setFid(key);
        uploadEntity.setPublicUrl(nasProperties().getPublicUrl() + "/" + key);
      }

      return uploadEntity;

    } catch (NasException e) {
      throw e;
    } catch (Exception e) {
      if (e instanceof QiniuException) {
        QiniuException exception = (QiniuException) e;
        throw new NasException(
            "[QiNiu] upload failed , Cause by : " + exception.error() + " , Key : " + key);
      }
      throw new NasException("[QiNiu] upload failed", e);
    }
  }

  /**
   * Upload a new file
   *
   * @param fileName file name
   * @param fileSuffix file suffix
   * @param filePath source file path
   * @param uploadProgressCallback upload progress callback
   * @return upload entity instance of {@link UploadEntity}
   * @throws NasException exception
   */
  @Override
  public UploadEntity upload(
      String fileName, String fileSuffix, String filePath, ProgressCallback uploadProgressCallback)
      throws NasException {

    if (!StringUtils.hasText(filePath)) {
      throw new NasException("[QiNiu] filepath must be null or empty.");
    }

    return upload(fileName, fileSuffix, new File(filePath), uploadProgressCallback);
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
      URL url = new URL(nasProperties().getPublicUrl() + "/" + fid);
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
        LOGGER.info(
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
}
