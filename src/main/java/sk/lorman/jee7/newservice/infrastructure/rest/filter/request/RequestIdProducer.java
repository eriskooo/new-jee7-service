package sk.lorman.jee7.newservice.infrastructure.rest.filter.request;

import sk.lorman.jee7.newservice.infrastructure.qualifier.Current;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

/**
 * Produces the current request id for the application.
 */
@RequestScoped
public class RequestIdProducer {

  private static final Logger LOG = LoggerFactory.getLogger(RequestIdProducer.class);

  private RequestId requestId;

  @Produces
  @RequestScoped
  @Current
  public RequestId getRequestId() {
    return requestId;
  }

  public void observeRequestId(@Observes @Current final RequestId id) {
    LOG.debug("Observed request id {}", id);
    this.requestId = id;
  }
}
