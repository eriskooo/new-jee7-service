package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import static org.apache.commons.lang3.Validate.notNull;

import sk.lorman.jee7.newservice.infrastructure.error.GatewayErrorDTO;

import javax.ws.rs.WebApplicationException;

/**
 * Provide means for Gateway error exceptions.
 */
public abstract class AbstractGatewayException extends WebApplicationException {

  private final GatewayErrorDTO error;

  protected AbstractGatewayException(final Integer status, final GatewayErrorDTO error) {
    super(notNull(status, "status must not be null"));
    this.error = notNull(error, "error must not be null");
  }

  public GatewayErrorDTO getError() {
    return error;
  }
}
