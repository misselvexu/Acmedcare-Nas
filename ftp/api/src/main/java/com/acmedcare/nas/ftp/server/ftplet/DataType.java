package com.acmedcare.nas.ftp.server.ftplet;

/**
 * Type safe enum for describing the data type
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public enum DataType {

  /**
   * Binary data type
   */
  BINARY,

  /**
   * ASCII data type
   */
  ASCII;

  /**
   * Parses the argument value from the TYPE command into the type safe class
   *
   * @param argument The argument value from the TYPE command. Not case sensitive
   * @return The appropriate data type
   * @throws IllegalArgumentException If the data type is unknown
   */
  public static DataType parseArgument(char argument) {
    switch (argument) {
      case 'A':
      case 'a':
        return ASCII;
      case 'I':
      case 'i':
        return BINARY;
      default:
        throw new IllegalArgumentException("Unknown data type: " + argument);
    }
  }
}
