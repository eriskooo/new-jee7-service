package sk.lorman.jee7.newservice.infrastructure.rest.filter.request;

import static sk.lorman.jee7.newservice.infrastructure.rest.filter.request.RequestIdFilter.DEFAULT_REQUEST_VALUE;
import static sk.lorman.jee7.newservice.infrastructure.rest.filter.request.TestRequestIds.REQUEST_ID;
import static sk.lorman.jee7.newservice.infrastructure.web.CustomHttpHeader.X_REQUEST_ID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import javax.enterprise.event.Event;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * Test class for the filter {@link RequestIdFilter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestIdFilterTest {

  @InjectMocks
  private RequestIdFilter filter;

  @Mock
  private Event<RequestId> event;

  @Mock
  private ContainerRequestContext context;

  @Test
  public void givenExistingHeaderWhenFilterThenFireEvent() throws IOException {
    Mockito.when(context.getHeaderString(X_REQUEST_ID)).thenReturn(REQUEST_ID);

    filter.filter(context);

    Mockito.verify(event).fire(new RequestId(REQUEST_ID));
  }

  @Test
  public void givenNonExistingHeaderWhenFilterThenFireEvent() throws IOException {
    Mockito.when(context.getHeaderString(X_REQUEST_ID)).thenReturn(null);

    filter.filter(context);

    Mockito.verify(event).fire(new RequestId(DEFAULT_REQUEST_VALUE));
  }
}