package sk.lorman.jee7.newservice.infrastructure.rest.filter.request;

import static sk.lorman.jee7.newservice.infrastructure.rest.filter.request.TestRequestIds.REQUEST_ID;
import static sk.lorman.jee7.newservice.infrastructure.web.CustomHttpHeader.X_REQUEST_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;

/**
 * Test class for the JAX-RS client filter {@link ClientSideRequestIdFilter}.
 */
public class ClientSideRequestIdFilterTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private ClientSideRequestIdFilter filter;

  @Before
  public void setUp() throws Exception {
    filter = new ClientSideRequestIdFilter(new RequestId(REQUEST_ID));
  }

  @Test
  public void filter() {
    MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

    ClientRequestContext context = mock(ClientRequestContext.class);
    Mockito.when(context.getHeaders()).thenReturn(headers);

    filter.filter(context);

    assertThat(headers).containsKey(X_REQUEST_ID);
    assertThat(headers.getFirst(X_REQUEST_ID)).isEqualTo(REQUEST_ID);
  }

  @Test
  public void instantiationShouldFailForMissingParam() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("requestId must not be null");
    new ClientSideRequestIdFilter(null);
  }
}
