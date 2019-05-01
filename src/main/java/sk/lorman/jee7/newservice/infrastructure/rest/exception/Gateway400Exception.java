package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import sk.lorman.jee7.newservice.infrastructure.error.GatewayErrorDTO;

import javax.ws.rs.core.Response;

/**
 * Exception to be thrown when a {@link Response.Status#BAD_REQUEST} error is received from the Gateway.
 */
public class Gateway400Exception extends AbstractGatewayException {

  public Gateway400Exception(final GatewayErrorDTO error) {
    super(Response.Status.BAD_REQUEST.getStatusCode(), error);
  }
}
