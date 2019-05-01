package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import static org.assertj.core.api.Assertions.assertThat;

import sk.lorman.jee7.newservice.infrastructure.error.ApplicationErrorDTO;
import sk.lorman.jee7.newservice.infrastructure.error.GatewayErrorDTO;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Test class for the exception mapper {@link GatewayExceptionMapper}.
 */
public class GatewayExceptionMapperTest {

  @Test
  public void toResponse() {
    GatewayExceptionMapper exceptionMapper = new GatewayExceptionMapper();

    GatewayErrorDTO errorDTO = Mockito.mock(GatewayErrorDTO.class);
    Mockito.when(errorDTO.getErrorMessage()).thenReturn("");

    Response response = exceptionMapper.toResponse(new Gateway400Exception(errorDTO));

    assertThat(response.getStatus()).isEqualTo(500);
    assertThat((List<ApplicationErrorDTO>) response.getEntity()).hasSize(1);
  }
}
