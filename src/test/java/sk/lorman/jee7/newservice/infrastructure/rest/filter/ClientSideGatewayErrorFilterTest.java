package sk.lorman.jee7.newservice.infrastructure.rest.filter;

import static javax.ws.rs.core.Response.Status.BAD_GATEWAY;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;
import static javax.ws.rs.core.Response.Status.TEMPORARY_REDIRECT;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import sk.lorman.jee7.newservice.infrastructure.rest.exception.Gateway400Exception;
import sk.lorman.jee7.newservice.infrastructure.rest.exception.Gateway500Exception;
import sk.lorman.jee7.newservice.infrastructure.rest.exception.UnknownGatewayErrorException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;

/**
 * Test class for the JAX-RS client filter {@link ClientSideGatewayErrorFilter}.
 */
public class ClientSideGatewayErrorFilterTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private ClientSideGatewayErrorFilter filter;

  private ClientRequestContext requestContext;

  private ClientResponseContext responseContext;

  @Before
  public void setUp() {
    filter = new ClientSideGatewayErrorFilter();
    requestContext = Mockito.mock(ClientRequestContext.class);
    responseContext = Mockito.mock(ClientResponseContext.class);
  }

  @Test
  public void filterShouldReturnVoidForStatusFamilyInformational() {
    Mockito.when(responseContext.getStatus()).thenReturn(101);
    filter.filter(requestContext, responseContext);
  }

  @Test
  public void filterShouldReturnVoidForStatusFamilyRedirect() {
    Mockito.when(responseContext.getStatus()).thenReturn(TEMPORARY_REDIRECT.getStatusCode());
    filter.filter(requestContext, responseContext);
  }

  @Test
  public void filterShouldReturnVoidForStatusFamilySuccessful() {
    Mockito.when(responseContext.getStatus()).thenReturn(OK.getStatusCode());
    filter.filter(requestContext, responseContext);
  }

  @Test
  public void filterShouldReturnVoidForServerError502() {
    Mockito.when(responseContext.getStatus()).thenReturn(BAD_GATEWAY.getStatusCode());
    filter.filter(requestContext, responseContext);
  }

  @Test
  public void filterShouldThrowGateway400ExceptionForBadRequest() {
    thrown.expect(Gateway400Exception.class);
    Mockito.when(responseContext.getStatus()).thenReturn(BAD_REQUEST.getStatusCode());
    Mockito.when(responseContext.hasEntity()).thenReturn(Boolean.TRUE);
    Mockito.when(responseContext.getEntityStream())
        .thenReturn(ClientSideGatewayErrorFilterTest.class.getResourceAsStream("/__files/json/BadRequest.json"));

    filter.filter(requestContext, responseContext);
  }

  @Test
  public void filterShouldThrowGateway500ExceptionForInternalServerError() {
    thrown.expect(Gateway500Exception.class);
    Mockito.when(responseContext.getStatus()).thenReturn(INTERNAL_SERVER_ERROR.getStatusCode());
    Mockito.when(responseContext.hasEntity()).thenReturn(Boolean.TRUE);
    Mockito.when(responseContext.getEntityStream())
        .thenReturn(ClientSideGatewayErrorFilterTest.class.getResourceAsStream("/__files/json/InternalServerError.json"));

    filter.filter(requestContext, responseContext);
  }

  @Test
  public void filterShouldThrowUnknownGatewayErrorExceptionForUnauthorized() {
    thrown.expect(UnknownGatewayErrorException.class);
    Mockito.when(responseContext.getStatus()).thenReturn(UNAUTHORIZED.getStatusCode());
    filter.filter(requestContext, responseContext);
  }

  @Test
  public void filterShouldThrowUnknownGatewayErrorExceptionForServiceUnavailable() {
    thrown.expect(UnknownGatewayErrorException.class);
    Mockito.when(responseContext.getStatus()).thenReturn(SERVICE_UNAVAILABLE.getStatusCode());
    filter.filter(requestContext, responseContext);
  }
}
