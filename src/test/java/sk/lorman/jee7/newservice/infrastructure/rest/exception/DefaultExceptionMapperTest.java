package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import static org.assertj.core.api.Assertions.assertThat;

import sk.lorman.jee7.newservice.infrastructure.error.ApplicationErrorDTO;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 * Test class for the exception mapper {@link DefaultExceptionMapper}.
 */
public class DefaultExceptionMapperTest {

  private DefaultExceptionMapper exceptionMapper;

  @Before
  public void setUp() {
    exceptionMapper = new DefaultExceptionMapper();
  }

  @Test
  public void toResponseShouldReturn401ForNonApplicationException() {
    Response response = exceptionMapper.toResponse(new NotAuthorizedException("Unauthorized access"));
    assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
    assertThat(response.hasEntity()).isFalse();
  }

  @Test
  public void toResponseShouldReturn403ForForbiddenException() {
    Response response = exceptionMapper.toResponse(new ForbiddenException());
    assertThat(response.getStatus()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode());
    assertThat(response.hasEntity()).isFalse();
  }

  @Test
  public void toResponseShouldReturn404ForNotFoundException() {
    Response response = exceptionMapper.toResponse(new NotFoundException());
    assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    assertThat(response.hasEntity()).isFalse();
  }

  @Test
  public void toResponseShouldReturn500ForNonApplicationException() {
    Response response = exceptionMapper.toResponse(new IllegalStateException("Illegal state occurred"));
    assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    assertThat(response.hasEntity()).isTrue();

    List<ApplicationErrorDTO> errors = (List<ApplicationErrorDTO>) response.getEntity();
    assertThat(errors).hasSize(1);

    ApplicationErrorDTO error = errors.get(0);
    assertThat(error.getCode()).isEqualTo("UNKNOWN");
    assertThat(error.getMessage()).isEqualTo("Illegal state occurred");
  }

  @Test
  public void toResponseShouldReturn500ForWebApplicationException() {
    Response response = exceptionMapper.toResponse(new InternalServerErrorException());
    assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    assertThat(response.hasEntity()).isTrue();

    List<ApplicationErrorDTO> errors = (List<ApplicationErrorDTO>) response.getEntity();
    assertThat(errors).hasSize(1);

    ApplicationErrorDTO error = errors.get(0);
    assertThat(error.getCode()).isEqualTo("UNKNOWN");
    assertThat(error.getMessage()).isEqualTo("HTTP 500 Internal Server Error");
  }
}
