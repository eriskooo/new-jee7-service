package sk.lorman.jee7.newservice.infrastructure.rest.jwt;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static org.apache.commons.lang3.Validate.notNull;

import com.auth0.jwt.interfaces.DecodedJWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 * JAX-RS client filter. Adds bearer token to the request.
 *
 * @see javax.ws.rs.client.ClientBuilder#register(Object)
 */
public class ClientSideJwtAuthorizationFilter implements ClientRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(ClientSideJwtAuthorizationFilter.class);

  private final DecodedJWT jwt;

  public ClientSideJwtAuthorizationFilter(final DecodedJWT jwt) {
    this.jwt = notNull(jwt, "jwt must not be null");
  }

  @Override
  public void filter(final ClientRequestContext requestContext) {
    String bearerToken = String.format("Bearer %s", jwt.getToken());
    LOG.debug("add http header {} = {}", AUTHORIZATION, bearerToken);
    requestContext.getHeaders().putSingle(AUTHORIZATION, bearerToken);
  }
}
