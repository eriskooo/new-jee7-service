package sk.lorman.jee7.newservice.infrastructure.rest.filter.request;

import sk.lorman.jee7.newservice.infrastructure.domain.value.AbstractSimpleValueObject;

/**
 * A value object that contains a request id.
 */
public class RequestId extends AbstractSimpleValueObject<String> {

  /**
   * CDI only
   */
  public RequestId() {
    super();
  }

  public RequestId(final String requestId) {
    super(requestId);
  }
}
