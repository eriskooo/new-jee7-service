package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import sk.lorman.jee7.newservice.infrastructure.error.ApplicationErrorDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Custom exception mapper. Handles all exceptions from gateway. Prevents leaking internal details to the client.
 */
@Provider
public class GatewayExceptionMapper implements ExceptionMapper<AbstractGatewayException> {

  private static final Logger LOG = LoggerFactory.getLogger(GatewayExceptionMapper.class);

  @Override
  public Response toResponse(final AbstractGatewayException exception) {
    LOG.error(exception.getMessage(), exception);

    ApplicationErrorDTO error = new ApplicationErrorDTO(() -> "GATEWAY", exception.getError().getErrorMessage());

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity(new GenericEntity<List<ApplicationErrorDTO>>(Collections.singletonList(error)) {
        })
        .build();
  }
}
