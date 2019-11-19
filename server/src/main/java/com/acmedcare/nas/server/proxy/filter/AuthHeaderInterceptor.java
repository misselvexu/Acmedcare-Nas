package com.acmedcare.nas.server.proxy.filter;

import com.acmedcare.nas.common.BizResult;
import com.acmedcare.nas.common.BizResult.ExceptionWrapper;
import com.acmedcare.nas.common.exception.NasException;
import com.acmedcare.nas.server.NasAutoConfiguration.ApplicationContext;
import com.acmedcare.nas.server.proxy.ProxyInterceptor;
import com.acmedcare.nas.server.proxy.ProxyInterceptor.Order;
import com.google.common.collect.Maps;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * Auth Header Filter
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version v1.0 - 28/08/2018.
 */
@Order(value = 0)
public class AuthHeaderInterceptor extends ProxyInterceptor {

  public AuthHeaderInterceptor() {
    super(ApplicationContext.getProxyConfig().getContextPath() + "/");
  }

  @Override
  public boolean match(String frontRequestUri) {
    return super.match(frontRequestUri);
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

    //

    BizResult.BizResultBuilder builder = BizResult.builder();
    // check auth token /account /org
    for (String extProxyHeader : EXT_PROXY_HEADERS) {
      if (!headers.containsKey(extProxyHeader)) {
        builder.exception(
            ExceptionWrapper.builder()
                .message("Request Header [" + extProxyHeader + "] is needed~")
                .build());
        break;
      }
    }

    BizResult result = builder.build();
    result.setCode(result.getException() == null ? 0 : -1);

    if (result.getCode() == -1) {
      // build response
      buildResponse(
          originalResponse,
          HttpStatus.SC_UNAUTHORIZED,
          result.bytes(),
          MediaType.TEXT_HTML_VALUE,
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
