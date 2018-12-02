package com.acmedcare.nas.api.entity;

import java.io.Serializable;

/**
 * File Entity
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-02.
 */
public class FileEntity implements Serializable {

  private static final long serialVersionUID = -7916894866412782688L;

  private String fid;

  private String fileName;

  private long size;

  private String fileUrl;

  public FileEntity(String fid, String fileName, long size, String fileUrl) {
    this.fid = fid;
    this.fileName = fileName;
    this.size = size;
    this.fileUrl = fileUrl;
  }
}
