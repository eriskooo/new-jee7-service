package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import static org.assertj.core.api.Assertions.assertThat;

import sk.lorman.jee7.newservice.infrastructure.error.GatewayErrorDTO;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class for the exception {@link UnknownPsiStateException}.
 */
public class UnknownPsiStateExceptionTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void instantiationShouldFailForMissingGatewayErrorDTO() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("error must not be null");
    new UnknownPsiStateException(null);
  }

  @Test
  public void instantiationShouldSucceed() {
    UnknownPsiStateException exception = new UnknownPsiStateException(new GatewayErrorDTO());
    assertThat(exception.getResponse().getStatus()).isEqualTo(502);
    assertThat(exception.getError()).isNotNull();
  }
}
