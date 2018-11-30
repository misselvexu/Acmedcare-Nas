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
package com.acmedcare.nas.common.kits;

import com.acmedcare.nas.common.log.AcmedcareNasLogger;
import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/** @author Elve.Xu */
public class SystemKits {

  /** The key of nacos home. */
  public static final String NAS_HOME_KEY = "nas.home";

  public static final String NAS_LOCAL_IP_KEY = "NAS_LOCAL_IP";

  public static final String LOCAL_IP = getHostAddress();

  /** The home of nacos. */
  public static final String NAS_HOME = getNasHome();

  private static OperatingSystemMXBean operatingSystemMXBean =
      (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

  public static List<String> getIPsBySystemEnv(String key) {
    String env = getSystemEnv(key);
    List<String> ips = new ArrayList<String>();
    if (StringUtils.isNotEmpty(env)) {
      ips = Arrays.asList(env.split(","));
    }
    return ips;
  }

  public static String getSystemEnv(String key) {
    return System.getenv(key);
  }

  public static float getLoad() {
    return (float) operatingSystemMXBean.getSystemLoadAverage();
  }

  public static float getCPU() {
    return (float) operatingSystemMXBean.getSystemCpuLoad();
  }

  public static float getMem() {
    return (float)
        (1
            - (double) operatingSystemMXBean.getFreePhysicalMemorySize()
                / (double) operatingSystemMXBean.getTotalPhysicalMemorySize());
  }

  private static String getHostAddress() {
    String address = "127.0.0.1";

    try {
      Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
      while (networkInterfaces.hasMoreElements()) {
        NetworkInterface networkInterface = networkInterfaces.nextElement();
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
          InetAddress ip = inetAddresses.nextElement();
          // 兼容不规范网段
          if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
            return ip.getHostAddress();
          }
        }
      }
    } catch (Exception e) {
      AcmedcareNasLogger.logger().error("get local host address error", e);
    }

    return address;
  }

  private static String getNasHome() {
    String nacosHome = System.getProperty(NAS_HOME_KEY);
    if (StringUtils.isBlank(nacosHome)) {
      nacosHome = System.getProperty("user.home") + File.separator + "nas";
    }
    return nacosHome;
  }
}
