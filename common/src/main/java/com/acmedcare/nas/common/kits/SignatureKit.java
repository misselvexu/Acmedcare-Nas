package com.acmedcare.nas.common.kits;

import com.acmedcare.nas.common.exception.NasException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Signature Kit
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 30/11/2018.
 */
public class SignatureKit {

  /**
   * Base64 Encoding
   *
   * @param b bytes array
   * @return Base64 encoding content
   */
  public static String base64encoding(byte[] b) {
    return Base64.encodeBytes(b);
  }

  /**
   * Sign
   *
   * @param key key
   * @param strToSign source string content
   * @return sign result
   */
  public static String sign(String key, String strToSign) {

    try {

      byte[] keyBytes = key.getBytes();
      SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(signingKey);

      byte[] rawHmac = mac.doFinal(strToSign.getBytes());

      return base64encoding(rawHmac);
    } catch (Exception e) {
      throw new NasException(e);
    }
  }
}
