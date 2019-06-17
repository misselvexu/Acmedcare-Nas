package com.acmedcare.nas.ftp.server.ftplet;

/**
 * Type safe enum for describing the structure
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public enum Structure {

  /**
   * File structure
   */
  FILE;

  /**
   * Parses the argument value from the STRU command into the type safe class
   *
   * @param argument The argument value from the STRU command. Not case sensitive
   * @return The appropriate structure
   * @throws IllegalArgumentException If the structure is unknown
   */
  public static Structure parseArgument(char argument) {
    switch (argument) {
      case 'F':
      case 'f':
        return FILE;
      default:
        throw new IllegalArgumentException("Unknown structure: " + argument);
    }
  }
}
