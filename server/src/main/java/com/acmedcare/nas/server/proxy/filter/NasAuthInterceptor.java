package com.acmedcare.nas.server.proxy.filter;

import com.acmedcare.nas.common.BizResult;
import com.acmedcare.nas.common.BizResult.ExceptionWrapper;
import com.acmedcare.nas.common.exception.NasException;
import com.acmedcare.nas.server.NasProperties;
import com.acmedcare.nas.server.proxy.AbstractProxyInterceptor;
import com.acmedcare.nas.server.proxy.AbstractProxyInterceptor.Order;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

/**
 * Auth Header Filter
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version v1.0 - 28/08/2018.
 */
@Order(value = 0)
public class NasAuthInterceptor extends AbstractProxyInterceptor {

  private final NasProperties nasProperties;

  public NasAuthInterceptor(NasProperties nasProperties) {
    this.nasProperties = nasProperties;
  }

  @Override
  public boolean match(String frontRequestUri) {

    if (StringUtils.isBlank(frontRequestUri)) {
      return false;
    }

    // is upload uri ?
    if (isNasUploadRequest(frontRequestUri)) {
      if (nasProperties.getAcl() != null && nasProperties.getAcl().isEnabled()) {
        return true;
      }
    }

    // default
    return false;
  }

  /**
   * Before invoke proxy request
   *
   * @param originalRequest original request
   * @throws NasException exception
   */
  @Override
  public int beforeProxy(HttpServletRequest originalRequest, HttpServletResponse originalResponse)
      throws NasException {

    Map<String, String> headers = Maps.newHashMap();
    if (originalRequest != null) {
      Enumeration<String> headerNames = originalRequest.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String headerKey = headerNames.nextElement();
        headers.put(headerKey, originalRequest.getHeader(headerKey));
      }
    }

    BizResult.BizResultBuilder builder = BizResult.builder();
    // check auth token /account /org
    for (String extProxyHeader : EXT_PROXY_HEADERS) {
      if (!headers.containsKey(extProxyHeader)) {
        builder.exception(
            ExceptionWrapper.builder()
                .message("Request Header [" + extProxyHeader + "] is missing.")
                .build());
        break;
      }
    }

    String nasId = headers.get(NAS_APP_ID_NAME);
    String nasKey = headers.get(NAS_APP_KEY_NAME);

    // TODO nas auth service

    BizResult result = builder.build();
    result.setCode(result.getException() == null ? 0 : -1);

    if (result.getCode() == -1) {
      // build response
      buildResponse(
          originalResponse,
          HttpStatus.SC_UNAUTHORIZED,
          result.bytes(),
          MediaType.TEXT_PLAIN_VALUE,
          null);
    }

    return result.getCode();
  }

  /**
   * invoke method after proxy response
   *
   * @param proxyResponse proxy response
   * @throws NasException exception
   */
  @Override
  public void afterProxy(HttpResponse proxyResponse, HttpServletResponse originalResponse)
      throws NasException {
    // ....
  }
}
