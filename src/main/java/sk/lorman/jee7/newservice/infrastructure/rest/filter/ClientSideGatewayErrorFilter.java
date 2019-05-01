package sk.lorman.jee7.newservice.infrastructure.rest.filter;

import static javax.ws.rs.core.Response.Status.BAD_GATEWAY;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.Family.CLIENT_ERROR;
import static javax.ws.rs.core.Response.Status.Family.SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import sk.lorman.jee7.newservice.infrastructure.error.GatewayErrorDTO;
import sk.lorman.jee7.newservice.infrastructure.rest.exception.Gateway400Exception;
import sk.lorman.jee7.newservice.infrastructure.rest.exception.Gateway500Exception;
import sk.lorman.jee7.newservice.infrastructure.rest.exception.UnknownGatewayErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;

/**
 * JAX-RS client filter. Process gateway errors.
 *
 * @see javax.ws.rs.client.ClientBuilder#register(Object)
 */
public class ClientSideGatewayErrorFilter implements ClientResponseFilter {

  private static final Logger LOG = LoggerFactory.getLogger(ClientSideGatewayErrorFilter.class);

  @Override
  public void filter(final ClientRequestContext requestContext, final ClientResponseContext responseContext) {
    int status = responseContext.getStatus();

    if (isBadGatewayError(status)) {
      return;
    }

    if (isBadRequestError(status)) {
      GatewayErrorDTO error = getErrorDto(responseContext);
      throw new Gateway400Exception(error);
    }

    if (isInternalServerError(status)) {
      GatewayErrorDTO error = getErrorDto(responseContext);
      throw new Gateway500Exception(error);
    }

    if (isUnknownError(status)) {
      LOG.error("Unknown gateway error with status {}", status);
      throw new UnknownGatewayErrorException(status);
    }
  }

  private boolean isBadGatewayError(final int status) {
    return BAD_GATEWAY.getStatusCode() == status;
  }

  private boolean isBadRequestError(int status) {
    return BAD_REQUEST.getStatusCode() == status;
  }

  private GatewayErrorDTO getErrorDto(final ClientResponseContext responseContext) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    if (responseContext.hasEntity()) {
      try {
        return mapper.readValue(responseContext.getEntityStream(), GatewayErrorDTO.class);
      } catch (IOException e) {
        return null;
      }
    }
    return null;
  }

  private boolean isInternalServerError(int status) {
    return INTERNAL_SERVER_ERROR.getStatusCode() == status;
  }

  private boolean isUnknownError(int status) {
    return isClientError(status) && !isBadRequestError(status)
           || isServerError(status) && !isInternalServerError(status);
  }

  private boolean isClientError(final int status) {
    Response.Status.Family family = Response.Status.Family.familyOf(status);
    return CLIENT_ERROR.equals(family);
  }

  private boolean isServerError(final int status) {
    Response.Status.Family family = Response.Status.Family.familyOf(status);
    return SERVER_ERROR.equals(family);
  }
}
