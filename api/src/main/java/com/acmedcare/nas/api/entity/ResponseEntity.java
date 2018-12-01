package com.acmedcare.nas.api.entity;

import com.acmedcare.nas.api.NasClientConstants.ResponseCode;

/**
 * Response Entity
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
public class ResponseEntity {

  /**
   * Execute Response Status Code
   *
   * <p>
   */
  private ResponseCode responseCode;

  private String message;

  public ResponseCode getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(ResponseCode responseCode) {
    this.responseCode = responseCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
