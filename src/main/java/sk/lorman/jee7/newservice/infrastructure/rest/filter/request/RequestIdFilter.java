package sk.lorman.jee7.newservice.infrastructure.rest.filter.request;

import sk.lorman.jee7.newservice.infrastructure.qualifier.Current;
import sk.lorman.jee7.newservice.infrastructure.web.CustomHttpHeader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 * Filter that receives the request Id from the HTTP header and fire it as a CDI event.
 */
@Provider
@PreMatching
public class RequestIdFilter implements ContainerRequestFilter {

  static final String DEFAULT_REQUEST_VALUE = "unset";

  private static final Logger LOG = LoggerFactory.getLogger(ClientSideRequestIdFilter.class);

  @Inject
  @Current
  private Event<RequestId> event;

  @Override
  public void filter(final ContainerRequestContext context) {
    String requestId = getRequestId(context);

    LOG.debug("Fire event with request Id {}", requestId);
    event.fire(new RequestId(requestId));
  }

  private String getRequestId(final ContainerRequestContext context) {
    String requestId = context.getHeaderString(CustomHttpHeader.X_REQUEST_ID);
    return StringUtils.defaultString(requestId, DEFAULT_REQUEST_VALUE);
  }
}
