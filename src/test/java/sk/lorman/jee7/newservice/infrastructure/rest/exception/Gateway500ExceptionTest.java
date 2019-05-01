package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import static org.assertj.core.api.Assertions.assertThat;

import sk.lorman.jee7.newservice.infrastructure.error.GatewayErrorDTO;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class for the exception {@link Gateway500Exception}.
 */
public class Gateway500ExceptionTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void instantiationShouldFailForMissingGatewayErrorDTO() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("error must not be null");
    new Gateway500Exception(null);
  }

  @Test
  public void instantiationShouldSucceed() {
    Gateway500Exception exception = new Gateway500Exception(new GatewayErrorDTO());
    assertThat(exception.getResponse().getStatus()).isEqualTo(500);
    assertThat(exception.getError()).isNotNull();
  }
}
