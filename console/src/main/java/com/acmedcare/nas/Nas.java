/*
 * Copyright 1999-2018 Acmedcare+ Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acmedcare.nas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Nas Main Class
 *
 * @author Elve.Xu [iskp.me<at>gmail.com]
 * @version v1.0 - 27/09/2018.
 */
@SpringBootApplication(scanBasePackages = "com.acmedcare.nas")
@ServletComponentScan
public class Nas {

  /**
   * Main Method For Application Startup
   *
   * @param args args
   */
  public static void main(String[] args) {
    SpringApplication.run(Nas.class, args);
  }
}
