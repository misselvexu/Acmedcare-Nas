package com.acmedcare.nas.client.exts.qiniu;

import com.acmedcare.nas.api.Extension;
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
import java.io.File;

/**
 * QiNiu Nas Client
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
@Extension("qiniu")
public class QiNiuNasFileClient implements NasFileService {

  private QiNiuProperties qiNiuProperties;

  private QiNiuProperties nasProperties() {
    if (qiNiuProperties == null) {
      NasPropertiesLoader nasPropertiesLoader =
          NasServiceFactory.getNasPropertiesLoader(NasExtType.QINIU);
      NasProperties nasProperties = (NasProperties) nasPropertiesLoader.loadProperties(null);
      qiNiuProperties = nasProperties.decodePropertiesBean(QiNiuProperties.class);
    }
    return qiNiuProperties;
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

    // TODO UPLOAD FILE

    return null;
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
      throw new NasException("filepath must be null or empty.");
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
    return null;
  }
}
