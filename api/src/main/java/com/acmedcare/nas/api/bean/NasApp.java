package com.acmedcare.nas.api.bean;

import lombok.*;

import java.io.Serializable;

/**
 * {@link NasApp}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019/11/21.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NasApp implements Serializable {

  private String nasAppId;

  private String nasAppKey;

  private String nasAppName;
}
