package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import static org.apache.commons.lang3.Validate.notNull;

import javax.ws.rs.WebApplicationException;

/**
 * Exception to be thrown when an unexpected error is received from the Gateway.
 */
public class UnknownGatewayErrorException extends WebApplicationException {

  public UnknownGatewayErrorException(final Integer status) {
    super(notNull(status, "status must not be null"));
  }
}
