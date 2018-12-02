package com.acmedcare.nas.api.entity;

/**
 * Upload Entity Instance
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
public class UploadEntity extends ResponseEntity {

  /** File System Storage Unique Id */
  private String fid;

  /** File Public Access Url */
  private String publicUrl;

  public String getFid() {
    return fid;
  }

  public void setFid(String fid) {
    this.fid = fid;
  }

  public String getPublicUrl() {
    return publicUrl;
  }

  public void setPublicUrl(String publicUrl) {
    this.publicUrl = publicUrl;
  }
}
