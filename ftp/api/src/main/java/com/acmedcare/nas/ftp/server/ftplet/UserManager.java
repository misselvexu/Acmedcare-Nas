package com.acmedcare.nas.ftp.server.ftplet;

/**
 * User manager interface.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface UserManager {

  /**
   * Get user by name.
   *
   * @param username the name to search for.
   * @return the user with the specified name, or null if a such user does not exist.
   * @throws FtpException when the UserManager can't fulfill the request.
   */
  User getUserByName(String username) throws FtpException;

  /**
   * Get all user names in the system.
   *
   * @return an array of username strings, note that the result should never be null, if there is no
   * users the result is an empty array.
   * @throws FtpException when the UserManager can't fulfill the request.
   */
  String[] getAllUserNames() throws FtpException;

  /**
   * Delete the user from the system.
   *
   * @param username The name of the {@link User} to delete
   * @throws FtpException                  when the UserManager can't fulfill the request.
   * @throws UnsupportedOperationException if UserManager in read-only mode
   */
  void delete(String username) throws FtpException;

  /**
   * Save user. If a new user, create it else update the existing user.
   *
   * @param user the Uset to save
   * @throws FtpException                  when the UserManager can't fulfill the request.
   * @throws UnsupportedOperationException if UserManager in read-only mode
   */
  void save(User user) throws FtpException;

  /**
   * Check if the user exists.
   *
   * @param username the name of the user to check.
   * @return true if the user exist, false otherwise.
   * @throws FtpException
   */
  boolean doesExist(String username) throws FtpException;

  /**
   * Authenticate user
   *
   * @param authentication The {@link Authentication} that proves the users identity
   * @return the authenticated account.
   * @throws AuthenticationFailedException
   * @throws FtpException                  when the UserManager can't fulfill the request.
   */
  User authenticate(Authentication authentication) throws AuthenticationFailedException;

  /**
   * Get admin user name
   *
   * @return the admin user name
   * @throws FtpException when the UserManager can't fulfill the request.
   */
  String getAdminName() throws FtpException;

  /**
   * Check if the user is admin.
   *
   * @param username The name of the {@link User} to check
   * @return true if user with this login is administrator
   * @throws FtpException when the UserManager can't fulfill the request.
   */
  boolean isAdmin(String username) throws FtpException;
}
