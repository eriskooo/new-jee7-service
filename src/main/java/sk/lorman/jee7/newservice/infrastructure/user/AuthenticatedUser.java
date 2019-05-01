package sk.lorman.jee7.newservice.infrastructure.user;

import static org.apache.commons.lang3.Validate.notNull;

import sk.lorman.jee7.newservice.infrastructure.domain.value.AbstractValueObject;

/**
 * A value object that represents an authenticated user.
 */
public class AuthenticatedUser extends AbstractValueObject {

  private String username;

  private String firstName;

  private String lastName;

  public AuthenticatedUser() {
    super();
  }

  public AuthenticatedUser(final String username, final String firstName, final String lastName) {
    this();
    this.username = notNull(username, "username must not be null");
    this.firstName = notNull(firstName, "firstName must not be null");
    this.lastName = notNull(lastName, "lastName must not be null");
  }

  public String getUsername() {
    return username;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  @Override
  protected Object[] values() {
    return new Object[]{username, firstName, lastName};
  }

  @Override
  public String toString() {
    return "AuthenticatedUser{"
           + "username='" + username + '\''
           + ", firstName='" + firstName + '\''
           + ", lastName='" + lastName + '\''
           + '}';
  }
}
