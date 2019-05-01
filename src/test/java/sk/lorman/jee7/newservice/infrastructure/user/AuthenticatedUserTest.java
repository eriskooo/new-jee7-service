package sk.lorman.jee7.newservice.infrastructure.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class for the value object {@link AuthenticatedUser}.
 */
public class AuthenticatedUserTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void instantiationShouldFailForMissingFirstName() {
    thrown.expect(NullPointerException.class);
    new AuthenticatedUser("johndoe1", null, "Doe");
    thrown.expectMessage("firstName must not be null");
  }

  @Test
  public void instantiationShouldFailForMissingLastName() {
    thrown.expect(NullPointerException.class);
    new AuthenticatedUser("johndoe1", "John", null);
    thrown.expectMessage("lastName must not be null");
  }

  @Test
  public void instantiationShouldFailForMissingUserName() {
    thrown.expect(NullPointerException.class);
    new AuthenticatedUser(null, "John", "Doe");
    thrown.expectMessage("userName must not be null");
  }

  @Test
  public void instantiationShouldSucceed() {
    AuthenticatedUser authenticatedUser = new AuthenticatedUser("johndoe1", "John", "Doe");
    assertThat(authenticatedUser.getUsername()).isEqualTo("johndoe1");
    assertThat(authenticatedUser.getFirstName()).isEqualTo("John");
    assertThat(authenticatedUser.getLastName()).isEqualTo("Doe");
  }
}
