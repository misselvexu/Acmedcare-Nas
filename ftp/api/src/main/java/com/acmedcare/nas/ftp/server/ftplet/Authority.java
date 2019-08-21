package com.acmedcare.nas.ftp.server.ftplet;

/**
 * Interface for an authority granted to the user, typical example is write access or the number of
 * concurrent logins
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface Authority {

  /**
   * Indicates weather this Authority can authorize a certain request
   *
   * @param request The request to authorize
   * @return True if the request can be authorized by this Authority
   */
  boolean canAuthorize(AuthorizationRequest request);

  /**
   * Authorize an {@link AuthorizationRequest}.
   *
   * @param request The {@link AuthorizationRequest}
   * @return Returns a populated AuthorizationRequest as long as If {@link
   * #canAuthorize(AuthorizationRequest)} returns true for the AuthorizationRequest, otherwise
   * returns null. {@link #canAuthorize(AuthorizationRequest)} should always be checked before
   * calling this method.
   */
  AuthorizationRequest authorize(AuthorizationRequest request);
}
