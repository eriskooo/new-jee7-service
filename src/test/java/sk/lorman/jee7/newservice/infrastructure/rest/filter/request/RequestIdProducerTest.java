package sk.lorman.jee7.newservice.infrastructure.rest.filter.request;

import static sk.lorman.jee7.newservice.infrastructure.rest.filter.request.TestRequestIds.REQUEST_ID;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Test class for the producer {@link RequestIdProducer}.
 */
public class RequestIdProducerTest {

  @Test
  public void getRequestId() {
    RequestIdProducer producer = new RequestIdProducer();

    producer.observeRequestId(new RequestId(REQUEST_ID));
    assertThat(producer.getRequestId()).isEqualTo(new RequestId(REQUEST_ID));
  }
}
