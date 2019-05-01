package sk.lorman.jee7.newservice.infrastructure.rest.filter.request;

import static sk.lorman.jee7.newservice.infrastructure.web.CustomHttpHeader.X_REQUEST_ID;
import static org.apache.commons.lang3.Validate.notNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 * JAX-RS client filter. Adds request id to the request.
 *
 * @see javax.ws.rs.client.ClientBuilder#register(Object)
 */
public class ClientSideRequestIdFilter implements ClientRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(ClientSideRequestIdFilter.class);

  private final RequestId requestId;

  public ClientSideRequestIdFilter(final RequestId requestId) {
    this.requestId = notNull(requestId, "requestId must not be null");
  }

  @Override
  public void filter(final ClientRequestContext context) {
    LOG.debug("add http header {} = {}", X_REQUEST_ID, requestId);
    context.getHeaders().putSingle(X_REQUEST_ID, requestId.getValue());
  }
}
