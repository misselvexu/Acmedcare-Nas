package com.acmedcare.nas.api;

/**
 * Nas Client Constants
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-01.
 */
public class NasClientConstants {

  public static final String DEFAULT_CHARSET = "UTF-8";

  public static final String SEPARATOR = "/";

  /**
   * Response Defined Code
   *
   * <p>
   */
  public enum ResponseCode {

    /** Upload file success */
    UPLOAD_OK(10000),

    /** upload file failed */
    UPLOAD_FAILED(-10000),

    DOWNLOAD_OK(20000),

    DOWNLOAD_FAILED(-20000);

    int code;

    ResponseCode(int code) {
      this.code = code;
    }

    public int code() {
      return code;
    }
  }

  public interface AuthHeader {

    /** Nas AppId Key */
    String NAS_APP_ID = "NAS-APPID";

    /** Nas AppKey Key */
    String NAS_APP_KEY = "NAS-APPKEY";
  }

  /** Request Defined */
  public interface NasRequest {

    String CONTEXT_PATH = "/acmedcare-nas/nas";

    /** Upload file request context path */
    String UPLOAD = CONTEXT_PATH + "/submit";

    /** Download file with fid */
    String DOWNLOAD = CONTEXT_PATH + "/%s";
  }
}
