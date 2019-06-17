package com.acmedcare.nas.ftp.server;

import com.acmedcare.nas.ftp.server.ssl.SslConfiguration;

import java.net.InetAddress;

/**
 * Data connection configuration interface.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface DataConnectionConfiguration {

  /**
   * Get the maximum idle time in seconds.
   *
   * @return The maximum idle time
   */
  int getIdleTime();

  /**
   * Is active data connection enabled?
   *
   * @return true if active data connections are enabled
   */
  boolean isActiveEnabled();

  /**
   * Check the PORT IP with the client IP?
   *
   * @return true if the PORT IP is verified
   */
  boolean isActiveIpCheck();

  /**
   * Get the active data connection local host.
   *
   * @return The {@link InetAddress} for active connections
   */
  String getActiveLocalAddress();

  /**
   * Get the active data connection local port.
   *
   * @return The active data connection local port
   */
  int getActiveLocalPort();

  /**
   * Get passive server address. null, if not set in the configuration.
   *
   * @return The {@link InetAddress} used for passive connections
   */
  String getPassiveAddress();

  /**
   * Get the passive address that will be returned to clients on the PASV command.
   *
   * @return The passive address to be returned to clients, null if not configured.
   */
  String getPassiveExernalAddress();

  /**
   * Get the passive ports to be used for data connections. Ports can be defined as single ports,
   * closed or open ranges. Multiple definitions can be separated by commas, for example:
   *
   * <ul>
   *   <li>2300 : only use port 2300 as the passive port
   *   <li>2300-2399 : use all ports in the range
   *   <li>2300- : use all ports larger than 2300
   *   <li>2300, 2305, 2400- : use 2300 or 2305 or any port larger than 2400
   * </ul>
   *
   * <p>Defaults to using any available port
   *
   * @return The passive ports string
   */
  String getPassivePorts();

  /**
   * Tells whether or not IP address check is performed when accepting a passive data connection.
   *
   * @return <code>true</code>, if the IP address checking is enabled; <code>false</code>,
   *     otherwise. A value of <code>true</code> means that site to site transfers are disabled. In
   *     other words, a passive data connection MUST be made from the same IP address that issued
   *     the PASV command.
   */
  boolean isPassiveIpCheck();

  /**
   * Request a passive port. Will block until a port is available
   *
   * @return A free passive part
   */
  int requestPassivePort();

  /**
   * Release passive port.
   *
   * @param port The port to be released
   */
  void releasePassivePort(int port);

  /**
   * Get SSL configuration for this data connection.
   *
   * @return The {@link SslConfiguration}
   */
  SslConfiguration getSslConfiguration();

  /** @return True if SSL is mandatory for the data channel */
  boolean isImplicitSsl();
}
