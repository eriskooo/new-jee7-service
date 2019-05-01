package sk.lorman.jee7.newservice.infrastructure.rest.filter.request;

import static sk.lorman.jee7.newservice.infrastructure.rest.filter.request.TestRequestIds.REQUEST_ID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class for the value object {@link RequestId}.
 */
public class RequestIdTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void instantiationShouldFailForMissingValue() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("value must not be null");
    new RequestId(null);
  }

  @Test
  public void instantiationSucceed() {
    new RequestId(REQUEST_ID);
  }
}