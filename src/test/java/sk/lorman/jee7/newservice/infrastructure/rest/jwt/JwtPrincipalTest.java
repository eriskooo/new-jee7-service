package sk.lorman.jee7.newservice.infrastructure.rest.jwt;

import static sk.lorman.jee7.newservice.infrastructure.security.Roles.CASHIER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class for the principal {@link JwtPrincipal}.
 */
public class JwtPrincipalTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void instantiationShouldFailForMissingName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name must not be null");
    new JwtPrincipal(null, CASHIER);
  }

  @Test
  public void instantiationShouldFailForMissingRoles() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("roles must not be null");
    new JwtPrincipal("johndoe1", null);
  }

  @Test
  public void instantiationShouldSucceed() {
    JwtPrincipal principal = new JwtPrincipal("johndoe1", CASHIER);
    assertThat(principal.getName()).isEqualTo("johndoe1");
    assertThat(principal.getRoles()).contains(CASHIER);
  }
}
