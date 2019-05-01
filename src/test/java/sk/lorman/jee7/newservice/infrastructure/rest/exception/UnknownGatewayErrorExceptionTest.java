package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class for the exception {@link UnknownGatewayErrorException}.
 */
public class UnknownGatewayErrorExceptionTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void instantiationShouldFailForMissingStatus() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("status must not be null");
    new UnknownGatewayErrorException(null);
  }

  @Test
  public void instantiationShouldSucceed() {
    UnknownGatewayErrorException exception = new UnknownGatewayErrorException(SERVICE_UNAVAILABLE.getStatusCode());
    assertThat(exception.getMessage()).isEqualTo("HTTP 503 Service Unavailable");
  }
}
