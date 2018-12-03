package com.acmedcare.nas.api;

import com.acmedcare.nas.api.entity.ResponseEntity;
import com.acmedcare.nas.api.entity.UploadEntity;
import com.acmedcare.nas.api.exception.NasException;
import java.io.File;

/**
 * Nas File Service
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
@Extensible(singleton = false)
public interface NasFileService {

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
  UploadEntity upload(
      String fileName, String fileSuffix, File file, ProgressCallback uploadProgressCallback)
      throws NasException;

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
  UploadEntity upload(
      String fileName, String fileSuffix, String filePath, ProgressCallback uploadProgressCallback)
      throws NasException;

  /**
   * Download file from fs server
   *
   * @param fid file unique id
   * @param destFilePath dest storage file path
   * @return download result
   * @throws NasException exception
   */
  ResponseEntity download(String fid, String destFilePath) throws NasException;
}
