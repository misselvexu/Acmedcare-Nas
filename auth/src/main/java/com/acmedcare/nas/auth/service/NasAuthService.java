package com.acmedcare.nas.auth.service;

import com.acmedcare.nas.api.bean.NasApp;
import com.acmedcare.nas.auth.exception.NasAuthException;
import lombok.NonNull;

/**
 * {@link NasAuthService}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019/11/21.
 */
public class NasAuthService {

  /**
   * Nas Application Auth Operate
   *
   * @param appId nas client app id
   * @param appKey nas client app key
   * @return valid ,return instance of {@link NasApp}
   * @throws NasAuthException invalid ,thrown {@link NasAuthException}
   */
  @NonNull
  public NasApp auth(String appId, String appKey) throws NasAuthException {

    // TODO

    return null;
  }
}
