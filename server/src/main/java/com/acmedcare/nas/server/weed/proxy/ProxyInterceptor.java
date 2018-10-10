package com.acmedcare.nas.server.weed.proxy;

import com.acmedcare.nas.common.exception.NasException;
import com.acmedcare.nas.common.log.AcmedcareNasLogger;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.http.MediaType;

/**
 * Proxy Filter
 *
 * @author Elve.Xu [iskp.me<at>gmail.com]
 * @version v1.0 - 28/08/2018.
 */
public abstract class ProxyInterceptor {

  /** Proxy Request Required Header */
  protected static final String[] EXT_PROXY_HEADERS = {"NAS-APPID", "NAS-APPKEY"};

  private static final Pattern FIX_REGEX = Pattern.compile("/nas/\\d+,[0-9A-Za-z_*\\-]+");
  /**
   * Url Mapping Prefix
   *
   * <pre>
   *
   *
   * </pre>
   */
  @Getter private final String urlMappingPrefix;

  public ProxyInterceptor(String urlMappingPrefix) {
    if (StringUtils.isNoneBlank(urlMappingPrefix)) {
      // replace `*` or `**` ,not support regex yet~
      this.urlMappingPrefix = urlMappingPrefix.replaceAll("\\*?", "");
    } else {
      this.urlMappingPrefix = "/";
    }
  }

  /**
   * Build Response With HTTP_STATUS
   *
   * @param response response
   */
  protected void buildResponse(
      HttpServletResponse response,
      int httpStatus,
      byte[] content,
      String contentType,
      Map<String, String> headers) {

    try {
      if (StringUtils.isBlank(contentType)) {
        contentType = MediaType.APPLICATION_JSON_UTF8_VALUE;
      }
      // set content type
      response.setContentType(contentType);

      // set headers
      if (headers != null && headers.size() > 0) {
        for (String key : headers.keySet()) {
          String value = headers.get(key);
          response.setHeader(key, value);
        }
      }

      if (content != null) {
        response.setIntHeader("Content-Length", content.length);
        // flush content
        response.getOutputStream().write(content);
        // set content
      }
    } catch (Exception e) {
      AcmedcareNasLogger.logger().error("Build Response Error ,{}", e);
    }
  }

  /**
   * CHeck is macth current rule
   *
   * @param frontRequestUri requets url
   * @return true/ false
   */
  public boolean match(String frontRequestUri) {
    if (StringUtils.isBlank(frontRequestUri)) {
      return false;
    }
    Matcher matcher = FIX_REGEX.matcher(frontRequestUri);
    if (matcher.matches()) {
      return false;
    }
    return StringUtils.startsWith(frontRequestUri, urlMappingPrefix);
  }

  /**
   * Before invoke proxy request
   *
   * @param originalRequest original request
   * @param originalResponse original response
   * @throws NasException exception
   */
  public abstract int beforeProxy(
      HttpServletRequest originalRequest, HttpServletResponse originalResponse) throws NasException;

  /**
   * invoke method after proxy response
   *
   * @param proxyResponse proxy response
   * @param originalResponse original response
   * @throws NasException exception
   */
  public abstract void afterProxy(HttpResponse proxyResponse, HttpServletResponse originalResponse)
      throws NasException;

  /**
   * This Method Only For Wrapper Flag is true
   *
   * @param response response
   * @throws NasException exception
   */
  public int onTailProcess(String response, HttpServletResponse originalResponse)
      throws NasException {
    return 0;
  }

  /**
   * Filter Order Annotation
   *
   * @author Elve.Xu
   */
  @Retention(RetentionPolicy.CLASS)
  @Target({ElementType.TYPE})
  @Documented
  public @interface Order {

    /**
     * Order Value
     *
     * @return value
     */
    int value() default 0;
  }
}
