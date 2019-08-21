package com.acmedcare.nas.ftp.server.command;

/**
 * Command factory interface.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface CommandFactory {

  /**
   * Get the command instance.
   *
   * @param commandName The name of the command to create
   * @return The {@link Command} matching the provided name, or null if no such command exists.
   */
  Command getCommand(String commandName);

  /**
   * Get the registered SITE commands
   *
   * @return Active site commands, the key is the site command name, used in FTP sessions as SITE
   *     <command name>
   */
  // Map<String, Command> getSiteCommands();
  /**
   * Register SITE commands. The map can replace or append to the default SITE commands provided by
   * FtpServer depending on the value of {@see CommandFactory#isUseDefaultSiteCommands()}
   *
   * @param siteCommands Active site commands, the key is the site command name, used in FTP
   *     sessions as SITE <command name>. The value is the command
   */
  // void setSiteCommands(Map<String, Command> siteCommands);
  /**
   * Should custom site commands append to or replace the default commands provided by FtpServer?.
   * The default is to append
   *
   * @return true if custom commands should append to the default, false if they should replace
   */
  // boolean isUseDefaultSiteCommands();
  /**
   * Should custom site commands append to or replace the default commands provided by FtpServer?.
   *
   * @param useDefaultSiteCommands true if custom commands should append to the default, false if
   *     they should replace
   */
  // void setUseDefaultSiteCommands(boolean useDefaultSiteCommands);
}
