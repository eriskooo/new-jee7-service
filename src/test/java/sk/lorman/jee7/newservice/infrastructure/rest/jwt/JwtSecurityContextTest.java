package sk.lorman.jee7.newservice.infrastructure.rest.jwt;

import static sk.lorman.jee7.newservice.infrastructure.security.Roles.CASHIER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Test class for security context {@link JwtSecurityContext}.
 */
public class JwtSecurityContextTest {

  @Test
  public void getAuthenticationScheme() {
    JwtSecurityContext securityContext = new JwtSecurityContext("johndoe1", CASHIER);
    assertThat(securityContext.getAuthenticationScheme()).isEqualTo("JWT");
  }

  @Test
  public void isUserInRole() {
    JwtSecurityContext securityContext = new JwtSecurityContext("johndoe1", CASHIER);
    assertThat(securityContext.isUserInRole(CASHIER)).isTrue();
    assertThat(securityContext.isUserInRole("unknown")).isFalse();
  }
}
